package ru.inno.course.finalProject.model;

import java.util.Objects;

public class AdminUser {

    private String username;

    private String password;

    public AdminUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdminUser adminUser)) return false;
        return Objects.equals(username, adminUser.username) && Objects.equals(password, adminUser.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
