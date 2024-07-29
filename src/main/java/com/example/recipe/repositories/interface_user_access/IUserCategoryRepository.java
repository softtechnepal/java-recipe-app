package com.example.recipe.repositories.interface_user_access;

import com.example.recipe.domain.Category;
import com.example.recipe.domain.common.DbResponse;
import java.util.ArrayList;

public interface IUserCategoryRepository {
    DbResponse<ArrayList<Category>> getAllCategories();
    DbResponse<Category> getCategoryById(long categoryId);
}
