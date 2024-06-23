package ru.inno.course.finalProject.EmployeeService;

import org.postgresql.util.PSQLException;
import ru.inno.course.finalProject.helpers.PropsHelper;
import ru.inno.course.finalProject.model.Employee;

import java.sql.*;

public class EmployeeRepositoryJDBC implements EmployeeRepository {

    @Override
    public int createEmployeeDB(Employee employee) throws SQLException {
        Connection connection = DriverManager.getConnection(PropsHelper.getDBConnectionString(), PropsHelper.getDBLog(), PropsHelper.getDBPass());
        PreparedStatement statement = connection.prepareStatement(PropsHelper.sqlInsertEmployee(), Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, employee.getFirstName());
        statement.setString(2, employee.getLastName());
        statement.setString(3, employee.getPhone());
        statement.setString(4, employee.getEmail());
        statement.setInt(5, employee.getCompanyId());
        statement.executeUpdate();
        ResultSet keys = statement.getGeneratedKeys();
        keys.next();
        connection.close();
        return keys.getInt("id");
    }

    @Override
    public Employee getEmployeeByIdDB(int id) throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(PropsHelper.getDBConnectionString(), PropsHelper.getDBLog(), PropsHelper.getDBPass());
            PreparedStatement statement = connection.prepareStatement(PropsHelper.sqlSelectById());
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Employee employee = new Employee(resultSet.getString("first_name"),
                    resultSet.getString("middle_name"),
                    resultSet.getString("last_name"),
                    resultSet.getInt("company_id"),
                    resultSet.getString("email"),
                    resultSet.getString("phone"),
                    resultSet.getBoolean("is_active"));
            employee.setId(resultSet.getInt("id"));
            connection.close();
            return employee;
        } catch (PSQLException e) {
            return null;
        }
    }

    @Override
    public void deleteEmployeeByIdDB(int id) throws SQLException {
        Connection connection = DriverManager.getConnection(PropsHelper.getDBConnectionString(), PropsHelper.getDBLog(), PropsHelper.getDBPass());
        PreparedStatement statement = connection.prepareStatement(PropsHelper.sqlDeleteById());
        statement.setInt(1, id);
        statement.executeUpdate();
        connection.close();
    }
}