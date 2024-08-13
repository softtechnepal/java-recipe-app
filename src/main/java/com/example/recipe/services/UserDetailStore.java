package com.example.recipe.services;

public class UserDetailStore {
    private static UserDetailStore instance;
    private String userName;
    private String userEmail;
    private long userId;

    private UserDetailStore() {
        // Private constructor to prevent instantiation
    }

    public static synchronized UserDetailStore getInstance() {
        if (instance == null) {
            instance = new UserDetailStore();
        }
        return instance;
    }

    public void clear() {
        userName = null;
        userEmail = null;
        userId = 0;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}