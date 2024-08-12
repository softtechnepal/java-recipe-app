package com.example.recipe.repositories.iuser;

import com.example.recipe.domain.User;
import com.example.recipe.domain.common.DatabaseCallback;
import com.example.recipe.domain.common.DbResponse;

public interface UserRepository {
    void updateProfile(User updateRequest, DatabaseCallback<User> callback);

    DbResponse<User> getUserById(long userId);
}
