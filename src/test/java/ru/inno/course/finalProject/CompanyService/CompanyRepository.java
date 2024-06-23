package ru.inno.course.finalProject.CompanyService;

import ru.inno.course.finalProject.model.Company;

import java.sql.SQLException;

public interface CompanyRepository {
    Company getCompanyByIdDB(int id) throws SQLException;

    void deleteCompanyByIdDB(int id) throws SQLException;
}
