package com.example.recipe.repositories.interface_user_access;

import com.example.recipe.domain.common.DatabaseCallback;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.domain.recipe.Category;

import java.util.ArrayList;

public interface IUserCategoryRepository {
    DbResponse<ArrayList<Category>> getAllCategories();

    void getAllCategories(DatabaseCallback<ArrayList<Category>> callback);

    DbResponse<Category> getCategoryById(long categoryId);
}
