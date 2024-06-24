package ru.inno.course.finalProject.services.сompanyService;

import java.sql.*;
import ru.inno.course.finalProject.helpers.PropsHelper;
import ru.inno.course.finalProject.model.Company;

public class CompanyRepositoryJDBC implements CompanyRepository {
  @Override
  public Company getCompanyByIdDB(int id) throws SQLException {
    Connection connection =
        DriverManager.getConnection(
            PropsHelper.getDBConnectionString(), PropsHelper.getDBLog(), PropsHelper.getDBPass());
    PreparedStatement statement = connection.prepareStatement(PropsHelper.sqlSelectCompanyById());
    statement.setInt(1, id);
    ResultSet resultSet = statement.executeQuery();
    resultSet.next();
    Company company =
        new Company(
            resultSet.getInt("id"),
            resultSet.getBoolean("is_active"),
            resultSet.getString("create_timestamp"),
            resultSet.getString("change_timestamp"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("deleted_at"));
    company.setId(resultSet.getInt("id"));
    connection.close();
    return company;
  }

  @Override
  public void deleteCompanyByIdDB(int id) throws SQLException {
    Connection connection =
        DriverManager.getConnection(
            PropsHelper.getDBConnectionString(), PropsHelper.getDBLog(), PropsHelper.getDBPass());
    PreparedStatement statement = connection.prepareStatement(PropsHelper.sqlDeleteCompanyById());
    statement.setInt(1, id);
    statement.executeUpdate();
    connection.close();
  }
}
