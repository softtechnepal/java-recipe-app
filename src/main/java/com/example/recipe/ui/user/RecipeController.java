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
import javafx.scene.text.Text;

import static com.example.recipe.utils.LoggerUtil.logger;


public class RecipeController extends BaseRecipeListing {
    @FXML
    public GridPane menuGrid;
    @FXML
    public GridPane categoryGrid;
    @FXML
    public VBox progressContainer;
    @FXML
    public Text navTitle;
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
            var rowItems = 10;
            var categories = userCategoryService.getSelectedCategories();
            for (int i = 0; i < categories.size(); i++) {
                Label categoryLabel = new Label(categories.get(i).getCategoryName());
                categoryLabel.getStyleClass().add("grid-item");
                int row = i / rowItems;
                int col = i % rowItems;
                categoryGrid.add(categoryLabel, col, row);
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
