package ru.inno.course.finalProject.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import io.restassured.http.ContentType;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.inno.course.finalProject.helpers.PropsHelper;
import ru.inno.course.finalProject.model.AdminUser;
import ru.inno.course.finalProject.model.Company;
import ru.inno.course.finalProject.model.Employee;
import ru.inno.course.finalProject.notificator.TestResultNotificator;
import ru.inno.course.finalProject.services.companyFactory.CompanyFactory;
import ru.inno.course.finalProject.services.employeeFactory.EmployeeFactory;
import ru.inno.course.finalProject.services.employeeService.EmployeeRepository;
import ru.inno.course.finalProject.services.employeeService.EmployeeRepositoryJDBC;
import ru.inno.course.finalProject.services.сompanyService.CompanyRepository;
import ru.inno.course.finalProject.services.сompanyService.CompanyRepositoryJDBC;

@ExtendWith(TestResultNotificator.class)
public class XClientsBusinessTests {

  private static String TOKEN;
  private EmployeeRepository employeeRepository = new EmployeeRepositoryJDBC();
  private CompanyRepository companyRepository = new CompanyRepositoryJDBC();
  private EmployeeFactory employeeFactory = new EmployeeFactory();
  private CompanyFactory companyFactory = new CompanyFactory();
  private int[] employeeIdsToDelete;
  private int[] companyIdsToDelete;

  @AfterEach
  public void tearDown() throws SQLException {
    if (employeeIdsToDelete != null) {
      for (int e : employeeIdsToDelete) {
        employeeRepository.deleteEmployeeByIdDB(e);
      }
      employeeIdsToDelete = null;
    }
    if (companyIdsToDelete != null) {
      for (int c : companyIdsToDelete) {
        companyRepository.deleteCompanyByIdDB(c);
      }
      companyIdsToDelete = null;
    }
  }

  @BeforeAll
  @DisplayName("Авторизация пользователя в роли администратора.Создать компанию")
  public static void authorizeAdmin() {
    AdminUser admin = new AdminUser(PropsHelper.getAdminUsername(), PropsHelper.getAdminPass());

    TOKEN =
        given()
            .log()
            .all()
            .body(admin)
            .contentType(ContentType.JSON)
            .when()
            .post(PropsHelper.getAuthUrl())
            .then()
            .log()
            .all()
            .statusCode(201)
            .extract()
            .path("userToken");
  }

  @Test
  @DisplayName("1.Список компаний без фильтрации по статусу.")
  @Tags({@Tag("positive"), @Tag("required")})
  public void getCompaniesWithoutActiveFilter() throws SQLException {

    Company company3 = companyFactory.getRandomCompany();
    int idCompany1 = createNewCompanyApi(companyFactory.getRandomCompany());
    int idCompany2 = createNewCompanyApi(companyFactory.getRandomCompany());
    int idCompany3 = createNewCompanyApi(company3);

    assertTrue(companyRepository.getCompanyByIdDB(idCompany1).getIsActive());
    assertTrue(companyRepository.getCompanyByIdDB(idCompany2).getIsActive());
    assertTrue(companyRepository.getCompanyByIdDB(idCompany3).getIsActive());

    company3.setIsActive(false);

    given()
        .header("x-client-token", TOKEN)
        .body(company3)
        .contentType(ContentType.JSON)
        .when()
        .patch(PropsHelper.getCompanyUrl() + "/status/" + idCompany3)
        .then()
        .log()
        .all()
        .statusCode(200);

    assertFalse(companyRepository.getCompanyByIdDB(idCompany3).getIsActive());

    List<Company> companies =
        given()
            .when()
            .get(PropsHelper.getCompanyUrl())
            .then()
            .extract()
            .body()
            .jsonPath()
            .getList(".", Company.class);

    assertTrue(companies.contains(companyRepository.getCompanyByIdDB(idCompany1)));
    assertTrue(companies.contains(companyRepository.getCompanyByIdDB(idCompany2)));
    assertTrue(companies.contains(companyRepository.getCompanyByIdDB(idCompany3)));

    companyIdsToDelete = new int[] {idCompany1, idCompany2, idCompany3};
  }

