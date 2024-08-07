package com.example.recipe.services;

import com.example.recipe.domain.common.DatabaseCallback;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.domain.recipe.Recipe;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecipeListingService {
    protected abstract GridPane getMenuGrid();

    protected abstract VBox getProgressContainer();

    private final List<VBox> menuComponents = new ArrayList<>();

    public void loadRecipeComponents(List<Recipe> data) {
        // Create a new FXMLLoader for each item
        // Update the cardBox with recipe data
        Task<Void> loadingTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    for (Recipe recipe : data) {
                        // Create a new FXMLLoader for each item
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/recipe/menu-item.fxml"));
                        VBox cardBox = fxmlLoader.load();

                        // Update the cardBox with recipe data
                        updateCardBoxWithRecipe(cardBox, recipe);
                        menuComponents.add(cardBox);
                    }
                    updateMessage("Loading completed");
                } catch (IOException e) {
                    updateMessage("Failed to load menu items");
                }
                return null;
            }
        };

        loadingTask.setOnSucceeded(event -> {
            updateGridPane(menuComponents);
            getProgressContainer().setVisible(false);
        });
        loadingTask.setOnFailed(event -> {
            getProgressContainer().setVisible(false);
        });

        new Thread(loadingTask).start();
    }

    // Method to update VBox with recipe data
    private void updateCardBoxWithRecipe(VBox cardBox, Recipe recipe) {
        Label titleLabel = (Label) cardBox.lookup("#recipeTitle");
        ImageView imageView = (ImageView) cardBox.lookup("#profileImage");
        titleLabel.setText(recipe.getTitle());
        imageView.setImage(new Image("file:" + recipe.getImage(), 200, 200, true, true));
    }

    public void updateGridPane(List<VBox> menuItems) {
        Platform.runLater(() -> {
            getMenuGrid().getChildren().clear();
            for (int i = 0; i < menuItems.size(); i++) {
                getMenuGrid().add(menuItems.get(i), i % 3, i / 3);
                GridPane.setMargin(menuItems.get(i), new Insets(10));
            }
        });
    }
}
