package com.example.recipe.services.user;

import com.example.recipe.domain.recipe.Recipe;
import com.example.recipe.domain.common.DatabaseCallback;
import com.example.recipe.repositories.impl.RecipeRepoImpl;
import com.example.recipe.repositories.iuser.UserRecipeRepository;
import com.example.recipe.services.UserDetailStore;

import java.util.List;

public class UserRecipeService {
    private final UserRecipeRepository recipeRepository;

    public UserRecipeService() {
        recipeRepository = new RecipeRepoImpl();
    }

    public void addRecipe(Recipe recipe, DatabaseCallback<Recipe> callback) {
        recipeRepository.addRecipe(recipe, callback);
    }

    public void getRecipeByUserId(long userId, DatabaseCallback<List<Recipe>> callback) {
        recipeRepository.getRecipeByUserId(userId, callback);
    }

    public void getAllRecipes(DatabaseCallback<List<Recipe>> callback) {
        recipeRepository.getAllRecipes(callback);
    }

    public void updateRecipe(Recipe recipe, DatabaseCallback<Recipe> callback) {
        recipeRepository.updateRecipe(recipe, callback);
    }

    public void addToFavourite(long recipeId, DatabaseCallback<Boolean> callback) {
        recipeRepository.addRecipeFavorite(recipeId, UserDetailStore.getInstance().getUserId(), callback);
    }
}
