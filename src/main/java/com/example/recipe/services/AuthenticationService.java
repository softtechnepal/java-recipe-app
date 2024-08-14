package com.example.recipe.services;

import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.domain.common.DatabaseCallback;
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

    public void authenticate(LoginRequest request, DatabaseCallback<LoginResponse> result) {
        authRepository.login(request, result);
    }

    public void register(UserRequest register, DatabaseCallback<String> result) {
        authRepository.register(register, result);
    }

    public void validateEmail(String email, DatabaseCallback<String> result) {
        authRepository.validateEmail(email, result);
    }

    public DbResponse<String> forgotPassword(String email) {
        return authRepository.forgotPassword(email);
    }

    public boolean isValidOtp(String otpCode) {
        return otpCode.equals("1234");
    }

    public void updatePassword(String email, String password, DatabaseCallback<String> callback) {
        authRepository.updatePassword(email, password, callback);
    }
}
