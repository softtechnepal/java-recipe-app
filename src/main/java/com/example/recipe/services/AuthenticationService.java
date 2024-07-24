package com.example.recipe.services;

import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.domain.request.LoginRequest;
import com.example.recipe.domain.request.UserRequest;
import com.example.recipe.domain.response.LoginResponse;
import com.example.recipe.repositories.AuthRepository;
import com.example.recipe.repositories.impl.AuthRepositoryImpl;

public class AuthenticationService {
    private final AuthRepository authRepository;

    public AuthenticationService() {
        authRepository = new AuthRepositoryImpl();
    }

    public DbResponse<LoginResponse> authenticate(LoginRequest request) {
        return authRepository.login(request);
    }

    public DbResponse<String> register(UserRequest register) {
        return authRepository.register(register);
    }

    public DbResponse<String> forgotPassword(String email) {
        return authRepository.forgotPassword(email);
    }

}
