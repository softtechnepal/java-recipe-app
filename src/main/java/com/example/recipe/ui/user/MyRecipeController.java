package com.example.recipe.ui.user;

import com.example.recipe.domain.enums.MenuListingType;
import com.example.recipe.services.BaseRecipeListing;
import com.example.recipe.services.UserDetailStore;
import com.example.recipe.services.user.UserRecipeService;
import com.example.recipe.utils.NavigationUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
    @FXML
    public TextField searchField;

    private static final UserRecipeService userRecipeService = new UserRecipeService();

    @FXML
    public void initialize() {
        btnAddRecipe.setOnAction(this::onAddRecipe);
        fetchRecipes();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchRecipes(newValue);
        });
    }

    private void onAddRecipe(ActionEvent actionEvent) {
        AddRecipeController.navigateToAddRecipe();
    }

    private void fetchRecipes() {
        Platform.runLater(() -> progressContainer.setVisible(true));
        userRecipeService.getRecipeByUserId(UserDetailStore.getInstance().getUserId(), response -> {
            if (response.isSuccess()) {
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

    @Override
    protected String getScreenId() {
        return this.getClass().getSimpleName();
    }
}