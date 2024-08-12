package com.example.recipe.ui.dialogs;

import com.example.recipe.domain.recipe.Review;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class ReviewListingDialog extends Stage {
    private final List<Review> reviews;

    public ReviewListingDialog(List<Review> reviews) {
        this.reviews = reviews;
        setTitle("Review Listing");
        initModality(Modality.APPLICATION_MODAL);
        initializeUI();
        this.centerOnScreen();
    }

    private void initializeUI() {
        VBox layout = new VBox(20);
        layout.setStyle("-fx-padding: 20px;");
        layout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Reviews");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
        layout.getChildren().add(titleLabel);

        if (reviews.isEmpty()) {
            Label noReviewLabel = new Label("No reviews found");
            layout.getChildren().add(noReviewLabel);
        } else {
            for (Review review : reviews) {
                VBox reviewItem = createReviewItem(review);
                layout.getChildren().add(reviewItem);
            }
        }

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> close());
        layout.getChildren().add(closeButton);

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        this.setMinHeight(200);
        this.setMaxWidth(500);
        this.setMaxHeight(500);
        this.setMinWidth(500);
        Scene scene = new Scene(scrollPane);
        setScene(scene);
    }

    private VBox createReviewItem(Review review) {
        VBox layout = new VBox(12);
        Label nameLabel = new Label(review.getUser().getFullName() + " (" + review.getRating() + " stars)");
        nameLabel.setStyle("-fx-font-weight: bold");
        Label reviewLabel = new Label(review.getReview());
        reviewLabel.setWrapText(true);
        reviewLabel.setMaxWidth(400);
        Separator separator = new Separator();
        layout.getChildren().addAll(nameLabel, reviewLabel, separator);
        return layout;
    }
}