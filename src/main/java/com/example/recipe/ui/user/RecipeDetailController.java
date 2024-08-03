package com.example.recipe.ui.user;

import com.example.recipe.utils.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class RecipeDetailController {
    @FXML
    public Button btnGoBack;

    @FXML
    private void initialize() {
        btnGoBack.setOnAction(event -> {
            NavigationUtil.insertChild("recipe-view.fxml");
        });
    }
}
