package com.example.recipe.repositories.interface_admin_access;

import com.example.recipe.domain.Recipe;
import com.example.recipe.domain.common.DbResponse;

import java.util.ArrayList;

public interface IAdminRecipeRepository {
    DbResponse<ArrayList<Recipe>> getAllRecipes();
    DbResponse<Recipe> getRecipeById(long recipeId);
    DbResponse<ArrayList<Recipe>> getRecipeByUserId(long userId);
    DbResponse<Recipe> deleteRecipe(long recipeId);
}
