package com.example.recipe.services.admin;

import com.example.recipe.domain.Recipe;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.repositories.impl.RecipeRepositoryImpl;
import com.example.recipe.repositories.iadmin.IAdminRecipeRepository;

import java.util.ArrayList;

public class AdminRecipeService {
    private final IAdminRecipeRepository recipeRepository;

    public AdminRecipeService() {
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
    public DbResponse<Recipe> deleteRecipe(long recipeId) {
        return recipeRepository.deleteRecipe(recipeId);
    }

    public DbResponse<ArrayList<String>> getIngredientsByRecipeId(long recipeId) {
        return recipeRepository.getIngredientsByRecipeId(recipeId);
    }

    public DbResponse<ArrayList<String>> getCategoriesByRecipeId(long recipeId) {
        return recipeRepository.getCategoriesByRecipeId(recipeId);
    }

    public DbResponse<Integer> getTotalReviewsByRecipeId(long recipeId) {
        return recipeRepository.getTotalReviewsByRecipeId(recipeId);
    }

    public DbResponse<Integer> getTotalSavedByRecipeId(long recipeId) {
        return recipeRepository.getTotalSavedByRecipeId(recipeId);
    }
    public DbResponse<ArrayList<Recipe>> searchRecipe(String searchTerm) {
        return recipeRepository.searchRecipe(searchTerm);
    }
}