  @Test
  @DisplayName("1a.Фильтрация активных компаний active = true")
  @Tags({@Tag("positive"), @Tag("required")})
  public void filterOnlyActiveCompany() throws SQLException {
    Company company3 = companyFactory.getRandomCompany();
    int idCompany1 = createNewCompanyApi(companyFactory.getRandomCompany());
    int idCompany2 = createNewCompanyApi(companyFactory.getRandomCompany());
    int idCompany3 = createNewCompanyApi(company3);

    assertTrue(companyRepository.getCompanyByIdDB(idCompany1).getIsActive());
    assertTrue(companyRepository.getCompanyByIdDB(idCompany2).getIsActive());
    assertTrue(companyRepository.getCompanyByIdDB(idCompany3).getIsActive());

    company3.setIsActive(false);

    given()
        .header("x-client-token", TOKEN)
        .body(company3)
        .contentType(ContentType.JSON)
        .when()
        .patch(PropsHelper.getCompanyUrl() + "/status/" + idCompany3)
        .then()
        .log()
        .all()
        .statusCode(200);

    assertFalse(companyRepository.getCompanyByIdDB(idCompany3).getIsActive());

    List<Company> companies =
        given()
            .when()
            .get(PropsHelper.getCompanyUrl() + "?active=true")
            .then()
            .extract()
            .body()
            .jsonPath()
            .getList(".", Company.class);

    assertTrue(companies.contains(companyRepository.getCompanyByIdDB(idCompany1)));
    assertTrue(companies.contains(companyRepository.getCompanyByIdDB(idCompany2)));
    assertFalse(companies.contains(companyRepository.getCompanyByIdDB(idCompany3)));

    companyIdsToDelete = new int[] {idCompany1, idCompany2, idCompany3};
  }

  @Test
  @DisplayName("1b.Фильтрация неактивных компаний active = false")
  @Tags({@Tag("positive"), @Tag("required")})
  public void filterOnlyInactiveCompany() throws SQLException {
    Company company3 = companyFactory.getRandomCompany();
    int idCompany1 = createNewCompanyApi(companyFactory.getRandomCompany());
    int idCompany2 = createNewCompanyApi(companyFactory.getRandomCompany());
    int idCompany3 = createNewCompanyApi(company3);

    assertTrue(companyRepository.getCompanyByIdDB(idCompany1).getIsActive());
    assertTrue(companyRepository.getCompanyByIdDB(idCompany2).getIsActive());
    assertTrue(companyRepository.getCompanyByIdDB(idCompany3).getIsActive());

    company3.setIsActive(false);

    given()
        .header("x-client-token", TOKEN)
        .body(company3)
        .contentType(ContentType.JSON)
        .when()
        .patch(PropsHelper.getCompanyUrl() + "/status/" + idCompany3)
        .then()
        .log()
        .all()
        .statusCode(200);

    assertFalse(companyRepository.getCompanyByIdDB(idCompany3).getIsActive());

    List<Company> companies =
        given()
            .when()
            .get(PropsHelper.getCompanyUrl() + "?active=false")
            .then()
            .extract()
            .body()
            .jsonPath()
            .getList(".", Company.class);

    assertFalse(companies.contains(companyRepository.getCompanyByIdDB(idCompany1)));
    assertFalse(companies.contains(companyRepository.getCompanyByIdDB(idCompany2)));
    assertTrue(companies.contains(companyRepository.getCompanyByIdDB(idCompany3)));

    companyIdsToDelete = new int[] {idCompany1, idCompany2, idCompany3};
  }

