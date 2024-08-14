package com.example.recipe.repositories.iadmin;

import com.example.recipe.domain.Recipe;
import com.example.recipe.domain.common.DatabaseCallback;
import com.example.recipe.domain.common.DbResponse;

import java.util.ArrayList;
import java.util.List;

public interface IAdminRecipeRepository {
    DbResponse<ArrayList<Recipe>> getAllRecipes();
    DbResponse<Recipe> getRecipeById(long recipeId);
    DbResponse<ArrayList<Recipe>> getRecipeByUserId(long userId);
    DbResponse<Recipe> deleteRecipe(long recipeId);
    void searchRecipe(String searchTerm, DatabaseCallback<List<Recipe>> response);
}
