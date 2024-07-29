package com.example.recipe.domain;

import java.sql.Date;

public class User {
    private long userId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private boolean isAdmin;
    private String status;
    private Date created_at;

    public User(long userId,String firstName, String lastName, String username, String email, boolean isAdmin, String status,
                Date created_at){
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.isAdmin = isAdmin;
        this.status = status;
        this.created_at = created_at;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getStatus() {
        return status;
    }

    public Date getCreatedAt() {
        return created_at;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setStatus(String email) {
        this.status = status;
    }

    public void setCreatedDate(Date created_at) {
        this.created_at = created_at;
    }
}