  @Test
  @DisplayName("2.Проверить создание сотрудника в несуществующей компании")
  @Tags({@Tag("negative"), @Tag("required")})
  public void createEmployeeOfNotExistedCompany() throws SQLException {
    Employee employee = employeeFactory.getRandomEmployee(1);
    given()
        .log()
        .all()
        .header("x-client-token", TOKEN)
        .body(employee)
        .contentType(ContentType.JSON)
        .when()
        .post(PropsHelper.getEmployeeUrl())
        .then()
        .log()
        .all()
        .statusCode(500);

    assertNull(employeeRepository.getEmployeeByIdDB(employee.getId()));
    employeeIdsToDelete = new int[] {employee.getId()};
  }

  @Test
  @DisplayName("3.Проверить, что неактивный сотрудник не отображается в списке")
  @Tags({@Tag("positive"), @Tag("required")})
  public void checkInvisibleInactiveEmployee() throws SQLException {
    int idCompany = createNewCompanyApi(companyFactory.getRandomCompany());
    Employee employee = employeeFactory.getRandomEmployee(idCompany);
    int idEmployee = createNewEmployeeApi(employee);

    List<Employee> employees =
        given()
            .when()
            .get(PropsHelper.getEmployeeUrl() + "?company=" + idCompany)
            .then()
            .extract()
            .body()
            .jsonPath()
            .getList(".", Employee.class);

    employee.setActive(false);

    given()
        .header("x-client-token", TOKEN)
        .body(employee)
        .contentType(ContentType.JSON)
        .when()
        .patch(PropsHelper.getEmployeeUrl() + "/" + idEmployee)
        .then()
        .log()
        .all()
        .statusCode(200);

    List<Employee> employeesAfter =
        given()
            .when()
            .get(PropsHelper.getEmployeeUrl() + "?company=" + idCompany)
            .then()
            .extract()
            .body()
            .jsonPath()
            .getList(".", Employee.class);

    assertFalse(employeeRepository.getEmployeeByIdDB(idEmployee).getIsActive());
    assertFalse(employeesAfter.contains(employeeRepository.getEmployeeByIdDB(idEmployee)));
  }

  @Test
  @DisplayName("4.Проверить, что у удаленной компании проставляется в БД поле deletedAt")
  @Tags({@Tag("positive"), @Tag("required")})
  public void softDeleteCompany() throws SQLException {
    int idCompany = createNewCompanyApi(companyFactory.getRandomCompany());

    assertNull(companyRepository.getCompanyByIdDB(idCompany).getDeletedAt());

    given()
        .log()
        .all()
        .header("x-client-token", TOKEN)
        .get(PropsHelper.getCompanyUrl() + "/delete/" + idCompany)
        .then()
        .statusCode(200);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate parsedCurrentDate = LocalDate.parse(LocalDate.now().format(formatter), formatter);
    String deletedAtDate = companyRepository.getCompanyByIdDB(idCompany).getDeletedAt();
    assertEquals(parsedCurrentDate.toString(), deletedAtDate.substring(0, 10));
    companyIdsToDelete = new int[] {idCompany};
  }

  @Test
  @DisplayName("5.Добавить нового сотрудника в существующую компанию")
  public void createNewEmployee() throws SQLException {

    int idCompany = createNewCompanyApi(companyFactory.getRandomCompany());
    Employee employeeAPI = employeeFactory.getRandomEmployee(idCompany);
    int idEmployee = createNewEmployeeApi(employeeAPI);
    employeeIdsToDelete = new int[] {idEmployee};
    companyIdsToDelete = new int[] {idCompany};

    Employee employeeDb = employeeRepository.getEmployeeByIdDB(idEmployee);

    assertEquals(idEmployee, employeeDb.getId());
    assertEquals(employeeAPI.getFirstName(), employeeDb.getFirstName());
    assertTrue(employeeDb.getIsActive());
  }

