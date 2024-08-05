package com.example.recipe.services.admin;

import com.example.recipe.domain.User;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.repositories.impl.UserRepositoryImpl;
import com.example.recipe.repositories.interface_admin_access.IAdminUserRepository;

import java.util.ArrayList;

public class AdminUserService {
    private final IAdminUserRepository userRepository;

    public AdminUserService() {
        this.userRepository = new UserRepositoryImpl();
    }

    public DbResponse<ArrayList<User>> getAllUsers(){
        return userRepository.getAllUsers();
    }

    public DbResponse<User> getUserById(long userId){
        return userRepository.getUserById(userId);
    }

    public DbResponse<User> toggleUserStatus(long userId, String status){
        return userRepository.toggleUserStatus(userId, status);
    }
}
