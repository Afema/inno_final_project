package ru.inno.course.finalProject;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.inno.course.finalProject.CompanyService.CompanyRepository;
import ru.inno.course.finalProject.CompanyService.CompanyRepositoryJDBC;
import ru.inno.course.finalProject.EmployeeService.EmployeeRepository;
import ru.inno.course.finalProject.EmployeeService.EmployeeRepositoryJDBC;
import ru.inno.course.finalProject.companyFactory.CompanyFactory;
import ru.inno.course.finalProject.employeeFactory.EmployeeFactory;
import ru.inno.course.finalProject.helpers.PropsHelper;
import ru.inno.course.finalProject.model.AdminUser;
import ru.inno.course.finalProject.model.Company;
import ru.inno.course.finalProject.model.Employee;
import ru.inno.course.finalProject.notificator.TestResultNotificator;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@ExtendWith(TestResultNotificator.class)
public class XClientContractTests {

    private static String TOKEN;
    private CompanyFactory companyFactory = new CompanyFactory();
    private EmployeeFactory employeeFactory = new EmployeeFactory();
    private EmployeeRepository employeeRepository = new EmployeeRepositoryJDBC();
    private CompanyRepository companyRepository = new CompanyRepositoryJDBC();
    private int[] employeeIdsToDelete;
    private int[] companyIdsToDelete;

    @BeforeAll
    @DisplayName("Авторизация пользователя в роли администратора")
    public static void authorizeAdmin() {
        AdminUser admin = new AdminUser(PropsHelper.getAdminUsername(), PropsHelper.getAdminPass());

        TOKEN = given().log().all()
                .body(admin)
                .contentType(ContentType.JSON)
                .when().post(PropsHelper.getAuthUrl())
                .then().log().all()
                .statusCode(201)
                .extract().path("userToken");
    }

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

    @Test
    @DisplayName("Проверить что создание компании доступно")
    public void createNewCompany() {
        Company company = companyFactory.getRandomCompany();

        int idCompany = given()
                .header("x-client-token", TOKEN)
                .body(company)
                .contentType(ContentType.JSON)
                .when().post(PropsHelper.getCompanyUrl())
                .then()
                .statusCode(201)
                .body("id", greaterThan(0))
                .extract().path("id");

        companyIdsToDelete = new int[]{idCompany};
    }

    @Test
    @DisplayName("Добавить нового сотрудника в компанию")
    public void createNewEmployee() {
        Company company = companyFactory.getRandomCompany();

        int idCompany = given()
                .header("x-client-token", TOKEN)
                .body(company)
                .contentType(ContentType.JSON)
                .when().post(PropsHelper.getCompanyUrl())
                .then()
                .statusCode(201)
                .body("id", greaterThan(0))
                .extract().path("id");

        Employee employee = employeeFactory.getRandomEmployee(idCompany);

        int idEmployee = given().log().all()
                .header("x-client-token", TOKEN)
                .body(employee)
                .contentType(ContentType.JSON)
                .when().post(PropsHelper.getEmployeeUrl())
                .then().log().all()
                .statusCode(201)
                .body("id", greaterThan(0))
                .extract().path("id");

        employeeIdsToDelete = new int[]{idEmployee};
        companyIdsToDelete = new int[]{idCompany};
    }

    @Test
    @DisplayName("Получить список сотрудников")
    public void getListEmployee() {
        Company company = companyFactory.getRandomCompany();

        int idCompany = given()
                .header("x-client-token", TOKEN)
                .body(company)
                .contentType(ContentType.JSON)
                .when().post(PropsHelper.getCompanyUrl())
                .then()
                .statusCode(201)
                .body("id", greaterThan(0))
                .extract().path("id");

        Employee employee = employeeFactory.getRandomEmployee(idCompany);

        int idEmployee = given().log().all()
                .header("x-client-token", TOKEN)
                .body(employee)
                .contentType(ContentType.JSON)
                .when().post(PropsHelper.getEmployeeUrl())
                .then().log().all()
                .statusCode(201)
                .body("id", greaterThan(0))
                .extract().path("id");


        given()
                .get(PropsHelper.getEmployeeUrl() + "?company=" + idCompany)
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.JSON);

        employeeIdsToDelete = new int[]{idEmployee};
        companyIdsToDelete = new int[]{idCompany};

    }


    @Test
    @DisplayName("Получить сотрудника по id")
    public void getEmployeeById() {
        Company company = companyFactory.getRandomCompany();

        int idCompany = given()
                .header("x-client-token", TOKEN)
                .body(company)
                .contentType(ContentType.JSON)
                .when().post(PropsHelper.getCompanyUrl())
                .then()
                .statusCode(201)
                .body("id", greaterThan(0))
                .extract().path("id");

        Employee employee = employeeFactory.getRandomEmployee(idCompany);


        int idEmployee = given().log().all()
                .header("x-client-token", TOKEN)
                .body(employee)
                .contentType(ContentType.JSON)
                .when().post(PropsHelper.getEmployeeUrl())
                .then().log().all()
                .statusCode(201)
                .body("id", greaterThan(0))
                .extract().path("id");


        given()
                .get(PropsHelper.getEmployeeUrl() + "/" + idEmployee)
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(idEmployee));

        employeeIdsToDelete = new int[]{idEmployee};
        companyIdsToDelete = new int[]{idCompany};
    }

    @Test
    @DisplayName("Изменить информацию о сотруднике по id")
    public void updateEmployeeById() {
        Company company = companyFactory.getRandomCompany();

        int idCompany = given()
                .header("x-client-token", TOKEN)
                .body(company)
                .contentType(ContentType.JSON)
                .when().post(PropsHelper.getCompanyUrl())
                .then()
                .statusCode(201)
                .body("id", greaterThan(0))
                .extract().path("id");

        Employee employee = employeeFactory.getRandomEmployee(idCompany);

        int idEmployee = given().log().all()
                .header("x-client-token", TOKEN)
                .body(employee)
                .contentType(ContentType.JSON)
                .when().post(PropsHelper.getEmployeeUrl())
                .then().log().all()
                .statusCode(201)
                .body("id", greaterThan(0))
                .extract().path("id");

        String updatingEmployee = "{\"lastName\": \"Honor\"," +
                "\"email\": \"ninja01@mail.ru\"," +
                " \"url\": \"text\"," +
                " \"phone\": \"777-777\"," +
                "  \"isActive\": false }";

        given()
                .header("x-client-token", TOKEN)
                .body(updatingEmployee)
                .contentType(ContentType.JSON)
                .when().patch(PropsHelper.getEmployeeUrl() + "/" + idEmployee)
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("email", equalTo("ninja01@mail.ru"));

        employeeIdsToDelete = new int[]{idEmployee};
        companyIdsToDelete = new int[]{idCompany};
    }
}
