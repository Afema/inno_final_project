package ru.inno.course.finalProject.model;

import java.util.Objects;

public class Company {
  private int id;
  private boolean isActive;
  private String deletedAt;
  private String name;
  private String description;
  private String createDateTime;
  private String lastChangedDateTime;

  public Company() {}

  public Company(
      int id,
      boolean isActive,
      String createDateTime,
      String lastChangedDateTime,
      String name,
      String description,
      String deletedAt) {
    this.id = id;
    this.isActive = isActive;
    this.createDateTime = createDateTime;
    this.lastChangedDateTime = lastChangedDateTime;
    this.name = name;
    this.description = description;
    this.deletedAt = deletedAt;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(boolean status) {
    isActive = status;
  }

  public String getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(String deletedAt) {
    this.deletedAt = deletedAt;
  }

  public String getLastChangedDateTime() {
    return lastChangedDateTime;
  }

  public void setLastChangedDateTime(String lastChangedDateTime) {
    this.lastChangedDateTime = lastChangedDateTime;
  }

  public String getCreateDateTime() {
    return createDateTime;
  }

  public void setCreateDateTime(String createDateTime) {
    this.createDateTime = createDateTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Company company)) return false;
    return id == company.id
        && isActive == company.isActive
        && Objects.equals(deletedAt, company.deletedAt)
        && Objects.equals(name, company.name)
        && Objects.equals(description, company.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id, isActive, deletedAt, name, description, createDateTime, lastChangedDateTime);
  }

  @Override
  public String toString() {
    return "Company{"
        + "id="
        + id
        + ", isActive="
        + isActive
        + ", deletedAt='"
        + deletedAt
        + '\''
        + ", name='"
        + name
        + '\''
        + ", description='"
        + description
        + '\''
        + ", createDateTime='"
        + createDateTime
        + '\''
        + ", lastChangedDateTime='"
        + lastChangedDateTime
        + '\''
        + '}';
  }
}
