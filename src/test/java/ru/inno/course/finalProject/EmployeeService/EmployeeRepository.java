package ru.inno.course.finalProject.EmployeeService;


import ru.inno.course.finalProject.model.Employee;

import java.sql.SQLException;

public interface EmployeeRepository {
    int createEmployeeDB(Employee employee) throws SQLException;

    Employee getEmployeeByIdDB(int id) throws SQLException;

    void deleteEmployeeByIdDB(int id) throws SQLException;
}
