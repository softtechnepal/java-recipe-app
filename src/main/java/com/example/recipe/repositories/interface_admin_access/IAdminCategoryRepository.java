package com.example.recipe.repositories.interface_admin_access;

import com.example.recipe.domain.recipe.Category;
import com.example.recipe.domain.common.DbResponse;

import java.util.ArrayList;

public interface IAdminCategoryRepository {
    DbResponse<ArrayList<Category>> getAllCategories();

    DbResponse<Category> getCategoryById(long categoryId);

    DbResponse<Category> addCategory(Category category);

    DbResponse<Category> updateCategory(Category category);

    DbResponse<Category> deleteCategory(long categoryId);
}
