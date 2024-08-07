package com.example.recipe.ui.user;

import com.example.recipe.domain.enums.MenuListingType;
import com.example.recipe.services.BaseRecipeListing;
import com.example.recipe.services.user.UserRecipeService;
import com.example.recipe.utils.NavigationUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import static com.example.recipe.utils.LoggerUtil.logger;

public class MyRecipeController extends BaseRecipeListing {
    @FXML
    public GridPane menuGrid;
    @FXML
    public Button btnAddRecipe;
    @FXML
    public VBox progressContainer;

    private static final UserRecipeService userRecipeService = new UserRecipeService();

    @FXML
    public void initialize() {
        btnAddRecipe.setOnAction(this::onAddRecipe);
        fetchRecipes();
    }

    private void onAddRecipe(ActionEvent actionEvent) {
        NavigationUtil.insertChild("add-recipe-view.fxml");
    }

    private void fetchRecipes() {
        Platform.runLater(() -> progressContainer.setVisible(true));
        userRecipeService.getRecipeByUserId(107, response -> {
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
        return MenuListingType.MY_RECIPE;
    }
}