  @Test
  @DisplayName(
      "6.Добавить несколько сотрудников в существующую компанию.Получить список сотрудников")
  public void createSomeNewEmployee() throws SQLException {

    int idCompany = createNewCompanyApi(companyFactory.getRandomCompany());
    int idEmployee1 = createNewEmployeeApi(employeeFactory.getRandomEmployee(idCompany));
    int idEmployee2 = createNewEmployeeApi(employeeFactory.getRandomEmployee(idCompany));
    int idEmployee3 = createNewEmployeeApi(employeeFactory.getRandomEmployee(idCompany));

    given()
        .get(PropsHelper.getEmployeeUrl() + "?company=" + idCompany)
        .then()
        .log()
        .all()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("id", hasItems(idEmployee1, idEmployee2, idEmployee3));

    Employee employeeDb1 = employeeRepository.getEmployeeByIdDB(idEmployee1);
    Employee employeeDb2 = employeeRepository.getEmployeeByIdDB(idEmployee2);
    Employee employeeDb3 = employeeRepository.getEmployeeByIdDB(idEmployee3);
    assertEquals(idEmployee1, employeeDb1.getId());
    assertEquals(idEmployee2, employeeDb2.getId());
    assertEquals(idEmployee3, employeeDb3.getId());

    employeeIdsToDelete = new int[] {idEmployee1, idEmployee2, idEmployee3};
    companyIdsToDelete = new int[] {idCompany};
  }

  @Test
  @DisplayName("7.Получить информацию о сотруднике по id")
  public void getEmployeeById() throws SQLException {

    int idCompany = createNewCompanyApi(companyFactory.getRandomCompany());
    Employee employee = employeeFactory.getRandomEmployee(idCompany);
    int newEmployeeId_DB = employeeRepository.createEmployeeDB(employee);

    var employeeResponse =
        given().get(PropsHelper.getEmployeeUrl() + "/" + newEmployeeId_DB).then();

    assertEquals(employee.getFirstName(), employeeResponse.extract().path("firstName"));
    assertEquals(newEmployeeId_DB, (int) employeeResponse.extract().path("id"));

    employeeIdsToDelete = new int[] {newEmployeeId_DB};
    companyIdsToDelete = new int[] {idCompany};
  }

  @Test
  @DisplayName("8.Изменить информацию о сотруднике по id")
  public void updateEmployeeById() throws SQLException {

    int idCompany = createNewCompanyApi(companyFactory.getRandomCompany());
    Employee employeeAPI = employeeFactory.getRandomEmployee(idCompany);
    int idEmployee = createNewEmployeeApi(employeeAPI);

    Employee updatingEmployee = new Employee("Honor", "ninja01@mail.ru", "url", "777-777", false);

    given()
        .header("x-client-token", TOKEN)
        .body(updatingEmployee)
        .contentType(ContentType.JSON)
        .when()
        .patch(PropsHelper.getEmployeeUrl() + "/" + idEmployee)
        .then()
        .log()
        .all()
        .statusCode(200)
        .extract()
        .body()
        .as(Employee.class);

    Employee employeeDb = employeeRepository.getEmployeeByIdDB(idEmployee);
    assertEquals(idEmployee, employeeDb.getId());
    assertEquals(updatingEmployee.getLastName(), employeeDb.getLastName());
    assertEquals(updatingEmployee.getEmail(), employeeDb.getEmail());
    assertFalse(employeeDb.getIsActive());

    employeeIdsToDelete = new int[] {idEmployee};
    companyIdsToDelete = new int[] {idCompany};
  }

  @Test
  @DisplayName("9.Добавить сотрудника в сущ.компанию без авторизации")
  @Tag("negative")
  public void addEmployeeWithoutAuth() {

    int idCompany = createNewCompanyApi(companyFactory.getRandomCompany());
    Employee employeeAPI = employeeFactory.getRandomEmployee(idCompany);

    given()
        .log()
        .all()
        .body(employeeAPI)
        .contentType(ContentType.JSON)
        .when()
        .post(PropsHelper.getEmployeeUrl())
        .then()
        .log()
        .all()
        .statusCode(401);

    companyIdsToDelete = new int[] {idCompany};
  }

