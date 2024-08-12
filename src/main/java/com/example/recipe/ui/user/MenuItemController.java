package com.example.recipe.ui.user;

import com.example.recipe.Constants;
import com.example.recipe.domain.common.RefreshCallback;
import com.example.recipe.domain.enums.MenuListingType;
import com.example.recipe.domain.recipe.Recipe;
import com.example.recipe.services.user.UserRecipeService;
import com.example.recipe.utils.DialogUtil;
import com.example.recipe.utils.ImageUtil;
import com.example.recipe.utils.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class MenuItemController {
    @FXML
    public ImageView recipeImage;
    @FXML
    public Label recipeTitle;
    @FXML
    public Label textDescription;
    @FXML
    public Label rating;
    @FXML
    public VBox root;
    @FXML
    public ImageView savedImage;
    @FXML
    public ImageView reviewImage;

    private Recipe recipe;
    private UserRecipeService userRecipeService;
    private RefreshCallback refreshCallback;

    @FXML
    public void initialize() {
        root.getProperties().put("controller", this);
    }

    public void setData(Recipe recipe, UserRecipeService userRecipeService, MenuListingType menuListingType) {
        this.recipe = recipe;
        this.userRecipeService = userRecipeService;
        if (menuListingType == MenuListingType.MY_RECIPE) {
            savedImage.setVisible(false);
            reviewImage.setVisible(false);
        }
        loadData();
    }

    public void setData(Recipe recipe, UserRecipeService userRecipeService, RefreshCallback callback) {
        this.refreshCallback = callback;
        this.recipe = recipe;
        this.userRecipeService = userRecipeService;
        loadData();
    }

    private void loadData() {
        recipeTitle.setText(recipe.getTitle());
        textDescription.setText(recipe.getDescription());
        ImageUtil.loadImageAsync(recipe.getImage(), recipeImage);
        if (recipe.isSaved()) {
            ImageUtil.loadImageAsync("file:src/main/resources/assets/ic_like_filled.png", savedImage);
        } else {
            ImageUtil.loadImageAsync("file:src/main/resources/assets/ic_like.png", savedImage);
        }
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void navigateToDetail(MouseEvent event) {
        navigateToNextPage();
    }

    public void navigateToNextPage() {
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.recipeParamId, recipe.getRecipeId());
        NavigationUtil.insertChild("recipe-details-view.fxml", params);
    }

    public void onSave(MouseEvent mouseEvent) {
        userRecipeService.addToFavourite(recipe.getRecipeId(), response -> {
            if (!response.isSuccess()) {
                DialogUtil.showErrorDialog("Error", response.getMessage());
                return;
            }
            if (!response.getData()) {
                savedImage.setImage(new Image("file:src/main/resources/assets/ic_like.png"));
            } else {
                savedImage.setImage(new Image("file:src/main/resources/assets/ic_like_filled.png"));
            }

            if (refreshCallback != null) {
                refreshCallback.refresh();
            }
        });
        mouseEvent.consume();
    }

    public void review(MouseEvent mouseEvent) {
        navigateToNextPage();
    }
}
