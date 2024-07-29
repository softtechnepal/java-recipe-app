package com.example.recipe.ui.admin;

import com.example.recipe.services.admin_access.AdminCategoryService;
import com.example.recipe.services.admin_access.AdminRecipeService;
import com.example.recipe.services.admin_access.AdminUserService;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class DashboardController {
    @FXML
    public Text totalUsers;
    @FXML
    public Text totalCategories;
    @FXML
    public Text totalRecipes;
    private AdminCategoryService categoryService;
    private AdminUserService userService;
    private AdminRecipeService recipeService;

    public void initialize(){
        // Load the data on view
        categoryService = new AdminCategoryService();
        userService = new AdminUserService();
        recipeService = new AdminRecipeService();
        loadData();
    }

    private void loadData(){
        totalUsers.setText(String.valueOf(userService.getAllUsers().getData().toArray().length));
        totalRecipes.setText(String.valueOf(recipeService.getAllRecipes().getData().toArray().length));
        totalCategories.setText(String.valueOf(categoryService.getAllCategories().getData().toArray().length));
    }
}

