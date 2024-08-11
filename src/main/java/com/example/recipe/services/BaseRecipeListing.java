package com.example.recipe.services;

import com.example.recipe.domain.UiModel;
import com.example.recipe.domain.enums.MenuListingType;
import com.example.recipe.domain.recipe.Category;
import com.example.recipe.domain.recipe.Recipe;
import com.example.recipe.services.user.UserRecipeService;
import com.example.recipe.ui.user.MenuItemController;
import com.example.recipe.utils.TaskManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.recipe.utils.LoggerUtil.logger;

public abstract class BaseRecipeListing {
    private static final Logger log = LoggerFactory.getLogger(BaseRecipeListing.class);

    protected abstract GridPane getMenuGrid();

    protected abstract VBox getProgressContainer();

    protected abstract MenuListingType getMenuListingType();

    protected abstract String getScreenId();

    protected static final UserRecipeService userRecipeService = new UserRecipeService();

    private final MenuComponentStore menuComponentStore = MenuComponentStore.getInstance();

    private final TaskManager taskManager = TaskManager.getInstance();

    public void loadRecipeComponents(List<Recipe> data) {
        if (loadRecipeIfExists(data)) return;

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
                            MenuItemController controller = fxmlLoader.getController();
                            controller.setData(recipe, userRecipeService);
                            // Update the cardBox with recipe data
                            // updateCardBoxWithRecipe(cardBox, recipe);
                            menuComponentStore.addMenuComponent(new UiModel(controller, cardBox), getMenuListingType());
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

    public void addFilter(List<Category> categories) {
        if (categories.isEmpty()) {
            updateGridPane(getCurrentRecipes());
            return;
        }
        List<UiModel> filteredResult = menuComponentStore.getAllMenuModel().stream().filter(vBox -> {
            MenuItemController controller = vBox.getMenuItemController();
            List<Category> recipe = controller.getRecipe().getCategory();
            if (recipe == null || recipe.isEmpty()) {
                return false;
            }
            AtomicBoolean hasCategory = new AtomicBoolean(false);

            recipe.forEach(category -> {
                logger.error("Category{}", category.getCategoryName());
                if (categories.stream().map(Category::getCategoryName).toList().contains(category.getCategoryName())) {
                    hasCategory.set(true);
                }
            });
            return hasCategory.get();
        }).toList();

        updateGridPane(filteredResult.stream().map(UiModel::getvBoxes).toList());
    }

    protected void searchRecipes(String query) {
        List<UiModel> filteredResult = menuComponentStore.getAllMenuModel().stream().filter(vBox -> {
            MenuItemController controller = vBox.getMenuItemController();
            return controller.getRecipe().getTitle().toLowerCase().contains(query.toLowerCase());
        }).toList();

        updateGridPane(filteredResult.stream().map(UiModel::getvBoxes).toList());
    }

    private List<VBox> getCurrentRecipes() {
        return menuComponentStore.getMenuComponents(getMenuListingType());
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
