package com.example.recipe.services.admin;

import com.example.recipe.domain.AdminDashboard;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.repositories.iadmin.IAdminDashboard;
import com.example.recipe.repositories.impl.AdminDashboardImpl;

import java.util.List;
import java.util.Map;


public class AdminDashboardService {
    private final IAdminDashboard dashboardRepository;

    public AdminDashboardService() {
        dashboardRepository = new AdminDashboardImpl();
    }

    public DbResponse<AdminDashboard> populateDashboard() {
        return dashboardRepository.populateDashboard();
    }

    public Map<String, Integer> getUserGrowthData() {
        return dashboardRepository.getUserGrowthData();
    }

    public Map<String, Integer> getRecipeGrowthData() {
        return dashboardRepository.getRecipeGrowthData();
    }
    public Map<String, Integer> getMostUsedCategoriesData() {
        return dashboardRepository.getMostUsedCategoriesData();
    }
    public List<Map<String, String>> getTopUsersData() {
        return dashboardRepository.getTopUsersData();
    }
    public List<Map<String, String>> getTopSavedRecipes() {
        return dashboardRepository.getTopSavedRecipes();
    }
}
