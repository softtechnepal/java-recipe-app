package com.example.recipe.ui.admin;

import com.example.recipe.domain.AdminDashboard;
import com.example.recipe.services.admin.AdminDashboardService;
import com.example.recipe.utils.ImageUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.Map;

public class DashboardController {
    @FXML
    public Text totalUsers;
    @FXML
    public Text totalCategories;
    @FXML
    public Text totalRecipes;
    @FXML
    public Text totalIngredients;
    @FXML
    public Text totalActiveUsers;
    @FXML
    public BarChart<String, Number> userGrowthChart;
    @FXML
    public LineChart<String, Number> recipeCreationChart;
    @FXML
    public PieChart mostUsedCategoriesChart;
    @FXML
    public VBox topUsersList;
    @FXML
    public VBox topSavedRecipesList;

    private final AdminDashboardService adminDashboard = new AdminDashboardService();

    public void initialize(){
        // Create and start a thread to load all data
        Thread loadDataThread = new Thread(this::loadAllData);
        loadDataThread.start();
    }

    private void loadAllData() {
        // Load all data
        Map<String, Integer> userGrowthData = adminDashboard.getUserGrowthData();
        Map<String, Integer> recipeGrowthData = adminDashboard.getRecipeGrowthData();
        Map<String, Integer> mostUsedCategoriesData = adminDashboard.getMostUsedCategoriesData();
        List<Map<String, String>> topUsersData = adminDashboard.getTopUsersData();
        List<Map<String, String>> topSavedRecipesData = adminDashboard.getTopSavedRecipes();
        AdminDashboard dashboardData = new AdminDashboardService().populateDashboard().getData();

        // Update UI on the JavaFX Application Thread
        Platform.runLater(() -> {
            updateDashboardCounts(dashboardData);
            populateUserGrowthChart(userGrowthData);
            populateRecipeGrowthChart(recipeGrowthData);
            populateMostUsedCategoriesChart(mostUsedCategoriesData);
            populateTopUsersList(topUsersData);
            populateTopSavedRecipeList(topSavedRecipesData);
        });
    }

    private void updateDashboardCounts(AdminDashboard dashboardData) {
        totalUsers.setText(String.valueOf(dashboardData.getUsersCount()));
        totalActiveUsers.setText(String.valueOf(dashboardData.getActiveUsersCount()));
        totalCategories.setText(String.valueOf(dashboardData.getCategoriesCount()));
        totalRecipes.setText(String.valueOf(dashboardData.getRecipesCount()));
        totalIngredients.setText(String.valueOf(dashboardData.getIngredientsCount()));
    }

    private void populateUserGrowthChart(Map<String, Integer> userGrowthData) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("User Growth");
        for (Map.Entry<String, Integer> entry : userGrowthData.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        userGrowthChart.getData().add(series);
    }

    private void populateRecipeGrowthChart(Map<String, Integer> recipeGrowthData) {
        XYChart.Series<String, Number> recipeCreationSeries = new XYChart.Series<>();
        recipeCreationSeries.setName("Recipe Creation");
        for (Map.Entry<String, Integer> entry : recipeGrowthData.entrySet()) {
            recipeCreationSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        recipeCreationChart.getData().add(recipeCreationSeries);
    }

    private void populateMostUsedCategoriesChart(Map<String, Integer> mostUsedCategoriesData) {
        for (Map.Entry<String, Integer> entry : mostUsedCategoriesData.entrySet()) {
            mostUsedCategoriesChart.getData().add(new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue()));
        }
    }

    private void populateTopUsersList(List<Map<String, String>> topUsersData) {
        for (Map<String, String> userData : topUsersData) {
            VBox userBox = new VBox();
            userBox.getStyleClass().add("user-box");

                String imagePath = (userData.get("profilePicture") != null && !userData.get("profilePicture").isEmpty()) ? userData.get("profilePicture") : "/assets/placeholder_user.png";
                try {
                    URL url = new URL("file:" + imagePath);
                    Image profileImage = new Image(url.toString());
                    ImageView profilePictureView = new ImageView(profileImage);
                    profilePictureView.getStyleClass().add("user-profile-picture");
                    userBox.getChildren().add(profilePictureView);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            Text fullName = new Text(userData.get("fullName"));
            fullName.getStyleClass().add("user-fullname");
            userBox.getChildren().add(fullName);

            Text username = new Text("@" + userData.get("username"));
            username.getStyleClass().add("user-username");
            userBox.getChildren().add(username);

            Text totalRecipes = new Text("Total Recipes: " + userData.get("totalRecipes"));
            totalRecipes.getStyleClass().add("user-total-recipes");
            userBox.getChildren().add(totalRecipes);

            Text createdDate = new Text("Joined: " + userData.get("createdDate").split(" ")[0]);
            createdDate.getStyleClass().add("user-created-date");
            userBox.getChildren().add(createdDate);

            topUsersList.getChildren().add(userBox);
        }
    }

    private  void  populateTopSavedRecipeList(List<Map<String, String>> topSavedRecipesData) {
        for (Map<String, String> recipeData : topSavedRecipesData) {
            VBox recipeBox = new VBox();
            recipeBox.getStyleClass().add("recipe-box");

            Text recipeName = new Text(recipeData.get("recipeName"));
            recipeName.getStyleClass().add("recipe-name");
            recipeBox.getChildren().add(recipeName);

            Text recipeCategory = new Text("Created Date: " + recipeData.get("createdDate").split(" ")[0]);
            recipeCategory.getStyleClass().add("recipe-category");
            recipeBox.getChildren().add(recipeCategory);

            Text recipeCreator = new Text("Creator: " + recipeData.get("username"));
            recipeCreator.getStyleClass().add("recipe-creator");
            recipeBox.getChildren().add(recipeCreator);

            Text recipeSavedCount = new Text("Saved Times: " + recipeData.get("savedCount"));
            recipeSavedCount.getStyleClass().add("recipe-saved-count");
            recipeBox.getChildren().add(recipeSavedCount);

            topSavedRecipesList.getChildren().add(recipeBox);
        }
    }
}