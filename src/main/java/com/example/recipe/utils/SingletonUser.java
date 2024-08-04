package com.example.recipe.utils;

import com.example.recipe.domain.response.LoginResponse;

public class SingletonUser {
    private LoginResponse loginResponse;

    private static class User {
        private static final SingletonUser instance = new SingletonUser();
    }

    public static SingletonUser getInstance() {
        return User.instance;
    }

    public LoginResponse getLoginResponse() {
        return loginResponse;
    }

    public void setLoginResponse(LoginResponse loginResponse) {
        this.loginResponse = loginResponse;
    }
}
