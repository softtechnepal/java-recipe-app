package com.example.recipe.ui.user;

import com.example.recipe.Constants;
import com.example.recipe.domain.recipe.*;
import com.example.recipe.services.UserDetailStore;
import com.example.recipe.services.user.UserRecipeService;
import com.example.recipe.ui.dialogs.AddReviewDialog;
import com.example.recipe.ui.dialogs.ReviewListingDialog;
import com.example.recipe.utils.*;
//import com.example.recipe.utils.TextToSpeech;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.recipe.utils.LoggerUtil.logger;

public class RecipeDetailController {

    private static final Logger log = LoggerFactory.getLogger(RecipeDetailController.class);
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
    @FXML
    public Label lbPreTime;
    @FXML
    public Label lbServings;
    @FXML
    public ImageView profileImage;
    private ImageView playIcon;
    private ImageView pauseIcon;

    public static void navigate(Map<String, Long> params) {
        NavigationUtil.insertChild("recipe-details-view.fxml", params);
    }

    private Long recipeId;
    private final UserRecipeService userRecipeService = new UserRecipeService();
    private Recipe currentRecipe;
    Task<Void> voicePlayerTask;

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
                        } else {
                            addReview.setVisible(true);
                            addReview.setManaged(true);
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

        if (data.getUser().getProfilePicture() != null) {
            ImageUtil.loadImageAsync(data.getUser().getProfilePicture(), profileImage);
        }

        // Set Recipe Create Date
        recipeCreateDate.setText(data.getCreatedAt().toString());

        // Set Recipe Preparation Time
        lbPreTime.setText("Preparation Time: " + data.getPrepTime() + " minutes");

        // Set Recipe Servings
        lbServings.setText("Can Servings: " + data.getTotalServings());

        // Set Recipe Image
        ImageUtil.loadImageAsync(data.getImage(), recipeImage);

        // Set Recipe By Name
        recipeByName.setText(data.getUser().getFullName());

        // Set Recipe Warnings
        if (data.getWarnings() == null || data.getWarnings().isEmpty()) {
            recipeWarnings.setText("No warnings");
        } else {
            recipeWarnings.setText(data.getWarnings());
        }

        loadSteps(data.getSteps());
        loadIngredients(data.getIngredients());
        loadCategories(data.getCategory());
        loadNutritionalInformation(data.getNutritionalInformation());
    }

    private void loadSteps(List<Steps> steps) {
        steps.sort(Comparator.comparingInt(Steps::getStepOrder));
        this.recipeSteps.getChildren().clear();
        for (Steps step : steps) {
            HBox container = new HBox();
            container.setAlignment(Pos.CENTER_LEFT);

            Label stepLabel = new Label(step.getStepName());
            stepLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px");
            Text stepDescription = new Text(step.getStepDescription());
            stepDescription.setWrappingWidth(790);
            stepDescription.setStyle("-fx-font-weight: normal; -fx-font-size: 16px; -fx-text-fill: #333333;");
            Separator separator = new Separator();
            container.getChildren().addAll(stepLabel, playerControls(step.getStepDescription()));

            this.recipeSteps.getChildren().addAll(container, stepDescription, separator);
        }
    }

    private HBox playerControls(String description) {
        HBox playerControls = new HBox();
        HBox.setHgrow(playerControls, Priority.ALWAYS);
        playerControls.setAlignment(Pos.CENTER_RIGHT);
        playIcon = getIcon("src/main/resources/assets/ic_play.png");

        pauseIcon = getIcon("src/main/resources/assets/ic_stop.png");
        ViewUtil.setVisibility(pauseIcon, false);

        playIcon.setOnMouseClicked(mouseEvent -> {
            ViewUtil.setVisibility(playIcon, false);
            ViewUtil.setVisibility(pauseIcon, true);
            //startSpeaking(description);
        });

        pauseIcon.setOnMouseClicked(mouseEvent -> {
            ViewUtil.setVisibility(playIcon, true);
            ViewUtil.setVisibility(pauseIcon, false);
            stopSpeaking();
        });

        playerControls.getChildren().addAll(playIcon, pauseIcon);
        return playerControls;
    }

    private ImageView getIcon(String iconPath) {
        ImageView imageView = new ImageView();
        ImageUtil.loadImageAsync(iconPath, imageView);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        imageView.setPreserveRatio(true);
        imageView.setCache(true);
        imageView.setStyle("-fx-cursor: hand; -fx-padding: 5px");
        return imageView;
    }

    private void startSpeaking(String description) {
        if (voicePlayerTask != null) {
            voicePlayerTask.cancel();
        }
        voicePlayerTask = new Task<>() {
            @Override
            protected Void call() {
                TextToSpeech.speak(description);
                return null;
            }
        };

        new Thread(voicePlayerTask).start();

        voicePlayerTask.setOnSucceeded(event -> {
            logger.info("Task completed");
            if (playIcon == null || pauseIcon == null) {
                return;
            }
            ViewUtil.setVisibility(playIcon, true);
            ViewUtil.setVisibility(pauseIcon, false);
        });

        voicePlayerTask.setOnCancelled(event -> {
            logger.info("Task cancelled");
            TextToSpeech.stopSpeaking();
            if (playIcon == null || pauseIcon == null) {
                return;
            }
            ViewUtil.setVisibility(playIcon, true);
            ViewUtil.setVisibility(pauseIcon, false);
        });
    }

    private void stopSpeaking() {
        if (voicePlayerTask != null) {
            voicePlayerTask.cancel();
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

    public void onViewReviews(MouseEvent mouseEvent) {
        userRecipeService.getRecipeReview(recipeId, response -> {
            if (response.isSuccess()) {
                if (response.getData() != null) {
                    if (response.getData().isEmpty()) {
                        DialogUtil.showErrorDialog("Error", "No reviews found");
                    } else {
                        var dialog = new ReviewListingDialog(response.getData(), userRecipeService);
                        dialog.showAndWait();
                        fetchReview();
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

    public void onWatchVideo(MouseEvent mouseEvent) {
        if (currentRecipe.getVideoUrl() == null && currentRecipe.getVideoUrl().isEmpty()) {
            DialogUtil.showErrorDialog("Error", "No video found for this recipe");
            return;
        }
        openUrl(currentRecipe.getVideoUrl());
    }

    private void openUrl(String url) {
        try {
            URI uri = new URI(url);
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(uri);
            }
        } catch (Exception e) {
            logger.error("Error: ", e);
            DialogUtil.showErrorDialog("Error", "Failed to open URL");
        }
    }

    public void onShareRecipe(MouseEvent mouseEvent) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem gmailItem = getMenuItemGmail();

        MenuItem facebookItem = new MenuItem("Share on Facebook");
        facebookItem.setOnAction(e -> {
            if (currentRecipe.getVideoUrl() == null || currentRecipe.getVideoUrl().isEmpty()) {
                DialogUtil.showErrorDialog("Error", "No video found to share for this recipe.");
                return;
            }
            String url = "https://www.facebook.com/sharer/sharer.php?u=" + URLEncoder.encode(currentRecipe.getVideoUrl(), StandardCharsets.UTF_8);
            openUrl(url);
        });

        contextMenu.getItems().addAll(gmailItem, facebookItem);

        contextMenu.show((Label) mouseEvent.getSource(), mouseEvent.getScreenX(), mouseEvent.getScreenY());
    }

    private MenuItem getMenuItemGmail() {
        MenuItem gmailItem = new MenuItem("Share via Gmail");
        gmailItem.setOnAction(e -> {
            String subject = URLEncoder.encode("Check out this recipe", StandardCharsets.UTF_8);
            String body = shareRecipeBody();
            String url = "https://mail.google.com/mail/?view=cm&fs=1&to=&su=" + subject + "&body=" + body + "&ui=2&tf=1";
            openUrl(url);
        });
        return gmailItem;
    }

    private String shareRecipeBody() {
        String stepsFormatted = currentRecipe.getSteps().stream()
                .map(step -> step.getStepName() + ":\n" + step.getStepDescription())
                .collect(Collectors.joining("\n\n"));

        return URLEncoder.encode(
                currentRecipe.getTitle() + " - " + currentRecipe.getDescription() + "\n\n" +
                        "Steps:\n" + stepsFormatted + "\n\nWatch Video: " + currentRecipe.getVideoUrl(),
                StandardCharsets.UTF_8
        );
    }
}
