package com.example.recipe.ui.user;

import com.example.recipe.domain.recipe.Recipe;
import com.example.recipe.utils.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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

    @FXML
    public void initialize() {
        root.getProperties().put("controller", this);
    }

    public void setData(Recipe recipe) {
        this.recipe = recipe;
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
        NavigationUtil.insertChild("recipe-details-view.fxml");
    }
}
