package com.example.recipe.domain.mappper;

import com.example.recipe.domain.User;
import com.example.recipe.domain.recipe.Category;
import com.example.recipe.domain.recipe.Recipe;
import com.example.recipe.domain.recipe.Review;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class RecipeMapper {
    public static List<Recipe> mapResultSetToRecipes(ResultSet resultSet, boolean fromSaved) throws SQLException {
        Map<Long, Recipe> recipeMap = new HashMap<>();

        boolean hasIsSavedColumn = false;
        if (!fromSaved) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                if ("is_saved".equalsIgnoreCase(metaData.getColumnName(i))) {
                    hasIsSavedColumn = true;
                    break;
                }
            }
        }
        while (resultSet.next()) {
            Long recipeId = resultSet.getLong("recipe_id");
            Recipe recipe = recipeMap.getOrDefault(recipeId, new Recipe());
            if (!recipeMap.containsKey(recipeId)) {
                recipe.setRecipeId(recipeId);
                recipe.setTitle(resultSet.getString("title"));
                recipe.setDescription(resultSet.getString("description"));
                recipe.setImage(resultSet.getString("image"));
                recipe.setVideoUrl(resultSet.getString("video_url"));
                recipe.setWarnings(resultSet.getString("warnings"));
                recipe.setCreatedAt(resultSet.getTimestamp("created_at"));
                recipe.setUpdatedAt(resultSet.getTimestamp("updated_at"));
                if (hasIsSavedColumn) {
                    recipe.setSaved(resultSet.getBoolean("is_saved"));
                }
                if (fromSaved)
                    recipe.setSaved(true);
                recipeMap.put(recipeId, recipe);
            }
            long categoryId = resultSet.getLong("category_id");
            if (categoryId != 0) {
                Category category = new Category();
                category.setCategoryId(categoryId);
                category.setCategoryName(resultSet.getString("category_name"));
                if (recipe.getCategory() == null) {
                    recipe.setCategory(new ArrayList<>());
                }
                recipe.getCategory().add(category);
            }
        }
        return new ArrayList<>(recipeMap.values());
    }


    public static Recipe mapRecipe(ResultSet resultSet) throws SQLException {
        Recipe recipe = new Recipe();
        recipe.setRecipeId(resultSet.getLong("recipe_id"));
        recipe.setTitle(resultSet.getString("title"));
        recipe.setDescription(resultSet.getString("description"));
        recipe.setImage(resultSet.getString("image"));
        recipe.setVideoUrl(resultSet.getString("video_url"));
        recipe.setCreatedAt(resultSet.getTimestamp("created_at"));
        recipe.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        recipe.setWarnings(resultSet.getString("warnings"));
        return recipe;
    }

    public static List<Review> mapResultSetToReviews(ResultSet resultSet) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        while (resultSet.next()) {
            Review review = new Review();
            review.setId(resultSet.getLong("review_id"));
            review.setRating(resultSet.getInt("rating"));
            review.setReview(resultSet.getString("comment"));
            review.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
            review.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());

            review.setUser(mapUser(resultSet));
            reviews.add(review);
        }
        return reviews;
    }

    public static User mapUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUserId(resultSet.getLong("user_id"));
        user.setUsername(resultSet.getString("username"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setEmail(resultSet.getString("email"));
        return user;
    }
}