package com.example.recipe.ui.user;

import com.example.recipe.Constants;
import com.example.recipe.domain.recipe.*;
import com.example.recipe.services.user.UserRecipeService;
import com.example.recipe.utils.ImageUtil;
import com.example.recipe.utils.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Comparator;
import java.util.List;
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
    @FXML
    public ImageView starIcon;
    @FXML
    public Label viewRecipe;

    public static void navigate(Map<String, Long> params) {
        NavigationUtil.insertChild("recipe-details-view.fxml", params);
    }

    private Long recipeId;
    private final UserRecipeService userRecipeService = new UserRecipeService();

    @FXML
    private void initialize() {
        recipeId = (Long) NavigationUtil.getParam(Constants.recipeParamId);
        ImageUtil.loadImageAsync("src/main/resources/assets/ic_start.png", starIcon);
        fetchRecipeDetail();
        fetchReview();
    }

    private void fetchReview() {
        userRecipeService.getRecipeReview(recipeId, response -> {
            if (response.isSuccess()) {
                if (response.getData() != null) {
                    if (response.getData().isEmpty()) {
                        recipeReview.setText("");
                        recipeRating.setText("No ratings yet");
                        viewRecipe.setText("Add Review");
                        return;
                    }
                    recipeReview.setText("Reviews: " + response.getData().size());
                    recipeRating.setText("Rating: " + response.getData().stream().mapToDouble(Review::getRating).average().orElse(0));
                }
            } else {
                logger.error("{}", response.getMessage());
            }
        });
    }

    private void fetchRecipeDetail() {
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
        // Set Recipe Title
        recipeTitle.setText(data.getTitle());

        // Set Recipe Description
        recipeDescription.setText(data.getDescription());

        // Set Recipe Create Date
        recipeCreateDate.setText(data.getCreatedAt().toString());

        // Set Recipe Image
        ImageUtil.loadImageAsync(data.getImage(), recipeImage);

        // Set Recipe By Name
        recipeByName.setText(data.getUser().getFullName());

        // Set Recipe Warnings
        recipeWarnings.setText(data.getWarnings());

        loadSteps(data.getSteps());
        loadIngredients(data.getIngredients());
        loadCategories(data.getCategory());
        loadNutritionalInformation(data.getNutritionalInformation());
    }

    private void loadSteps(List<Steps> steps) {
        steps.sort(Comparator.comparingInt(Steps::getStepOrder));
        this.recipeSteps.getChildren().clear();
        for (Steps step : steps) {
            Label stepLabel = new Label(step.getStepName());
            stepLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px");
            Label stepDescription = new Label(step.getStepDescription());
            stepDescription.setStyle("-fx-font-weight: normal; -fx-font-size: 16px; -fx-text-fill: #333333");
            Separator separator = new Separator();
            this.recipeSteps.getChildren().addAll(stepLabel, stepDescription, separator);
        }
    }

    private void loadCategories(List<Category> categories) {
        this.categories.getChildren().clear();
        for (Category category : categories) {
            Label categoryLabel = getSideStyle("-  " + category.getCategoryName());
            this.categories.getChildren().add(categoryLabel);
        }
    }

    private void loadIngredients(List<Ingredient> ingredients) {
        this.ingredients.getChildren().clear();
        for (Ingredient ingredient : ingredients) {
            Label ingredientLabel = getSideStyle("-    " + ingredient.getIngredientName() + ": " + ingredient.getQuantity() + " " + ingredient.getUnit());
            this.ingredients.getChildren().add(ingredientLabel);
        }
    }

    private void loadNutritionalInformation(NutritionalInformation nutritionalInformation) {
        nutrition.getChildren().clear();
        if (nutritionalInformation != null) {
            Label caloriesLabel = getSideStyle("-  Calories: " + nutritionalInformation.getCalories());
            Label proteinLabel = getSideStyle("-  Protein: " + nutritionalInformation.getProtein() + "g");
            Label fatLabel = getSideStyle("-  Fat: " + nutritionalInformation.getFat() + "g");
            Label carbohydratesLabel = getSideStyle("-  Carbohydrates: " + nutritionalInformation.getCarbohydrates() + "g");
            nutrition.getChildren().addAll(caloriesLabel, proteinLabel, fatLabel, carbohydratesLabel);
        }
    }

    private Label getSideStyle(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: normal; -fx-font-size: 18px; -fx-text-fill: #333");
        return label;
    }

    public void onBackPressed(MouseEvent mouseEvent) {

    }
}
