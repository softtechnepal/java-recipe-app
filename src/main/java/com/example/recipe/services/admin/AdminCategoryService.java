package com.example.recipe.services.admin;

import com.example.recipe.domain.recipe.Category;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.repositories.impl.CategoryRepositoryImpl;
import com.example.recipe.repositories.interface_admin_access.IAdminCategoryRepository;

import java.util.ArrayList;

public class AdminCategoryService {
    private final IAdminCategoryRepository categoryRepository;

    public AdminCategoryService() {
        categoryRepository = new CategoryRepositoryImpl();
    }

    public DbResponse<ArrayList<Category>> getAllCategories(){
        return categoryRepository.getAllCategories();
    }
    public DbResponse<Category> getCategoryById(long categoryId){
        return categoryRepository.getCategoryById(categoryId);
    }
    public DbResponse<Category> addCategory(Category category){
        return categoryRepository.addCategory(category);
    }
    public DbResponse<Category> updateCategory(Category category){
        return categoryRepository.updateCategory(category);
    }
    public DbResponse<Category> deleteCategory(long categoryId){
        return categoryRepository.deleteCategory(categoryId);
    }
}
