package com.example.recipe.repositories;

import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.domain.common.DatabaseCallback;
import com.example.recipe.domain.request.LoginRequest;
import com.example.recipe.domain.request.UserRequest;
import com.example.recipe.domain.response.LoginResponse;

public interface AuthRepository {
    void login(LoginRequest request, DatabaseCallback<LoginResponse> result);

    void register(UserRequest register, DatabaseCallback<String> result);

    DbResponse<String> forgotPassword(String email);
}
