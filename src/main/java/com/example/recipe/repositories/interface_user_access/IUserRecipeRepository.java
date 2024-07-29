package com.example.recipe.repositories.interface_user_access;

import com.example.recipe.domain.Recipe;
import com.example.recipe.domain.common.DbResponse;

import java.util.ArrayList;

public interface IUserRecipeRepository {
    DbResponse<ArrayList<Recipe>> getAllRecipes();
    DbResponse<Recipe> getRecipeById(long recipeId);
    DbResponse<ArrayList<Recipe>> getRecipeByUserId(long userId);
    DbResponse<Recipe> addRecipe(Recipe recipe);
    DbResponse<Recipe> updateRecipe(Recipe recipe);
    DbResponse<Recipe> deleteRecipe(long recipeId);
}


