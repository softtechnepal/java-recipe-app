package com.example.recipe.services.user;

import com.example.recipe.domain.recipe.Category;
import com.example.recipe.domain.common.DatabaseCallback;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.repositories.iuser.IUserCategoryRepository;
import com.example.recipe.repositories.impl.CategoryRepositoryImpl;
import com.example.recipe.utils.LoggerUtil;

import java.util.ArrayList;
import java.util.List;

public class UserCategoryService {
    private final IUserCategoryRepository categoryRepository;

    public UserCategoryService() {
        categoryRepository = new CategoryRepositoryImpl();
    }

    private final ArrayList<Category> categories = new ArrayList<>();

    public void getAllCategories(DatabaseCallback<ArrayList<Category>> callback) {
        LoggerUtil.logger.error("Error while retrieving data {}", categories.size());
        if (!categories.isEmpty()) {
            callback.onDbResponse(DbResponse.success("", categories));
            return;
        }
        categoryRepository.getAllCategories(callback);
    }

    public void setCategories(List<Category> categories) {
        if (this.categories.isEmpty()) {
            this.categories.addAll(categories);
        }
    }

    public DbResponse<Category> getCategoryById(long categoryId) {
        return categoryRepository.getCategoryById(categoryId);
    }
}
