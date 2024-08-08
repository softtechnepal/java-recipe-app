package com.example.recipe.repositories.impl;

import com.example.recipe.config.DatabaseConfig;
import com.example.recipe.domain.common.DatabaseCallback;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.domain.mappper.RecipeMapper;
import com.example.recipe.domain.recipe.*;
import com.example.recipe.repositories.iuser.UserRecipeRepository;
import com.example.recipe.utils.DatabaseThread;
import com.example.recipe.utils.ImageUtil;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            // todo(gprachan) set it do different place for better separation of concerns
            statement.setInt(1, 108);
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
    public void getRecipeDetailById(long recipeId, DatabaseCallback<Recipe> callback) {

    }

    @Override
    public void getRecipeByUserId(long userId, DatabaseCallback<List<Recipe>> callback) {
        final String SELECT_RECIPE_BY_ID_QUERY = "SELECT * FROM recipes WHERE user_id = ?";

        DatabaseThread.runDataOperation(() -> {
            try (
                    Connection connection = DatabaseConfig.getConnection();
                    PreparedStatement statement = connection.prepareStatement(SELECT_RECIPE_BY_ID_QUERY)
            ) {
                statement.setLong(1, userId);
                return DbResponse.success("", mapResultSetToRecipes(statement.executeQuery()));
            } catch (Exception e) {
                logger.error("Error while fetching recipe by ID", e);
                return DbResponse.failure(e.getMessage());
            }
        }, callback);
    }

    private List<Recipe> mapResultSetToRecipes(ResultSet resultSet) throws SQLException {
        List<Recipe> recipes = new ArrayList<>();
        while (resultSet.next()) {
            Recipe recipe = mapResultSetToRecipe(resultSet);
            recipes.add(recipe);
        }
        return recipes;
    }

    // todo make mapper class for this which maps all the database to its respective object
    private Recipe mapResultSetToRecipe(ResultSet resultSet) throws SQLException {
        Recipe recipe = new Recipe();
        recipe.setRecipeId(resultSet.getLong("recipe_id"));
        recipe.setTitle(resultSet.getString("title"));
        recipe.setDescription(resultSet.getString("description"));
        recipe.setImage(resultSet.getString("image"));
        recipe.setVideoUrl(resultSet.getString("video_url"));
        recipe.setCreatedAt(resultSet.getTimestamp("created_at"));
        recipe.setWarnings(resultSet.getString("warnings"));
        return recipe;
    }

    @Override
    public void getAllRecipes(DatabaseCallback<List<Recipe>> callback) {
        final String SELECT_RECIPE_QUERY = """
                SELECT r.recipe_id, r.title, r.description, r.image, r.video_url, r.warnings, r.created_at, r.updated_at,
                       c.category_id, c.category_name
                FROM recipes r
                         LEFT JOIN recipecategories rc ON r.recipe_id = rc.recipe_id
                         LEFT JOIN categories c ON rc.category_id = c.category_id;
                """;

        DatabaseThread.runDataOperation(() -> {
            try (
                    Connection connection = DatabaseConfig.getConnection();
                    PreparedStatement statement = connection.prepareStatement(SELECT_RECIPE_QUERY)
            ) {
                List<Recipe> recipes = RecipeMapper.mapResultSetToRecipes(statement.executeQuery());
                return DbResponse.success("Success", recipes);
            } catch (Exception e) {
                logger.error("Error while fetching recipe by ID", e);
                return DbResponse.failure(e.getMessage());
            }
        }, callback);
    }

    @Override
    public void updateRecipe(Recipe recipe, DatabaseCallback<Recipe> callback) {

    }

    @Override
    public void deleteRecipe(long recipeId, DatabaseCallback<Recipe> callback) {

    }
}
