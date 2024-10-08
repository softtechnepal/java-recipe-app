package com.example.recipe.repositories.impl;

import com.example.recipe.config.DatabaseConfig;
import com.example.recipe.domain.User;
import com.example.recipe.domain.common.DatabaseCallback;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.domain.mappper.RecipeMapper;
import com.example.recipe.domain.recipe.*;
import com.example.recipe.repositories.iuser.UserRecipeRepository;
import com.example.recipe.services.UserDetailStore;
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
                "(user_id, title, description, image, video_url, warnings, number_of_servings, total_preparation_time) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING recipe_id";
        try (PreparedStatement statement = connection.prepareStatement(insertRecipeQuery, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, UserDetailStore.getInstance().getUserId());
            statement.setString(2, recipe.getTitle());
            statement.setString(3, recipe.getDescription());
            statement.setString(4, recipe.getImage());
            statement.setString(5, recipe.getVideoUrl());
            statement.setString(6, recipe.getWarnings());
            statement.setInt(7, recipe.getTotalServings() != null ? recipe.getTotalServings() : 0);
            statement.setInt(8, recipe.getPrepTime() != null ? recipe.getPrepTime() : 0);
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
        final String SELECT_RECIPE_DETAIL_QUERY = """
                SELECT r.recipe_id, r.title, r.description, r.image, r.video_url, r.warnings, r.created_at, r.updated_at, r.number_of_servings, r.total_preparation_time,
                       u.user_id, u.username, u.email, u.first_name, u.last_name, u.profile_picture,
                       c.category_id, c.category_name,
                       i.ingredient_id, i.ingredient_name, i.quantity, i.unit,
                       s.step_id, s.step_description, s.step_order, s.step_name,
                       n.calories, n.protein, n.fat, n.carbohydrates, n.other,
                       CASE WHEN w.recipe_id IS NOT NULL THEN TRUE ELSE FALSE END AS is_saved
                FROM recipes r
                LEFT JOIN users u ON r.user_id = u.user_id
                LEFT JOIN recipecategories rc ON r.recipe_id = rc.recipe_id
                LEFT JOIN categories c ON rc.category_id = c.category_id
                LEFT JOIN ingredients ri ON r.recipe_id = ri.recipe_id
                LEFT JOIN ingredients i ON ri.ingredient_id = i.ingredient_id
                LEFT JOIN recipesteps rs ON r.recipe_id = rs.recipe_id
                LEFT JOIN recipesteps s ON rs.step_id = s.step_id
                LEFT JOIN nutritionalinformation n ON r.recipe_id = n.recipe_id
                LEFT JOIN wishlist w ON r.recipe_id = w.recipe_id AND w.user_id = ?
                WHERE r.recipe_id = ?;
                """;
        DatabaseThread.runDataOperation(() -> {
            try (
                    Connection connection = DatabaseConfig.getConnection();
                    PreparedStatement statement = connection.prepareStatement(SELECT_RECIPE_DETAIL_QUERY)
            ) {
                statement.setLong(1, UserDetailStore.getInstance().getUserId());
                statement.setLong(2, recipeId);
                ResultSet resultSet = statement.executeQuery();
                Recipe recipe = null;
                List<Category> categories = new ArrayList<>();
                List<Ingredient> ingredients = new ArrayList<>();
                List<Steps> steps = new ArrayList<>();

                while (resultSet.next()) {
                    if (recipe == null) {
                        // Initialize the Recipe object only once
                        recipe = RecipeMapper.mapRecipe(resultSet);
                        recipe.setSaved(resultSet.getBoolean("is_saved"));

                        // Set user details
                        recipe.setUser(RecipeMapper.mapUser(resultSet));

                        // Initialize the lists
                        categories = new ArrayList<>();
                        ingredients = new ArrayList<>();
                        steps = new ArrayList<>();
                    }

                    // Set categories
                    Long categoryId = resultSet.getLong("category_id");
                    if (!resultSet.wasNull() && categories.stream().map(Category::getCategoryId).noneMatch(id -> id.equals(categoryId))) {
                        Category category = new Category();
                        category.setCategoryId(categoryId);
                        category.setCategoryName(resultSet.getString("category_name"));
                        categories.add(category);
                    }

                    // Set ingredients
                    Long ingredientId = resultSet.getLong("ingredient_id");
                    if (!resultSet.wasNull() && ingredients.stream().map(Ingredient::getIngredientId).noneMatch(id -> id.equals(ingredientId))) {
                        Ingredient ingredient = new Ingredient();
                        ingredient.setIngredientId(ingredientId);
                        ingredient.setIngredientName(resultSet.getString("ingredient_name"));
                        ingredient.setQuantity(resultSet.getDouble("quantity"));
                        ingredient.setUnit(resultSet.getString("unit"));
                        ingredients.add(ingredient);
                    }

                    // Set steps
                    Long stepId = resultSet.getLong("step_id");
                    if (!resultSet.wasNull() && steps.stream().map(Steps::getStepId).noneMatch(id -> id.equals(stepId))) {
                        Steps step = new Steps();
                        step.setStepId(stepId);
                        step.setStepDescription(resultSet.getString("step_description"));
                        step.setStepOrder(resultSet.getInt("step_order"));
                        step.setStepName(resultSet.getString("step_name"));
                        steps.add(step);
                    }

                    // Set nutritional information
                    NutritionalInformation nutritionalInformation = new NutritionalInformation();
                    nutritionalInformation.setCalories(resultSet.getInt("calories"));
                    nutritionalInformation.setProtein(resultSet.getDouble("protein"));
                    nutritionalInformation.setFat(resultSet.getDouble("fat"));
                    nutritionalInformation.setCarbohydrates(resultSet.getDouble("carbohydrates"));
                    recipe.setNutritionalInformation(nutritionalInformation);
                }

                if (recipe != null) {
                    recipe.setCategory(categories);
                    recipe.setIngredients(ingredients);
                    recipe.setSteps(steps);
                }

                return DbResponse.success("Success", recipe);
            } catch (Exception e) {
                logger.error("Error while fetching recipe by ID", e);
                return DbResponse.failure(e.getMessage());
            }
        }, callback);
    }

    @Override
    public void getRecipeByUserId(long userId, DatabaseCallback<List<Recipe>> callback) {
        final String SELECT_RECIPE_BY_ID_QUERY = "SELECT * FROM recipes WHERE user_id = ? ORDER BY created_at DESC";

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

    @Override
    public void getAllRecipes(DatabaseCallback<List<Recipe>> callback) {
        final String SELECT_RECIPE_QUERY = """
                SELECT r.recipe_id, r.title, r.description, r.image, r.video_url, r.warnings, r.created_at, r.updated_at,r.total_preparation_time, r.number_of_servings,
                       c.category_id, c.category_name,
                       CASE WHEN w.user_id IS NOT NULL THEN TRUE ELSE FALSE END AS is_saved
                FROM recipes r
                         LEFT JOIN recipecategories rc ON r.recipe_id = rc.recipe_id
                         LEFT JOIN categories c ON rc.category_id = c.category_id
                         LEFT JOIN wishlist w ON r.recipe_id = w.recipe_id AND w.user_id = ?
                ORDER BY r.created_at DESC;
                """;

        DatabaseThread.runDataOperation(() -> {
            try (
                    Connection connection = DatabaseConfig.getConnection();
                    PreparedStatement statement = connection.prepareStatement(SELECT_RECIPE_QUERY)
            ) {
                statement.setLong(1, UserDetailStore.getInstance().getUserId());
                List<Recipe> recipes = RecipeMapper.mapResultSetToRecipes(statement.executeQuery(), false);
                return DbResponse.success("Success", recipes);
            } catch (Exception e) {
                logger.error("Error while fetching recipe by ID", e);
                return DbResponse.failure(e.getMessage());
            }
        }, callback);
    }

    private List<Recipe> mapResultSetToRecipes(ResultSet resultSet) throws SQLException {
        List<Recipe> recipes = new ArrayList<>();
        while (resultSet.next()) {
            Recipe recipe = RecipeMapper.mapRecipe(resultSet);
            recipes.add(recipe);
        }
        return recipes;
    }

    @Override
    public void updateRecipe(Recipe recipe, DatabaseCallback<Recipe> callback) {
        String UPDATE_RECIPE_QUERY = """
                    UPDATE recipes
                    SET title = ?, description = ?, image = ?, video_url = ?, warnings = ?,total_preparation_time = ?, number_of_servings = ?, updated_at = NOW()
                    WHERE recipe_id = ? AND user_id = ?
                """;
        DatabaseThread.runDataOperation(() -> {
            try (Connection connection = DatabaseConfig.getConnection()) {
                connection.setAutoCommit(false);

                String imagePath = recipe.getImage();
                if (!imagePath.contains("dbimages/")) {
                    imagePath = ImageUtil.copyImageToDbImages(new File(recipe.getImage()));
                }
                try (PreparedStatement statement = connection.prepareStatement(UPDATE_RECIPE_QUERY)) {
                    statement.setString(1, recipe.getTitle());
                    statement.setString(2, recipe.getDescription());
                    statement.setString(3, imagePath);
                    statement.setString(4, recipe.getVideoUrl());
                    statement.setString(5, recipe.getWarnings());
                    statement.setInt(6, recipe.getPrepTime() == null ? 0 : recipe.getPrepTime());
                    statement.setInt(7, recipe.getTotalServings() == null ? 0 : recipe.getTotalServings());
                    statement.setLong(8, recipe.getRecipeId());
                    statement.setLong(9, UserDetailStore.getInstance().getUserId());
                    statement.executeUpdate();

                    // Update Ingredients
                    deleteIngredients(connection, recipe.getRecipeId());
                    insertIngredients(connection, recipe.getIngredients(), recipe.getRecipeId());

                    // Update categories
                    deleteCategories(connection, recipe.getRecipeId());
                    insertCategories(connection, recipe.getCategory(), recipe.getRecipeId());

                    // Update Steps
                    deleteSteps(connection, recipe.getRecipeId());
                    insertSteps(connection, recipe.getSteps(), recipe.getRecipeId());

                    // Update Nutritional Information
                    updateNutritionalInformation(connection, recipe.getNutritionalInformation(), recipe.getRecipeId());

                    connection.commit();
                    return DbResponse.success("Recipe updated successfully", recipe);
                }
            } catch (Exception exception) {
                logger.error("Error while updating recipe", exception);
                return DbResponse.failure(exception.getMessage());
            }
        }, callback);
    }

    private void deleteIngredients(Connection connection, long recipeId) throws SQLException {
        String DELETE_INGREDIENT_QUERY = """
                        DELETE FROM ingredients WHERE recipe_id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(DELETE_INGREDIENT_QUERY)) {
            statement.setLong(1, recipeId);
            statement.executeUpdate();
        }
    }

    private void deleteCategories(Connection connection, long recipeId) throws SQLException {
        String DELETE_CATEGORY_QUERY = """
                        DELETE FROM recipecategories WHERE recipe_id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(DELETE_CATEGORY_QUERY)) {
            statement.setLong(1, recipeId);
            statement.executeUpdate();
        }
    }

    protected void deleteSteps(Connection connection, long recipeId) throws SQLException {
        String DELETE_STEP_QUERY = """
                        DELETE FROM recipesteps WHERE recipe_id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(DELETE_STEP_QUERY)) {
            statement.setLong(1, recipeId);
            statement.executeUpdate();
        }
    }

    private void deleteNutritionalInformation(Connection connection, long recipeId) throws SQLException {
        String DELETE_NUTRITIONAL_INFO_QUERY = """
                        DELETE FROM nutritionalinformation WHERE recipe_id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(DELETE_NUTRITIONAL_INFO_QUERY)) {
            statement.setLong(1, recipeId);
            statement.executeUpdate();
        }
    }

    private void updateNutritionalInformation(Connection connection, NutritionalInformation nutritionalInformation, Long recipeId) throws SQLException {
        String updateNutritionalInfoQuery = """
                    UPDATE nutritionalinformation
                    SET calories = ?, protein = ?, fat = ?, carbohydrates = ?, other = ?
                    WHERE recipe_id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(updateNutritionalInfoQuery)) {
            statement.setDouble(1, nutritionalInformation.getCalories() != null ? nutritionalInformation.getCalories() : 0);
            statement.setDouble(2, nutritionalInformation.getProtein() != null ? nutritionalInformation.getProtein() : 0);
            statement.setDouble(3, nutritionalInformation.getFat() != null ? nutritionalInformation.getFat() : 0);
            statement.setDouble(4, nutritionalInformation.getCarbohydrates() != null ? nutritionalInformation.getCarbohydrates() : 0);
            statement.setString(5, nutritionalInformation.getOther() != null ? nutritionalInformation.getOther() : "");
            statement.setLong(6, recipeId);
            statement.executeUpdate();
        }
    }

    private void deleteRecipe(Connection connection, long recipeId) throws SQLException {
        String DELETE_RECIPE_QUERY = """
                        DELETE FROM recipes WHERE recipe_id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(DELETE_RECIPE_QUERY)) {
            statement.setLong(1, recipeId);
            statement.executeUpdate();
        }
    }

    private void deleteRecipeReviews(Connection connection, long recipeId) throws SQLException {
        String DELETE_RECIPE_REVIEWS_QUERY = """
                        DELETE FROM reviews WHERE recipe_id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(DELETE_RECIPE_REVIEWS_QUERY)) {
            statement.setLong(1, recipeId);
            statement.executeUpdate();
        }
    }

    private void deleteRecipeWishList(Connection connection, long recipeId) throws SQLException {
        String DELETE_RECIPE_WISHLIST_QUERY = """
                        DELETE FROM wishlist WHERE recipe_id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(DELETE_RECIPE_WISHLIST_QUERY)) {
            statement.setLong(1, recipeId);
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteRecipe(long recipeId, DatabaseCallback<Boolean> callback) {
        DatabaseThread.runDataOperation(() -> {
            try (Connection connection = DatabaseConfig.getConnection()) {
                connection.setAutoCommit(false);
                deleteIngredients(connection, recipeId);
                deleteCategories(connection, recipeId);
                deleteSteps(connection, recipeId);
                deleteNutritionalInformation(connection, recipeId);
                deleteRecipeReviews(connection, recipeId);
                deleteRecipeWishList(connection, recipeId);
                deleteRecipe(connection, recipeId);
                connection.commit();
                return DbResponse.success("Recipe deleted successfully", true);
            } catch (Exception e) {
                logger.error("Error while deleting recipe", e);
                return DbResponse.failure(e.getMessage());
            }
        }, callback);
    }

    @Override
    public void addRecipeFavorite(long recipeId, long userId, DatabaseCallback<Boolean> callback) {
        DatabaseThread.runDataOperation(() -> {
            try (Connection connection = DatabaseConfig.getConnection()) {
                String checkQuery = "SELECT COUNT(*) FROM wishlist WHERE recipe_id = ? AND user_id = ?";
                try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                    checkStmt.setLong(1, recipeId);
                    checkStmt.setLong(2, userId);
                    try (ResultSet resultSet = checkStmt.executeQuery()) {
                        if (resultSet.next() && resultSet.getInt(1) > 0) {
                            // Recipe is already in wishlist, remove it
                            String deleteQuery = "DELETE FROM wishlist WHERE recipe_id = ? AND user_id = ?";
                            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                                deleteStmt.setLong(1, recipeId);
                                deleteStmt.setLong(2, userId);
                                deleteStmt.executeUpdate();
                                return DbResponse.success("Recipe removed from wishlist", false);
                            }
                        } else {
                            // Recipe is not in wishlist, add it
                            String insertQuery = "INSERT INTO wishlist (recipe_id, user_id) VALUES (?, ?)";
                            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                                insertStmt.setLong(1, recipeId);
                                insertStmt.setLong(2, userId);
                                insertStmt.executeUpdate();
                                return DbResponse.success("Recipe added to wishlist", true);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Error while updating wishlist", e);
                return DbResponse.failure(e.getMessage());
            }
        }, callback);
    }

    @Override
    public void getFavoriteRecipes(DatabaseCallback<List<Recipe>> callback) {
        final String SELECT_FAVORITE_RECIPE_QUERY = """
                SELECT r.recipe_id, r.title, r.description, r.image, r.video_url, r.warnings, r.created_at, r.updated_at, r.total_preparation_time, r.number_of_servings,
                                       c.category_id, c.category_name
                                FROM recipes r
                                         RIGHT JOIN wishlist w ON r.recipe_id = w.recipe_id
                                         LEFT JOIN recipecategories rc ON w.recipe_id = rc.recipe_id
                                         LEFT JOIN categories c ON rc.category_id = c.category_id
                                WHERE w.user_id = ?;
                """;

        DatabaseThread.runDataOperation(() -> {
            try (
                    Connection connection = DatabaseConfig.getConnection();
                    PreparedStatement statement = connection.prepareStatement(SELECT_FAVORITE_RECIPE_QUERY)
            ) {
                statement.setLong(1, UserDetailStore.getInstance().getUserId());
                List<Recipe> recipes = RecipeMapper.mapResultSetToRecipes(statement.executeQuery(), true);
                return DbResponse.success("Success", recipes);
            } catch (Exception e) {
                logger.error("Error while fetching favorite recipes", e);
                return DbResponse.failure(e.getMessage());
            }
        }, callback);
    }

    @Override
    public void getRecipeReviews(long recipeId, DatabaseCallback<List<Review>> callback) {
        final String SELECT_RECIPE_REVIEWS_QUERY = """
                SELECT r.review_id, r.rating, r.review, r.created_at, r.updated_at,
                       u.user_id, u.username, u.email, u.first_name, u.last_name, u.profile_picture
                FROM reviews r
                JOIN users u ON r.user_id = u.user_id
                WHERE r.recipe_id = ?;
                """;

        DatabaseThread.runDataOperation(() -> {
            try (
                    Connection connection = DatabaseConfig.getConnection();
                    PreparedStatement statement = connection.prepareStatement(SELECT_RECIPE_REVIEWS_QUERY)
            ) {
                statement.setLong(1, recipeId);
                List<Review> reviews = RecipeMapper.mapResultSetToReviews(statement.executeQuery());
                return DbResponse.success("Success", reviews);
            } catch (Exception e) {
                logger.error("Error while fetching recipe reviews", e);
                return DbResponse.failure(e.getMessage());
            }
        }, callback);
    }

    @Override
    public void addReview(long recipeId, Review review, DatabaseCallback<Review> callback) {
        String INSERT_REVIEW_QUERY = "INSERT INTO reviews (recipe_id, user_id, rating, review) VALUES (?, ?, ?, ?)";

        DatabaseThread.runDataOperation(() -> {
            try (Connection connection = DatabaseConfig.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(INSERT_REVIEW_QUERY, Statement.RETURN_GENERATED_KEYS)) {
                    statement.setLong(1, recipeId);
                    statement.setLong(2, UserDetailStore.getInstance().getUserId());
                    statement.setInt(3, review.getRating());
                    statement.setString(4, review.getReview());
                    statement.execute();
                    try (ResultSet resultSet = statement.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            review.setId(resultSet.getLong(1));
                            return DbResponse.success("Review added successfully", review);
                        } else {
                            throw new SQLException("Failed to get review ID");
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Error while adding review", e);
                return DbResponse.failure(e.getMessage());
            }
        }, callback);
    }

    @Override
    public void deleteReview(Long reviewId, DatabaseCallback<Boolean> callback) {
        String DELETE_REVIEW_QUERY = "DELETE FROM reviews WHERE review_id = ?";

        DatabaseThread.runDataOperation(() -> {
            try (Connection connection = DatabaseConfig.getConnection()) {
                connection.setAutoCommit(false);
                try (PreparedStatement statement = connection.prepareStatement(DELETE_REVIEW_QUERY)) {
                    statement.setLong(1, reviewId);
                    statement.executeUpdate();
                    connection.commit();
                    return DbResponse.success("Review deleted successfully", true);
                }
            } catch (Exception e) {
                logger.error("Error while deleting review", e);
                return DbResponse.failure(e.getMessage());
            }
        }, callback);
    }
}
