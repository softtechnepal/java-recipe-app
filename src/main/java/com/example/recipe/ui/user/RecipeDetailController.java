package com.example.recipe.ui.user;

import com.example.recipe.Constants;
import com.example.recipe.domain.recipe.Recipe;
import com.example.recipe.services.user.UserRecipeService;
import com.example.recipe.utils.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Map;

import static com.example.recipe.utils.LoggerUtil.logger;

public class RecipeDetailController {

    @FXML
    public Label recipeTitle;
    @FXML
    public Text recipeDescription;
    @FXML
    public Text recipeCreateDate;
    @FXML
    public ImageView recipeImage;
    @FXML
    public Label recipeByName;
    @FXML
    public VBox recipeByContainer;
    @FXML
    public Label recipeRating;
    @FXML
    public Label recipeReview;
    @FXML
    public Label recipeWarnings;
    @FXML
    public VBox recipeSteps;
    @FXML
    public VBox ingredients;
    @FXML
    public VBox categories;
    @FXML
    public VBox nutrition;

    public static void navigate(Map<String, Long> params) {
        NavigationUtil.insertChild("recipe-details-view.fxml", params);
    }

    private Long recipeId;
    private final UserRecipeService userRecipeService = new UserRecipeService();

    @FXML
    private void initialize() {
        recipeId = (Long) NavigationUtil.getParam(Constants.recipeParamId);
        getRecipeDetails();
    }

    private void getRecipeDetails() {
        userRecipeService.getRecipeDetailById(recipeId, response -> {
            if (response.isSuccess()) {
                if (response.getData() != null) {
                    loadUI(response.getData());
                }
            } else {
                logger.error("{}", response.getMessage());
            }
        });
    }

    private void loadUI(Recipe data) {

    }

    public void onBackPressed(MouseEvent mouseEvent) {

    }
}
