package com.example.recipe.services.user_access;

import com.example.recipe.domain.Category;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.repositories.interface_user_access.IUserCategoryRepository;
import com.example.recipe.repositories.impl.CategoryRepositoryImpl;

import java.util.ArrayList;

public class UserCategoryService {
    private final IUserCategoryRepository categoryRepository;

    public UserCategoryService() {
        categoryRepository = new CategoryRepositoryImpl();
    }

    public DbResponse<ArrayList<Category>> getAllCategories(){
        return categoryRepository.getAllCategories();
    }
    public DbResponse<Category> getCategoryById(long categoryId){
        return categoryRepository.getCategoryById(categoryId);
    }
}