  @Test
  @DisplayName("10.Изменить сотрудника без авторизации")
  @Tag("negative")
  public void updateEmployeeWithoutAuth() {
    int idCompany = createNewCompanyApi(companyFactory.getRandomCompany());
    int idEmployee = createNewEmployeeApi(employeeFactory.getRandomEmployee(idCompany));

    Employee updatingEmployee = new Employee("Honor", "ninja01@mail.ru", "url", "777-777", false);

    given()
        .body(updatingEmployee)
        .contentType(ContentType.JSON)
        .when()
        .patch(PropsHelper.getEmployeeUrl() + "/" + idEmployee)
        .then()
        .statusCode(401);

    employeeIdsToDelete = new int[] {idEmployee};
    companyIdsToDelete = new int[] {idCompany};
  }

  @Test
  @DisplayName("11.Получить список сотрудников несуществующей компании")
  @Tag("negative")
  public void getListEmployeeInvalidCompany() {
    given()
        .get(PropsHelper.getEmployeeUrl() + "?company=" + Integer.MAX_VALUE)
        .then()
        .log()
        .all()
        .statusCode(200)
        .body("results", hasSize(0));
  }

  @Test
  @DisplayName("12.Получить инфо о несущ.сотруднике")
  @Tag("negative")
  public void getEmployeeInfoByInvalidId() {
    given()
        .get(PropsHelper.getEmployeeUrl() + "/" + Integer.MAX_VALUE)
        .then()
        .statusCode(200)
        .header("Content-Length", "0");
  }

  @Test
  @DisplayName("13.Добавить нового сотрудника без компании")
  @Tag("negative")
  public void createNewEmployeeWithoutCompany() {
    Employee newEmployee = new Employee(0);

    given()
        .log()
        .all()
        .header("x-client-token", TOKEN)
        .body(newEmployee)
        .contentType(ContentType.JSON)
        .when()
        .post(PropsHelper.getEmployeeUrl())
        .then()
        .log()
        .all()
        .statusCode(500);
  }

  @Test
  @DisplayName(
      "14.Добавить сотрудника в существующую компанию. Затем удалить компанию,запросить инфо сотрудника")
  @Tag("negative")
  public void getEmployeeDeletedCompany() throws SQLException {

    int idCompany = createNewCompanyApi(companyFactory.getRandomCompany());
    int idEmployee = createNewEmployeeApi(employeeFactory.getRandomEmployee(idCompany));

    given()
        .log()
        .all()
        .header("x-client-token", TOKEN)
        .get(PropsHelper.getCompanyUrl() + "/delete/" + idCompany)
        .then()
        .statusCode(200);

    Employee employeeWithoutCompany =
        given()
            .get(PropsHelper.getEmployeeUrl() + "/" + idEmployee)
            .then()
            .log()
            .all()
            .statusCode(200)
            .extract()
            .body()
            .as(Employee.class);

    assertTrue(employeeWithoutCompany.getIsActive());
    employeeIdsToDelete = new int[] {idEmployee};
    companyIdsToDelete = new int[] {idCompany};
  }

  private int createNewCompanyApi(Company company) {
    return given()
        .header("x-client-token", TOKEN)
        .body(company)
        .contentType(ContentType.JSON)
        .when()
        .post(PropsHelper.getCompanyUrl())
        .then()
        .statusCode(201)
        .body("id", greaterThan(0))
        .extract()
        .path("id");
  }

  private int createNewEmployeeApi(Employee employee) {
    return given()
        .log()
        .all()
        .header("x-client-token", TOKEN)
        .body(employee)
        .contentType(ContentType.JSON)
        .when()
        .post(PropsHelper.getEmployeeUrl())
        .then()
        .log()
        .all()
        .statusCode(201)
        .body("id", greaterThan(0))
        .extract()
        .path("id");
  }
}
