package com.example.recipe.ui.user;

import com.example.recipe.Constants;
import com.example.recipe.domain.recipe.Recipe;
import com.example.recipe.services.user.UserRecipeService;
import com.example.recipe.utils.DialogUtil;
import com.example.recipe.utils.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

public class MenuItemController {
    @FXML
    public ImageView recipeImage;
    @FXML
    public Label recipeTitle;
    @FXML
    public Text textDescription;
    @FXML
    public Label rating;
    @FXML
    public VBox root;

    private Recipe recipe;
    private UserRecipeService userRecipeService;


    @FXML
    public void initialize() {
        root.getProperties().put("controller", this);
    }

    public void setData(Recipe recipe, UserRecipeService userRecipeService) {
        this.recipe = recipe;
        this.userRecipeService = userRecipeService;
        loadData();
    }

    private void loadData() {
        recipeTitle.setText(recipe.getTitle());
        textDescription.setText(recipe.getDescription());
        recipeImage.setImage(new Image("file:" + recipe.getImage(), 200, 200, true, true));
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
            DialogUtil.showInfoDialog("Success", response.getMessage());
            NavigationUtil.refreshCurrentChild();
        });
        mouseEvent.consume();
    }

    public void review(MouseEvent mouseEvent) {
        navigateToNextPage();
    }
}
