package ru.inno.course.finalProject.companyFactory;

import com.github.javafaker.Faker;
import ru.inno.course.finalProject.model.Company;

public class CompanyFactory {
  private Faker faker;

  public CompanyFactory() {
    this.faker = new Faker();
  }

  public Company getRandomCompany() {
    Company company = new Company();
    company.setName(faker.company().name());
    company.setDescription(faker.company().industry());
    return company;
  }
}
