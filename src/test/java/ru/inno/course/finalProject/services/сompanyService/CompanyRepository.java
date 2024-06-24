package ru.inno.course.finalProject.services.сompanyService;

import java.sql.SQLException;
import ru.inno.course.finalProject.model.Company;

public interface CompanyRepository {
  Company getCompanyByIdDB(int id) throws SQLException;

  void deleteCompanyByIdDB(int id) throws SQLException;
}
