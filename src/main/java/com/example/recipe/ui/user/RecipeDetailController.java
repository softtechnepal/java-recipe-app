package com.example.recipe.ui.user;

import com.example.recipe.Constants;
import com.example.recipe.services.user.UserRecipeService;
import com.example.recipe.utils.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.Map;

import static com.example.recipe.utils.LoggerUtil.logger;

public class RecipeDetailController {

    public static void navigate(Map<String, Long> params) {
        NavigationUtil.insertChild("recipe-details-view.fxml", params);
    }

    private Long recipeId;
    private final UserRecipeService userRecipeService = new UserRecipeService();

    @FXML
    public Button btnGoBack;

    @FXML
    private void initialize() {
        recipeId = (Long) NavigationUtil.getParam(Constants.recipeParamId);
        getRecipeDetails();
    }

    private void getRecipeDetails() {
        userRecipeService.getRecipeDetailById(recipeId, response -> {
            if (response.isSuccess()) {
                logger.info("Recipe details fetched successfully {}", response.getMessage());
            } else {
                logger.error("{}", response.getMessage());
            }
        });
    }
}
