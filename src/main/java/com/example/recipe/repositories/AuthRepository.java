package com.example.recipe.repositories;

import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.domain.request.LoginRequest;
import com.example.recipe.domain.response.LoginResponse;

public interface AuthRepository {
    DbResponse<LoginResponse> login(LoginRequest request);
}
