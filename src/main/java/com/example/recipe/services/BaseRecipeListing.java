package com.example.recipe.services;

import com.example.recipe.domain.enums.MenuListingType;
import com.example.recipe.domain.recipe.Recipe;
import com.example.recipe.utils.NavigationUtil;
import com.example.recipe.utils.TaskManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.recipe.utils.LoggerUtil.logger;

public abstract class BaseRecipeListing {
    protected abstract GridPane getMenuGrid();

    protected abstract VBox getProgressContainer();

    protected abstract MenuListingType getMenuListingType();

    protected abstract String getScreenId();

    private final MenuComponentStore menuComponentStore = MenuComponentStore.getInstance();

    private final TaskManager taskManager = TaskManager.getInstance();

    public void loadRecipeComponents(List<Recipe> data) {
        if (loadRecipeIfExists(data)) return;

        // todo () Here comes the logic for recipe loading if the recipe is not already loaded
        //  Load only the new ones if old ones already exist

        final Task<Void> loadingTask;
        if (isPreviousTaskRunningForCurrentScreen()) {
            loadingTask = taskManager.getTask(getScreenId());
            logger.error("Task is already running for this screen");
        } else {
            // Create a new FXMLLoader for each item
            // Update the cardBox with recipe data
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
                            menuComponentStore.addMenuComponent(cardBox, getMenuListingType());
                        }
                        updateMessage("Loading completed");
                    } catch (IOException e) {
                        updateMessage("Failed to load menu items");
                    }
                    return null;
                }
            };

            taskManager.startTask(loadingTask, getScreenId());
        }

        loadingTask.setOnSucceeded(event -> {
            updateGridPane(getCurrentRecipes());
        });
        loadingTask.setOnFailed(event -> {
            getProgressContainer().setVisible(false);
        });
    }

    private boolean isPreviousTaskRunningForCurrentScreen() {
        return taskManager.isTaskRunning(getScreenId());
    }

    private boolean loadRecipeIfExists(List<Recipe> data) {
        if (!getCurrentRecipes().isEmpty() && data.size() == getCurrentRecipes().size()) {
            updateGridPane(getCurrentRecipes());
            return true;
        }
        return false;
    }

    private List<Recipe> getNewRecipes(List<Recipe> data) {
        List<Recipe> newRecipes = new ArrayList<>();
        /*for (Recipe recipe : data) {
            if (!menuComponentStore.containsRecipe(recipe, getMenuListingType())) {
                newRecipes.add(recipe);
            }
        }*/
        return newRecipes;
    }

    private List<VBox> getCurrentRecipes() {
        return menuComponentStore.getMenuComponents(getMenuListingType());
    }

    // Method to update VBox with recipe data
    private void updateCardBoxWithRecipe(VBox cardBox, Recipe recipe) {
        VBox menuItemCard = (VBox) cardBox.lookup("#menuItemCard");
        menuItemCard.setOnMouseClicked(this::navigateToDetail);
        Label titleLabel = (Label) cardBox.lookup("#recipeTitle");
        Text description = (Text) cardBox.lookup("#textDescription");
        ImageView imageView = (ImageView) cardBox.lookup("#profileImage");
        titleLabel.setText(recipe.getTitle());
        description.setText(recipe.getDescription());
        imageView.setImage(new Image("file:" + recipe.getImage(), 200, 200, true, true));
    }

    private void navigateToDetail(MouseEvent event) {
        NavigationUtil.insertChild("recipe-details-view.fxml");
    }

    public void updateGridPane(List<VBox> menuItems) {
        Platform.runLater(() -> {
            getProgressContainer().setVisible(false);
            getMenuGrid().getChildren().clear();
            for (int i = 0; i < menuItems.size(); i++) {
                getMenuGrid().add(menuItems.get(i), i % 3, i / 3);
                GridPane.setMargin(menuItems.get(i), new Insets(10));
            }
        });
    }
}
