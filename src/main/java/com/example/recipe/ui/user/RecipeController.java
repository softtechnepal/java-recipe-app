package com.example.recipe.ui.user;

import com.example.recipe.utils.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class RecipeController {
    @FXML
    public Button btnDetails;

    @FXML
    private void initialize() {
        btnDetails.setOnAction(event -> {
            NavigationUtil.insertChild("recipe-details-view.fxml");
        });
    }
}
