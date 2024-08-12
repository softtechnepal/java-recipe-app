package com.example.recipe.ui.dialogs;

import com.example.recipe.domain.recipe.Review;
import com.example.recipe.services.user.UserRecipeService;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class ReviewDialog extends Stage {
    private long recipeId;
    private UserRecipeService recipeService;

    public ReviewDialog(long recipeId, UserRecipeService recipeService) {
        this.recipeId = recipeId;
        this.recipeService = recipeService;
        setTitle("Review");
        initModality(Modality.APPLICATION_MODAL);
        fetchReviews();
    }

    private void fetchReviews() {
        recipeService.getRecipeReview(recipeId, result -> {
            if (result.isSuccess()) {
                successUI(result.getData());
            } else {
                errorUI(result.getMessage());
            }
        });
    }

    private void successUI(List<Review> data) {
        VBox layout = new VBox(40);
        Label label = new Label("Review");
        Button doneButton = new Button("Close");
        doneButton.setOnAction(e -> close());

        for (Review review : data) {
            Label reviewLabel = new Label(review.getReview());
            layout.getChildren().add(reviewLabel);
        }

    }

    private VBox reviewItem(Review review) {
        VBox layout = new VBox(12);
        Label reviewLabel = new Label(review.getReview());
        layout.getChildren().add(reviewLabel);
        return layout;
    }

    private void errorUI(String message) {
        VBox layout = new VBox(40);
        Label label = new Label(message);
        Button doneButton = new Button("Close");
        doneButton.setOnAction(e -> close());
        this.setMinHeight(300);
        this.setMinWidth(500);
        layout.getChildren().addAll(label, doneButton);
        Scene scene = new Scene(layout);
        this.setScene(scene);
    }
}