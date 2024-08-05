package com.example.recipe.repositories.iadmin;

import com.example.recipe.domain.User;
import com.example.recipe.domain.common.DbResponse;

import java.util.ArrayList;

public interface IAdminUserRepository {
    DbResponse<ArrayList<User>> getAllUsers();
    DbResponse<User> getUserById(long userId);
    DbResponse<User> toggleUserStatus(long userId, String status);
}
