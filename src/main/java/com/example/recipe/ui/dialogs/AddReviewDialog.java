package com.example.recipe.ui.dialogs;

import com.example.recipe.domain.common.RefreshCallback;
import com.example.recipe.domain.recipe.Review;
import com.example.recipe.services.user.UserRecipeService;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddReviewDialog extends Stage {
    private final long recipeId;
    private final UserRecipeService recipeService;
    private final RefreshCallback callback;
    private TextField ratingField;
    private TextArea commentArea;

    public AddReviewDialog(long recipeId, UserRecipeService recipeService, RefreshCallback callback) {
        this.callback = callback;
        this.recipeId = recipeId;
        this.recipeService = recipeService;
        setTitle("Add Review");
        initModality(Modality.APPLICATION_MODAL);
        initializeUI();
        this.centerOnScreen();
    }

    private void initializeUI() {
        VBox layout = new VBox(20);
        layout.setStyle("-fx-padding: 20px;");
        layout.setAlignment(Pos.CENTER);

        Label ratingLabel = new Label("Rating (1-5):");
        ratingField = new TextField();
        ratingField.setPromptText("Enter rating");

        Label commentLabel = new Label("Comment:");
        commentArea = new TextArea();
        commentArea.setPromptText("Write your comment here");

        Button addButton = new Button("Add Rating");
        addButton.setOnAction(e -> addRating());

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> close());

        layout.getChildren().addAll(ratingLabel, ratingField, commentLabel, commentArea, addButton, cancelButton);

        Scene scene = new Scene(layout, 400, 300);
        setScene(scene);
    }

    private void addRating() {
        try {
            int rating = Integer.parseInt(ratingField.getText());
            if (rating < 1 || rating > 5) {
                showAlert("Invalid Rating", "Rating must be between 1 and 5.");
                return;
            }
            String comment = commentArea.getText();
            Review review = new Review();
            review.setRating(rating);
            review.setReview(comment);
            recipeService.addReview(recipeId, review, result -> {
                if (result.isSuccess()) {
                    callback.refresh();
                    close();
                } else {
                    showAlert("Error", result.getMessage());
                }
            });
            close();
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number for the rating.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}