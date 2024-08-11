package com.example.recipe.ui.user;

import com.example.recipe.Constants;
import com.example.recipe.utils.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import static com.example.recipe.utils.LoggerUtil.logger;

public class RecipeDetailController {
    @FXML
    public Button btnGoBack;

    @FXML
    private void initialize() {
        var params = NavigationUtil.getParam(Constants.recipeParamId);
        logger.error("Recipe ID: {}", params);
    }
}
