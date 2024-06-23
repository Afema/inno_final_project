package ru.inno.course.finalProject.employeeFactory;

import com.github.javafaker.Faker;
import ru.inno.course.finalProject.model.Employee;

public class EmployeeFactory {
    private Faker faker;

    public EmployeeFactory() {
        this.faker = new Faker();
    }

    public Employee getRandomEmployee(int companyId) {
        Employee employee = new Employee();
        employee.setFirstName(faker.name().firstName());
        employee.setMiddleName(faker.name().username());
        employee.setLastName(faker.name().lastName());
        employee.setCompanyId(companyId);
        employee.setEmail(faker.internet().emailAddress());
        employee.setPhone(faker.phoneNumber().cellPhone());
        employee.setBirthdate(faker.date().birthday().toString());
        return employee;
    }
}