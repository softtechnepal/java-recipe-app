package com.example.recipe.repositories.iuser;

import com.example.recipe.domain.recipe.Recipe;
import com.example.recipe.domain.common.DatabaseCallback;

import java.util.List;

public interface UserRecipeRepository {
    void addRecipe(Recipe recipe, DatabaseCallback<Recipe> callback);

    void getRecipeById(long recipeId, DatabaseCallback<Recipe> callback);

    void getRecipeByUserId(long userId, DatabaseCallback<List<Recipe>> callback);

    void getAllRecipes(DatabaseCallback<List<Recipe>> callback);

    void updateRecipe(Recipe recipe, DatabaseCallback<Recipe> callback);

    void deleteRecipe(long recipeId, DatabaseCallback<Recipe> callback);
}


