package com.example.recipe.repositories.impl;

import com.example.recipe.domain.AdminDashboard;
import com.example.recipe.domain.User;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.repositories.iadmin.IAdminDashboard;
import com.example.recipe.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AdminDashboardImpl implements IAdminDashboard {
    @Override
    public DbResponse<AdminDashboard> populateDashboard() {
        try (Connection connection = DatabaseConfig.getConnection()) {
            CompletableFuture<Integer> totalUsersFuture = CompletableFuture.supplyAsync(() -> getCount(connection, "SELECT COUNT(*) FROM users WHERE is_admin = false"));
            CompletableFuture<Integer> totalActiveUsersFuture = CompletableFuture.supplyAsync(() -> getCount(connection, "SELECT " +
                    "COUNT(*) FROM users WHERE is_admin = false"));
            CompletableFuture<Integer> totalRecipesFuture = CompletableFuture.supplyAsync(() -> getCount(connection, "SELECT COUNT(*) FROM recipes"));
            CompletableFuture<Integer> totalCategoriesFuture = CompletableFuture.supplyAsync(() -> getCount(connection, "SELECT COUNT(*) FROM categories"));
            CompletableFuture<Integer> totalReviewsFuture = CompletableFuture.supplyAsync(() -> getCount(connection, "SELECT COUNT(*) FROM reviews"));
            CompletableFuture<Integer> totalIngredientsFuture = CompletableFuture.supplyAsync(() -> getCount(connection, "SELECT COUNT(*) FROM ingredients"));
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                    totalUsersFuture, totalActiveUsersFuture, totalRecipesFuture,
                    totalCategoriesFuture, totalReviewsFuture, totalIngredientsFuture
            );

            allFutures.join();
            AdminDashboard dashboard = new AdminDashboard();
            dashboard.setUsersCount(totalUsersFuture.get());
            dashboard.setActiveUsersCount(totalActiveUsersFuture.get());
            dashboard.setRecipesCount(totalRecipesFuture.get());
            dashboard.setCategoriesCount(totalCategoriesFuture.get());
            dashboard.setReviewsCount(totalReviewsFuture.get());
            dashboard.setIngredientsCount(totalIngredientsFuture.get());

            return new DbResponse.Success<>("Dashboard data retrieved successfully", dashboard);
        } catch (Exception e) {
            return new DbResponse.Failure<>(e.getMessage());
        }
    }

    private int getCount(Connection connection, String query) {
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return 0;
    }

    private String getString(String query) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return "";
    }

    @Override
    public Map<String, Integer> getUserGrowthData() {
        String query = "SELECT TO_CHAR(created_at, 'YYYY-MM-DD') as date, COUNT(*) as count FROM users GROUP BY " +
                "TO_CHAR(created_at, 'YYYY-MM-DD') ORDER BY TO_CHAR(created_at, 'YYYY-MM-DD')";
        Map<String, Integer> userGrowthData = new HashMap<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                userGrowthData.put(rs.getString("date"), rs.getInt("count"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userGrowthData;
    }

    @Override
    public Map<String, Integer> getRecipeGrowthData() {
        String query = "SELECT TO_CHAR(created_at, 'YYYY-MM-DD') as date, COUNT(*) as count FROM recipes GROUP BY " +
                "TO_CHAR(created_at, 'YYYY-MM-DD') ORDER BY TO_CHAR(created_at, 'YYYY-MM-DD')";
        Map<String, Integer> recipeGrowthData = new HashMap<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                recipeGrowthData.put(rs.getString("date"), rs.getInt("count"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recipeGrowthData;
    }

    @Override
    public Map<String, Integer> getMostUsedCategoriesData() {
        String query = "SELECT category_name, COUNT(*) as count FROM recipecategories " +
                "JOIN categories ON recipecategories.category_id = categories.category_id " +
                "GROUP BY category_name ORDER BY count DESC LIMIT 5";
        Map<String, Integer> mostUsedCategoriesData = new HashMap<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                mostUsedCategoriesData.put(rs.getString("category_name"), rs.getInt("count"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mostUsedCategoriesData;
    }

    @Override
    public List<Map<String, String>> getTopUsersData() {
        String query = "SELECT users.first_name || ' ' || users.last_name AS fullname, users.profile_picture, users.username, " +
                "COUNT(recipes.recipe_id) as total_recipes, users.created_at " +
                "FROM users " +
                "JOIN recipes ON users.user_id = recipes.user_id " +
                "GROUP BY users.user_id, users.first_name, users.last_name, users.profile_picture, users.username, users.created_at " +
                "ORDER BY total_recipes DESC " +
                "LIMIT 5";
        List<Map<String, String>> topUsersData = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, String> userData = new HashMap<>();
                userData.put("fullName", rs.getString("fullname"));
                userData.put("profilePicture", rs.getString("profile_picture"));
                userData.put("username", rs.getString("username"));
                userData.put("totalRecipes", String.valueOf(rs.getInt("total_recipes")));
                userData.put("createdDate", rs.getString("created_at"));
                topUsersData.add(userData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topUsersData;
    }

    @Override
    public List<Map<String, String>> getTopSavedRecipes() {
        String query = "SELECT recipes.title as recipe_name, COUNT(wishlist.recipe_id) as saved_count, users.username, " +
                "recipes.created_at " +
                "FROM recipes " +
                "JOIN wishlist ON recipes.recipe_id = wishlist.recipe_id " +
                "JOIN users ON recipes.user_id = users.user_id " +
                "GROUP BY recipes.recipe_id, recipes.title, users.username, recipes.created_at " +
                "ORDER BY saved_count DESC " +
                "LIMIT 5";
        List<Map<String, String>> topSavedRecipesData = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, String> recipeData = new HashMap<>();
                recipeData.put("recipeName", rs.getString("recipe_name"));
                recipeData.put("username", rs.getString("username"));
                recipeData.put("savedCount", String.valueOf(rs.getInt("saved_count")));
                recipeData.put("createdDate", rs.getString("created_at"));
                topSavedRecipesData.add(recipeData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topSavedRecipesData;
    }

}