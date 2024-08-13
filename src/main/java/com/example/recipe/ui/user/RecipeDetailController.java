package com.example.recipe.ui.user;

import com.example.recipe.Constants;
import com.example.recipe.domain.recipe.*;
import com.example.recipe.services.UserDetailStore;
import com.example.recipe.services.user.UserRecipeService;
import com.example.recipe.ui.dialogs.AddReviewDialog;
import com.example.recipe.ui.dialogs.ReviewListingDialog;
import com.example.recipe.utils.DialogUtil;
import com.example.recipe.utils.ImageUtil;
import com.example.recipe.utils.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Comparator;
import java.util.HashMap;
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
    @FXML
    public Label addReview;
    @FXML
    public Label editRecipe;
    @FXML
    public HBox viewReviewsContainer;
    @FXML
    public Label deleteRecipe;

    public static void navigate(Map<String, Long> params) {
        NavigationUtil.insertChild("recipe-details-view.fxml", params);
    }

    private Long recipeId;
    private final UserRecipeService userRecipeService = new UserRecipeService();
    private Recipe currentRecipe;

    @FXML
    private void initialize() {
        recipeId = (Long) NavigationUtil.getParam(Constants.RECIPE_ID_PARAM);
        ImageUtil.loadImageAsync("src/main/resources/assets/ic_start.png", starIcon);
        fetchRecipeDetail();
    }

    private void fetchReview() {
        userRecipeService.getRecipeReview(recipeId, response -> {
            if (response.isSuccess()) {
                if (response.getData() != null) {
                    if (response.getData().isEmpty()) {
                        recipeRating.setText("No ratings yet");
                        viewReviewsContainer.setVisible(false);
                        viewReviewsContainer.setManaged(false);
                    } else {
                        viewReviewsContainer.setVisible(true);
                        viewReviewsContainer.setManaged(true);
                    }
                    if (currentRecipe != null) {
                        var reviewedUsers = response.getData().stream().map(Review::getUser);
                        if (currentRecipe.getUser().getUserId() == UserDetailStore.getInstance().getUserId()) {
                            addReview.setVisible(false);
                            addReview.setManaged(false);
                            editRecipe.setVisible(true);
                            deleteRecipe.setVisible(true);
                        } else if (reviewedUsers.anyMatch(user -> user.getUserId() == UserDetailStore.getInstance().getUserId())) {
                            addReview.setVisible(false);
                            addReview.setManaged(false);
                        }
                    }

                    recipeReview.setText("Reviews: " + response.getData().size());
                    var avgRating = response.getData().stream().mapToDouble(Review::getRating).average().orElse(0);
                    recipeRating.setText(String.format("%.1f", avgRating) + " stars");
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
                    currentRecipe = response.getData();
                    loadUI(currentRecipe);
                    fetchReview();
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
            Text stepDescription = new Text(step.getStepDescription());
            stepDescription.setWrappingWidth(790);
            stepDescription.setStyle("-fx-font-weight: normal; -fx-font-size: 16px; -fx-text-fill: #333333;");
            Separator separator = new Separator();
            this.recipeSteps.getChildren().addAll(stepLabel, stepDescription, separator);
        }
    }

    private void loadCategories(List<Category> categories) {
        this.categories.getChildren().clear();
        for (Category category : categories) {
            Label categoryLabel = getBulletPoints(category.getCategoryName());
            this.categories.getChildren().add(categoryLabel);
        }
    }

    private void loadIngredients(List<Ingredient> ingredients) {
        this.ingredients.getChildren().clear();
        for (Ingredient ingredient : ingredients) {
            Label ingredientLabel = getBulletPoints(ingredient.getIngredientName() + ": " + ingredient.getQuantity() + " " + ingredient.getUnit());
            this.ingredients.getChildren().add(ingredientLabel);
        }
    }

    private void loadNutritionalInformation(NutritionalInformation nutritionalInformation) {
        nutrition.getChildren().clear();
        if (nutritionalInformation != null) {
            Label caloriesLabel = getBulletPoints("Calories: " + nutritionalInformation.getCalories());
            Label proteinLabel = getBulletPoints("Protein: " + nutritionalInformation.getProtein() + "g");
            Label fatLabel = getBulletPoints("Fat: " + nutritionalInformation.getFat() + "g");
            Label carbohydratesLabel = getBulletPoints("Carbohydrates: " + nutritionalInformation.getCarbohydrates() + "g");
            nutrition.getChildren().addAll(caloriesLabel, proteinLabel, fatLabel, carbohydratesLabel);
        }
    }

    private Label getBulletPoints(String text) {
        Label label = new Label("• " + text);
        label.setWrapText(true);
        label.setStyle("-fx-font-weight: normal; -fx-font-size: 14px; -fx-text-fill: #333; -fx-padding: 5px");
        return label;
    }

    public void onBackPressed(MouseEvent mouseEvent) {

    }

    public void openReviewDialog(MouseEvent mouseEvent) {
        userRecipeService.getRecipeReview(recipeId, response -> {
            if (response.isSuccess()) {
                if (response.getData() != null) {
                    if (response.getData().isEmpty()) {
                        DialogUtil.showErrorDialog("Error", "No reviews found");
                    } else {
                        var dialog = new ReviewListingDialog(response.getData());
                        dialog.showAndWait();
                    }
                }
            } else {
                DialogUtil.showErrorDialog("Error", response.getMessage());
            }
        });
    }

    public void addReview(MouseEvent mouseEvent) {
        var dialog = new AddReviewDialog(recipeId, userRecipeService, this::fetchReview);
        dialog.showAndWait();
    }

    public void onEditRecipe(MouseEvent mouseEvent) {
        logger.error("{}", currentRecipe);
        if (currentRecipe != null && currentRecipe.getUser().getUserId() == UserDetailStore.getInstance().getUserId()) {
            Map<String, Recipe> params = new HashMap<>();
            params.put(Constants.RECIPE_DETAIL_PARAM, currentRecipe);
            AddRecipeController.navigateToEditRecipe(params);
        } else {
            DialogUtil.showErrorDialog("Error", "You are not allowed to edit this recipe");
        }
    }

    public void onViewProfile(MouseEvent mouseEvent) {
        Map<String, Long> params = new HashMap<>();
        params.put(Constants.USER_ID_PARAM, currentRecipe.getUser().getUserId());
        OtherUserController.navigate(params);
    }

    public void onDeleteRecipe(MouseEvent mouseEvent) {
        if (currentRecipe.getUser().getUserId() == UserDetailStore.getInstance().getUserId()) {
            userRecipeService.deleteRecipe(recipeId, response -> {
                if (response.isSuccess()) {
                    DialogUtil.showInfoDialog("Success", "Recipe deleted successfully");
                    NavigationUtil.insertChild("recipe-view.fxml");
                } else {
                    DialogUtil.showErrorDialog("Error", response.getMessage());
                }
            });
        } else {
            DialogUtil.showErrorDialog("Error", "You are not allowed to delete this recipe");
        }
    }
}
