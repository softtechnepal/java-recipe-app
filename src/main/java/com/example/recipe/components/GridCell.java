package com.example.recipe.components;

import com.example.recipe.domain.UiModel;
import com.example.recipe.domain.enums.MenuListingType;
import com.example.recipe.domain.recipe.Recipe;
import com.example.recipe.services.MenuComponentStore;
import com.example.recipe.services.user.UserRecipeService;
import com.example.recipe.ui.user.MenuItemController;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static com.example.recipe.utils.LoggerUtil.logger;

public class GridCell extends ListCell<Recipe> {
    private HBox hBox;
    private final UserRecipeService userRecipeService = new UserRecipeService();
    private final MenuComponentStore menuComponentStore = MenuComponentStore.getInstance();
    private Recipe currentItem;

    public GridCell() {
        hBox = new HBox();
        hBox.setSpacing(10);
        setGraphic(hBox);
    }

    @Override
    protected void updateItem(Recipe item, boolean empty) {
        super.updateItem(item, empty);

        if (item == currentItem && !empty) {
            return; // Avoid unnecessary updates
        }

        currentItem = item;
        hBox.getChildren().clear();

        if (item != null && !empty) {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    try {
                        VBox root = null;
                        var controllers = menuComponentStore.getAllMenuModel().stream().map(UiModel::getMenuItemController).toList();
                        for(int i = 0; i < controllers.size(); i++) {
                            if(controllers.get(i).getRecipe().getRecipeId() == item.getRecipeId()) {
                                root =  menuComponentStore.getAllMenuModel().get(i).getvBoxes();
                                break;
                            }
                        }
                        if(root != null) {
                            hBox.getChildren().add(root);
                            return null;
                        }
                        logger.error("Loading data using fxml");
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/recipe/menu-item.fxml"));
                        root = loader.load();
                        MenuItemController controller = loader.getController();
                        controller.setData(item, MenuListingType.MY_RECIPE);
                        // hBox.getChildren().add(root);
                        menuComponentStore.addMenuComponent(new UiModel(controller, root), MenuListingType.ALL_RECIPE);
                        hBox.getChildren().add(root);
                    } catch (Exception e) {
                        logger.error("Failed to load menu item {}", e.getMessage());
                    }
                    return null;
                }
            };

            new Thread(task).start();
            task.setOnSucceeded(event -> {
                setGraphic(hBox);
            });
            task.setOnFailed(event -> {
                logger.error("Failed to load menu item {}", task.getException().getMessage());
            });
        } else {
            logger.error("Item is null or empty");
            setGraphic(null);
        }
    }
}