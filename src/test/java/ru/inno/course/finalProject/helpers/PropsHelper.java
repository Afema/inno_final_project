package ru.inno.course.finalProject.helpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropsHelper {

  private static final String CONFIG_FILE =
      "src/test/java/ru/inno/course/finalProject/resources/config.properties";

  private static final Properties props = new Properties();

  static {
    try {
      props.load(new FileInputStream(CONFIG_FILE));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getAuthUrl() {
    return props.getProperty("URL_AUTH");
  }

  public static String getCompanyUrl() {
    return props.getProperty("URL_COMPANY");
  }

  public static String getEmployeeUrl() {
    return props.getProperty("URL_EMPLOYEE");
  }

  public static String getDBConnectionString() {
    return props.getProperty("db_connectionString");
  }

  public static String getDBLog() {
    return props.getProperty("db_log");
  }

  public static String getDBPass() {
    return props.getProperty("db_pass");
  }

  public static String getAdminUsername() {
    return props.getProperty("admin_username");
  }

  public static String getAdminPass() {
    return props.getProperty("admin_pass");
  }

  public static String sqlInsertEmployee() {
    return props.getProperty("SQL_INSERT_EMPLOYEE");
  }

  public static String sqlSelectById() {
    return props.getProperty("SQL_SELECT_BY_ID");
  }

  public static String sqlSelectCompanyById() {
    return props.getProperty("SQL_SELECT_COMPANY_BY_ID");
  }

  public static String sqlDeleteById() {
    return props.getProperty("SQL_DELETE_BY_ID");
  }

  public static String sqlDeleteCompanyById() {
    return props.getProperty("SQL_DELETE_COMPANY_BY_ID");
  }

  public static String getTelegramUrl() {
    return props.getProperty("tg_send_message");
  }
}
