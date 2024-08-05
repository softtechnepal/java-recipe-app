package com.example.recipe.repositories.impl;

import com.example.recipe.config.DatabaseConfig;
import com.example.recipe.domain.common.DatabaseCallback;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.domain.recipe.*;
import com.example.recipe.repositories.iuser.UserRecipeRepository;
import com.example.recipe.utils.DatabaseThread;
import com.example.recipe.utils.ImageUtil;

import java.io.File;
import java.sql.*;
import java.util.List;

import static com.example.recipe.utils.LoggerUtil.logger;

public class RecipeRepoImpl implements UserRecipeRepository {
    @Override
    public void addRecipe(Recipe recipe, DatabaseCallback<Recipe> callback) {
        DatabaseThread.runDataOperation(() -> {
            try (Connection connection = DatabaseConfig.getConnection()) {
                String newImagePath = ImageUtil.copyImageToDbImages(new File(recipe.getImage()));
                recipe.setImage(newImagePath);
                long recipeId = insertRecipe(connection, recipe);
                recipe.setRecipeId(recipeId);
                insertCategories(connection, recipe.getCategory(), recipeId);
                insertIngredients(connection, recipe.getIngredients(), recipeId);
                insertSteps(connection, recipe.getSteps(), recipeId);
                insertNutritionalInformation(connection, recipe.getNutritionalInformation(), recipeId);
                return DbResponse.success("Recipe Added Successfully", recipe);
            } catch (Exception e) {
                logger.error("Error while adding recipe", e);
                return DbResponse.failure(e.getMessage());
            }
        }, callback);
    }

    private Long insertRecipe(Connection connection, Recipe recipe) throws SQLException {
        String insertRecipeQuery = "INSERT INTO recipes " +
                "(user_id, title, description, image, video_url, warnings) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING recipe_id";
        try (PreparedStatement statement = connection.prepareStatement(insertRecipeQuery, Statement.RETURN_GENERATED_KEYS)) {
            // todo(gprachan)
            statement.setInt(1, 107);
            statement.setString(2, recipe.getTitle());
            statement.setString(3, recipe.getDescription());
            statement.setString(4, recipe.getImage());
            statement.setString(5, recipe.getVideoUrl());
            statement.setString(6, recipe.getWarnings());
            statement.execute();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                } else {
                    throw new SQLException("Failed to get recipe ID");
                }
            }
        }
    }

    private void insertCategories(Connection connection, List<Category> categories, Long recipeId) throws SQLException {
        String insertCategoryQuery = "INSERT INTO recipecategories (recipe_id, category_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertCategoryQuery)) {
            for (Category category : categories) {
                statement.setLong(1, recipeId);
                statement.setLong(2, category.getCategoryId());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private void insertIngredients(Connection connection, List<Ingredient> ingredients, Long recipeId) throws SQLException {
        String insertIngredientQuery = "INSERT INTO ingredients " +
                "(recipe_id, ingredient_name, quantity, unit) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertIngredientQuery)) {
            for (Ingredient ingredient : ingredients) {
                statement.setLong(1, recipeId);
                statement.setString(2, ingredient.getIngredientName());
                statement.setDouble(3, ingredient.getQuantity() != null ? ingredient.getQuantity() : 0);
                statement.setString(4, ingredient.getUnit() != null ? ingredient.getUnit() : "");
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private void insertSteps(Connection connection, List<Steps> steps, Long recipeId) throws SQLException {
        String insertStepQuery = "INSERT INTO recipesteps " +
                "(recipe_id, step_order, step_name, step_description) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertStepQuery)) {
            for (int i = 0; i < steps.size(); i++) {
                Steps step = steps.get(i);
                statement.setLong(1, recipeId);
                statement.setInt(2, i + 1);
                statement.setString(3, step.getStepName());
                statement.setString(4, step.getStepDescription());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private void insertNutritionalInformation(Connection connection, NutritionalInformation recipe, Long recipeId) throws SQLException {
        String insertNutritionalInfoQuery = "INSERT INTO nutritionalinformation " +
                "(recipe_id, calories, protein, fat, carbohydrates, other) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertNutritionalInfoQuery)) {
            statement.setLong(1, recipeId);

            statement.setDouble(2, recipe.getCalories() != null ? recipe.getCalories() : 0);
            statement.setDouble(3, recipe.getProtein() != null ? recipe.getProtein() : 0);
            statement.setDouble(4, recipe.getFat() != null ? recipe.getFat() : 0);
            statement.setDouble(5, recipe.getCarbohydrates() != null ? recipe.getCarbohydrates() : 0);
            statement.setString(6, recipe.getOther() != null ? recipe.getOther() : "");
            statement.execute();
        }
    }

    @Override
    public void getRecipeById(long recipeId, DatabaseCallback<Recipe> callback) {

    }

    @Override
    public void getRecipeByUserId(long userId, DatabaseCallback<List<Recipe>> callback) {

    }

    @Override
    public void getAllRecipes(long userId, DatabaseCallback<List<Recipe>> callback) {

    }

    @Override
    public void updateRecipe(Recipe recipe, DatabaseCallback<Recipe> callback) {

    }

    @Override
    public void deleteRecipe(long recipeId, DatabaseCallback<Recipe> callback) {

    }
}
