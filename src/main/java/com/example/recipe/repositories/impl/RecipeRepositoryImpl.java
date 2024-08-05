package com.example.recipe.repositories.impl;

import com.example.recipe.config.DatabaseConfig;
import com.example.recipe.domain.Recipe;
import com.example.recipe.domain.common.DatabaseCallback;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.repositories.interface_admin_access.IAdminRecipeRepository;
import com.example.recipe.repositories.interface_user_access.IUserRecipeRepository;
import com.example.recipe.utils.DatabaseThread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.example.recipe.utils.LoggerUtil.logger;

public class RecipeRepositoryImpl implements IAdminRecipeRepository, IUserRecipeRepository {

    @Override
    public DbResponse<ArrayList<Recipe>> getAllRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "SELECT * FROM recipes";
            try (PreparedStatement st = conn.prepareStatement(query)) {
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    Recipe recipe = new Recipe(
                            rs.getLong("recipe_id"),
                            rs.getLong("user_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("image"),
                            rs.getString("video_url"),
                            rs.getString("created_at")
                    );
                    recipes.add(recipe);
                }
            }
        } catch (Exception e) {
            logger.error("Error while retrieving data", e);
            return new DbResponse.Failure<>(e.getMessage());
        }
        return new DbResponse.Success<>("Get recipes success", recipes);
    }

    @Override
    public DbResponse<Recipe> getRecipeById(long recipeId) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "SELECT * FROM recipes WHERE recipe_id = ?";
            try (PreparedStatement st = conn.prepareStatement(query)) {
                st.setLong(1, recipeId);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    Recipe recipe = new Recipe(
                            rs.getLong("recipe_id"),
                            rs.getLong("user_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("image"),
                            rs.getString("video_url"),
                            rs.getString("created_at")
                    );
                    return new DbResponse.Success<>("Get recipe success", recipe);
                } else {
                    return new DbResponse.Failure<>("Recipe not found");
                }
            }
        } catch (Exception e) {
            logger.error("Error while retrieving recipe", e);
            return new DbResponse.Failure<>(e.getMessage());
        }
    }

    @Override
    public DbResponse<ArrayList<Recipe>> getRecipeByUserId(long userId) {
        ArrayList<Recipe> recipes = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "SELECT * FROM recipes WHERE user_id = ?";
            try (PreparedStatement st = conn.prepareStatement(query)) {
                st.setLong(1, userId);
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    Recipe recipe = new Recipe(
                            rs.getLong("recipe_id"),
                            rs.getLong("user_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("image"),
                            rs.getString("video_url"),
                            rs.getString("created_at")
                    );
                    recipes.add(recipe);
                }
                return new DbResponse.Success<>("Get recipes by user ID success", recipes);
            }
        } catch (Exception e) {
            logger.error("Error while retrieving recipes by user ID", e);
            return new DbResponse.Failure<>(e.getMessage());
        }
    }

    @Override
    public DbResponse<Recipe> addRecipe(Recipe recipe) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "INSERT INTO recipes (user_id, title, description, image, video_url, created_at) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                st.setLong(1, recipe.getUserId());
                st.setString(2, recipe.getTitle());
                st.setString(3, recipe.getDescription());
                st.setString(4, recipe.getImage());
                st.setString(5, recipe.getVideoUrl());
                st.setString(6, recipe.getCreatedAt());
                int affectedRows = st.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            long newRecipeId = generatedKeys.getLong(1);
                            recipe.setRecipeId(newRecipeId);
                            return new DbResponse.Success<>("Recipe added successfully", recipe);
                        } else {
                            return new DbResponse.Failure<>("Failed to retrieve recipe ID");
                        }
                    }
                } else {
                    return new DbResponse.Failure<>("Failed to add recipe");
                }
            }
        } catch (Exception e) {
            logger.error("Error while adding recipe", e);
            return new DbResponse.Failure<>(e.getMessage());
        }
    }

    @Override
    public void addRecipe(Recipe recipe, DatabaseCallback<Recipe> callback) {
        DatabaseThread.runDataOperation(() -> addRecipe(recipe), callback);
    }

    @Override
    public DbResponse<Recipe> updateRecipe(Recipe recipe) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "UPDATE recipes SET user_id = ?, title = ?, description = ?, image = ?, video_url = ?, created_at = ? WHERE recipe_id = ?";
            try (PreparedStatement st = conn.prepareStatement(query)) {
                st.setLong(1, recipe.getUserId());
                st.setString(2, recipe.getTitle());
                st.setString(3, recipe.getDescription());
                st.setString(4, recipe.getImage());
                st.setString(5, recipe.getVideoUrl());
                st.setString(6, recipe.getCreatedAt());
                st.setLong(7, recipe.getRecipeId());
                int affectedRows = st.executeUpdate();
                if (affectedRows > 0) {
                    return new DbResponse.Success<>("Recipe updated successfully", recipe);
                } else {
                    return new DbResponse.Failure<>("Failed to update recipe");
                }
            }
        } catch (Exception e) {
            logger.error("Error while updating recipe", e);
            return new DbResponse.Failure<>(e.getMessage());
        }
    }

    @Override
    public DbResponse<Recipe> deleteRecipe(long recipeId) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "DELETE FROM recipes WHERE recipe_id = ?";
            try (PreparedStatement st = conn.prepareStatement(query)) {
                st.setLong(1, recipeId);
                int rowsAffected = st.executeUpdate();
                if (rowsAffected == 0) {
                    return new DbResponse.Failure<>("Recipe not found");
                }
            }
        } catch (Exception e) {
            logger.error("Error while deleting recipe", e);
            return new DbResponse.Failure<>(e.getMessage());
        }
        return new DbResponse.Success<>("Delete recipe success", null);
    }
}
