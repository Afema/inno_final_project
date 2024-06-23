package ru.inno.course.finalProject.model;

import java.util.Objects;

public class Employee {
  private int id;
  private String firstName;
  private String middleName;
  private String lastName;
  private String url;
  private int companyId;
  private String email;
  private String phone;
  private String birthdate;
  private boolean isActive = true;
  private String lastChangedDateTime;
  private String avatar_url;

  public Employee() {}

  public Employee(int companyId) {
    this.companyId = companyId;
  }

  public Employee(
      String firstName,
      String middleName,
      String lastName,
      int companyId,
      String email,
      String phone,
      boolean isActive) {
    this.firstName = firstName;
    this.middleName = middleName;
    this.lastName = lastName;
    this.companyId = companyId;
    this.email = email;
    this.phone = phone;
    this.isActive = isActive;
  }

  public Employee(String lastName, String email, String url, String phone, boolean isActive) {
    this.lastName = lastName;
    this.url = url;
    this.email = email;
    this.phone = phone;
    this.isActive = isActive;
  }

  public String getCreateDateTime() {
    return createDateTime;
  }

  public void setCreateDateTime(String createDateTime) {
    this.createDateTime = createDateTime;
  }

  private String createDateTime;

  public String getLastChangedDateTime() {
    return lastChangedDateTime;
  }

  public void setLastChangedDateTime(String lastChangedDateTime) {
    this.lastChangedDateTime = lastChangedDateTime;
  }

  public String getAvatar_url() {
    return avatar_url;
  }

  public void setAvatar_url(String avatar_url) {
    this.avatar_url = avatar_url;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public void setBirthdate(String birthdate) {
    this.birthdate = birthdate;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public int getCompanyId() {
    return companyId;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone() {
    return phone;
  }

  public String getBirthdate() {
    return birthdate;
  }

  public boolean getIsActive() {
    return isActive;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setCompanyId(int companyId) {
    this.companyId = companyId;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Employee employee)) return false;
    return id == employee.id
        && companyId == employee.companyId
        && isActive == employee.isActive
        && Objects.equals(firstName, employee.firstName)
        && Objects.equals(middleName, employee.middleName)
        && Objects.equals(lastName, employee.lastName)
        && Objects.equals(email, employee.email)
        && Objects.equals(phone, employee.phone)
        && Objects.equals(birthdate, employee.birthdate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id, firstName, middleName, lastName, companyId, email, phone, birthdate, isActive);
  }
}
