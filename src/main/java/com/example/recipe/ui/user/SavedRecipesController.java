package com.example.recipe.ui.user;

import com.example.recipe.domain.enums.MenuListingType;
import com.example.recipe.services.BaseRecipeListing;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import static com.example.recipe.utils.LoggerUtil.logger;

public class SavedRecipesController extends BaseRecipeListing {
    @FXML
    public GridPane menuGrid;
    @FXML
    public VBox progressContainer;

    @FXML
    public void initialize() {
        fetchSavedRecipes();
    }

    private void fetchSavedRecipes() {
        Platform.runLater(() -> progressContainer.setVisible(true));
        userRecipeService.getFavoriteRecipes(response -> {
            if (response.isSuccess()) {
                if (!response.getData().isEmpty()) {
                    loadRecipeComponents(response.getData());
                } else {
                    Platform.runLater(() -> progressContainer.setVisible(false));
                    logger.info("No saved recipes found");
                }
            } else {
                logger.error("Failed to fetch saved recipes {}", response.getMessage());
            }
        });
    }

    @Override
    protected GridPane getMenuGrid() {
        return menuGrid;
    }

    @Override
    protected VBox getProgressContainer() {
        return progressContainer;
    }

    @Override
    protected MenuListingType getMenuListingType() {
        return MenuListingType.FAVOURITE_RECIPE;
    }

    @Override
    protected String getScreenId() {
        return this.getClass().getSimpleName();
    }
}
