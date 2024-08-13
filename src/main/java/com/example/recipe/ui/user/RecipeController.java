package com.example.recipe.ui.user;

import com.example.recipe.domain.enums.MenuListingType;
import com.example.recipe.services.BaseRecipeListing;
import com.example.recipe.services.user.UserCategoryService;
import com.example.recipe.ui.dialogs.CategoryDialog;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import static com.example.recipe.utils.LoggerUtil.logger;


public class RecipeController extends BaseRecipeListing {
    @FXML
    public GridPane menuGrid;
    @FXML
    public GridPane categoryGrid;
    @FXML
    public VBox progressContainer;
    @FXML
    public Label navTitle;
    @FXML
    public TextField searchField;


    private final UserCategoryService userCategoryService = new UserCategoryService();

    @FXML
    public void initialize() {
        navTitle.requestFocus();
        fetchRecipes();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchRecipes(newValue);
        });
    }

    private void fetchRecipes() {
        Platform.runLater(() -> progressContainer.setVisible(true));
        userRecipeService.getAllRecipes(response -> {
            if (response.isSuccess()) {
                loadRecipeComponents(response.getData());
            } else {
                logger.error("Failed to fetch recipes {}", response.getMessage());
            }
        });
    }

    public void onFilterClicked(MouseEvent event) {
        userCategoryService.getAllCategories(response -> {
            if (response.isSuccess()) {
                userCategoryService.setCategories(response.getData());
                var dialog = new CategoryDialog(
                        "Add Categories",
                        userCategoryService.getSelectedCategories(),
                        response.getData(),
                        userCategoryService::setSelectedCategories);
                dialog.showAndWait();
                loadSelectedCategories();
            }
        });
    }

    private void loadSelectedCategories() {
        Platform.runLater(() -> {
            categoryGrid.getChildren().clear();
            for (int i = 0; i < userCategoryService.getSelectedCategories().size(); i++) {
                AnchorPane anchorPane = new AnchorPane();
                Label label = new Label(userCategoryService.getSelectedCategories().get(i).getCategoryName());
                anchorPane.getChildren().add(label);
                categoryGrid.add(anchorPane, i % 4, userCategoryService.getSelectedCategories().size() / 4);
            }
        });

        addFilter(userCategoryService.getSelectedCategories());
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

    @Override
    protected String getScreenId() {
        return this.getClass().getSimpleName();
    }
}
