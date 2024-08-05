package com.example.recipe.ui.user;

import com.example.recipe.domain.recipe.Recipe;
import com.example.recipe.services.user.UserRecipeService;
import com.example.recipe.utils.NavigationUtil;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.recipe.utils.LoggerUtil.logger;

public class MyRecipeController {
    @FXML
    public GridPane menuGrid;
    @FXML
    public Button btnAddRecipe;
    @FXML
    public VBox progressContainer;

    private Task<Void> loadingTask;

    private static final UserRecipeService userRecipeService = new UserRecipeService();

    @FXML
    public void initialize() {
        btnAddRecipe.setOnAction(this::onAddRecipe);
        fetchRecipes();
    }

    private void onAddRecipe(ActionEvent actionEvent) {
        if (loadingTask != null) {
            loadingTask.cancel();
        }
        NavigationUtil.insertChild("add-recipe-view.fxml");
    }

    private void fetchRecipes() {
        userRecipeService.getRecipeByUserId(107, response -> {
            if (response.isSuccess()) {
                if (!response.getData().isEmpty())
                    loadMenuItems(response.getData());
            } else {
                logger.error("Failed to fetch recipes {}", response.getMessage());
            }
        });
    }

    private void loadMenuItems(List<Recipe> data) {
        try {
            Platform.runLater(() -> progressContainer.setVisible(true));
            List<VBox> menuItems = new ArrayList<>();
            loadingTask = new Task<>() {
                @Override
                protected Void call() {
                    try {
                        for (Recipe recipe : data) {
                            // Create a new FXMLLoader for each item
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/recipe/menu-item.fxml"));
                            VBox cardBox = fxmlLoader.load();

                            // Update the cardBox with recipe data
                            updateCardBoxWithRecipe(cardBox, recipe);
                            menuItems.add(cardBox);
                        }
                        updateMessage("Loading completed");
                    } catch (IOException e) {
                        updateMessage("Failed to load menu items");
                    }
                    return null;
                }
            };

            loadingTask.setOnSucceeded(event -> {
                menuGrid.getChildren().clear();
                for (int i = 0; i < menuItems.size(); i++) {
                    menuGrid.add(menuItems.get(i), i % 3, i / 3);
                    GridPane.setMargin(menuItems.get(i), new Insets(10));
                }
                Platform.runLater(() -> progressContainer.setVisible(false));
            });

            loadingTask.setOnFailed(event -> {
                Platform.runLater(() -> progressContainer.setVisible(false));
                logger.error("Failed to load menu items", loadingTask.getException());
            });


            new Thread(loadingTask).start();
        } catch (Exception e) {
            logger.error("Failed to load menu items {}", e.getMessage());
        }
    }

    // Method to update VBox with recipe data
    private void updateCardBoxWithRecipe(VBox cardBox, Recipe recipe) {
        Label titleLabel = (Label) cardBox.lookup("#recipeTitle");
        ImageView imageView = (ImageView) cardBox.lookup("#profileImage");
        Platform.runLater(() -> {
            titleLabel.setText(recipe.getTitle());
            imageView.setImage(new Image("file:" + recipe.getImage(), 200, 200, true, true));
        });
    }

}