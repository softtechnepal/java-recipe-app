package com.example.recipe.repositories.iuser;

import com.example.recipe.domain.recipe.Recipe;
import com.example.recipe.domain.common.DatabaseCallback;
import com.example.recipe.domain.recipe.Review;

import java.util.List;

public interface UserRecipeRepository {
    void addRecipe(Recipe recipe, DatabaseCallback<Recipe> callback);

    // Get recipe detail
    void getRecipeDetailById(long recipeId, DatabaseCallback<Recipe> callback);

    // User's recipe list
    void getRecipeByUserId(long userId, DatabaseCallback<List<Recipe>> callback);

    // All recipes
    void getAllRecipes(DatabaseCallback<List<Recipe>> callback);

    void updateRecipe(Recipe recipe, DatabaseCallback<Recipe> callback);

    void deleteRecipe(long recipeId, DatabaseCallback<Boolean> callback);

    void addRecipeFavorite(long recipeId, long userId, DatabaseCallback<Boolean> callback);

    // Get User's Favorite Recipes
    void getFavoriteRecipes(DatabaseCallback<List<Recipe>> callback);

    // Get Recipe Reviews
    void getRecipeReviews(long recipeId, DatabaseCallback<List<Review>> callback);

    void addReview(long recipeId, Review review, DatabaseCallback<Review> callback);

    void deleteReview(Long reviewId, DatabaseCallback<Boolean> callback);
}


