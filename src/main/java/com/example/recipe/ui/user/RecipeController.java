package com.example.recipe.ui.user;

import com.example.recipe.domain.enums.MenuListingType;
import com.example.recipe.services.BaseRecipeListing;
import com.example.recipe.services.user.UserRecipeService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import static com.example.recipe.utils.LoggerUtil.logger;


public class RecipeController extends BaseRecipeListing {
    @FXML
    public GridPane menuGrid;
    @FXML
    public VBox progressContainer;

    private static final UserRecipeService userRecipeService = new UserRecipeService();

    @FXML
    public void initialize() {

        fetchRecipes();
    }

    private void fetchRecipes() {
        Platform.runLater(() -> progressContainer.setVisible(true));
        userRecipeService.getAllRecipes(response -> {
            if (response.isSuccess()) {
                if (!response.getData().isEmpty())
                    loadRecipeComponents(response.getData());
            } else {
                logger.error("Failed to fetch recipes {}", response.getMessage());
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
        return MenuListingType.ALL_RECIPE;
    }
}
