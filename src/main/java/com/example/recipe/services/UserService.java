package com.example.recipe.services;

import com.example.recipe.domain.User;
import com.example.recipe.domain.common.DatabaseCallback;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.repositories.impl.UserRepositoryImpl;
import com.example.recipe.repositories.iuser.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepositoryImpl();
    }

    public void updateProfile(User updateRequest, DatabaseCallback<User> callback) {
        userRepository.updateProfile(updateRequest, callback);
    }

    public DbResponse<User> getUserById(long userId) {
        return userRepository.getUserById(userId);
    }
}
