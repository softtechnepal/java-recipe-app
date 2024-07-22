package com.example.recipe.repositories.impl;

import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.domain.request.LoginRequest;
import com.example.recipe.domain.response.LoginResponse;
import com.example.recipe.repositories.AuthRepository;

public class AuthRepositoryImpl implements AuthRepository {

    @Override
    public DbResponse<LoginResponse> login(LoginRequest loginRequest) {
        if (loginRequest.getUsername().equals("admin") && loginRequest.getPassword().equals("admin")) {
            return new DbResponse.Success<>("Success", null);
        } else {
            return new DbResponse.Failure<>("Failure");
        }
    }
}
