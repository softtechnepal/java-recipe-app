package com.example.recipe.repositories.iadmin;

import com.example.recipe.domain.AdminDashboard;
import com.example.recipe.domain.common.DbResponse;

import java.util.List;
import java.util.Map;

public interface IAdminDashboard {
    DbResponse<AdminDashboard> populateDashboard();
    Map<String, Integer> getUserGrowthData();
    Map<String, Integer> getRecipeGrowthData();
    Map<String, Integer> getMostUsedCategoriesData();
    List<Map<String, String>> getTopUsersData();
    List<Map<String, String>> getTopSavedRecipes();
}
