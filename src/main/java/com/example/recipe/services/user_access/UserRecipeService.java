package com.example.recipe.services.user_access;

import com.example.recipe.domain.Recipe;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.repositories.impl.RecipeRepositoryImpl;
import com.example.recipe.repositories.interface_user_access.IUserRecipeRepository;

import java.util.ArrayList;

public class UserRecipeService {
    private final IUserRecipeRepository recipeRepository;

    public UserRecipeService() {
        recipeRepository = new RecipeRepositoryImpl();
    }

    public DbResponse<ArrayList<Recipe>> getAllRecipes() {
        return recipeRepository.getAllRecipes();
    }

    public DbResponse<Recipe> getRecipeById(long recipeId) {
        return recipeRepository.getRecipeById(recipeId);
    }

    public DbResponse<ArrayList<Recipe>> getRecipesByUserId(long userId) {
        return recipeRepository.getRecipeByUserId(userId);
    }

    public DbResponse<Recipe> addRecipe(Recipe recipe) {
        return recipeRepository.addRecipe(recipe);
    }

    public DbResponse<Recipe> updateRecipe(Recipe recipe) {
        return recipeRepository.updateRecipe(recipe);
    }

    public DbResponse<Recipe> deleteRecipe(long recipeId) {
        return recipeRepository.deleteRecipe(recipeId);
    }
}
