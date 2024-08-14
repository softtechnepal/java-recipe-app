package com.example.recipe.services.admin;

import com.example.recipe.domain.User;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.domain.recipe.Category;
import com.example.recipe.repositories.impl.UserRepositoryImpl;
import com.example.recipe.repositories.iadmin.IAdminUserRepository;

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

    public DbResponse<ArrayList<User>> getAllUsersByParams(String pararms){
        return userRepository.getAllUsersByParams(pararms);
    }

    public DbResponse<Void> changePassword(String oldPassword, String newPassword){
        return userRepository.changePassword(oldPassword, newPassword);
    }
}
