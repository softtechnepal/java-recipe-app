package com.example.recipe.domain.mappper;

import com.example.recipe.domain.recipe.Category;
import com.example.recipe.domain.recipe.Recipe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class RecipeMapper {
    public static List<Recipe> mapResultSetToRecipes(ResultSet resultSet) throws SQLException {
        Map<Long, Recipe> recipeMap = new HashMap<>();
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
}