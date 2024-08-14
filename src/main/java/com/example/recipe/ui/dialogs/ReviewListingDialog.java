package com.example.recipe.ui.dialogs;

import com.example.recipe.domain.recipe.Review;
import com.example.recipe.services.UserDetailStore;
import com.example.recipe.services.user.UserRecipeService;
import com.example.recipe.utils.DialogUtil;
import com.example.recipe.utils.ImageUtil;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

import static com.example.recipe.utils.LoggerUtil.logger;

public class ReviewListingDialog extends Stage {
    private final List<Review> reviews;
    private final UserRecipeService userRecipeService;

    public ReviewListingDialog(List<Review> reviews, UserRecipeService userRecipeService) {
        this.reviews = reviews;
        this.userRecipeService = userRecipeService;
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

        for (Review review : reviews) {
            VBox reviewItem = createReviewItem(review);
            layout.getChildren().add(reviewItem);
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
        VBox root = new VBox(12);
        HBox parent = new HBox(20);
        ImageView imageView = new ImageView();
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setCache(true);
        imageView.setClip(ImageUtil.createCircle(50, 50));
        String profilePicture = review.getUser().getProfilePicture();
        if (profilePicture == null || profilePicture.isEmpty()) {
            profilePicture = "src/main/resources/assets/users.png";
        }
        ImageUtil.loadImageAsync(profilePicture, imageView);

        VBox layout = new VBox(2);
        Label nameLabel = new Label(review.getUser().getFullName());
        nameLabel.setStyle("-fx-font-weight: bold");

        Label rating = new Label(review.getRating() + " / 5");

        Label reviewLabel = new Label(review.getReview());
        reviewLabel.setWrapText(true);
        reviewLabel.setMaxWidth(400);
        reviewLabel.setStyle("-fx-font-size: 16px;");

        Separator separator = new Separator();
        layout.getChildren().addAll(nameLabel, rating, reviewLabel);

        HBox.setHgrow(layout, Priority.ALWAYS);
        HBox centerContainer = new HBox(layout);
        centerContainer.setAlignment(Pos.CENTER_RIGHT);

        parent.getChildren().addAll(imageView, layout);

        if (review.getUser().getUserId() == UserDetailStore.getInstance().getUserId()) {
            logger.error("User id: {} Review User id: {}", UserDetailStore.getInstance().getUserId(), review.getUser().getUserId());
            ImageView deleteImg = new ImageView();
            deleteImg.setFitWidth(20);
            deleteImg.setFitHeight(20);
            deleteImg.setCache(true);
            ImageUtil.loadImageAsync("src/main/resources/assets/ic_delete.png", deleteImg);
            HBox deleteContainer = new HBox(deleteImg);
            deleteContainer.setOnMouseClicked(e -> {
                onDeleteReview(review);
            });
            deleteContainer.setStyle("-fx-cursor: hand;");
            parent.getChildren().add(deleteContainer);
        }

        root.getChildren().addAll(parent, separator);
        return root;
    }


    private void onDeleteReview(Review review) {
        userRecipeService.deleteReview(review.getId(), (response -> {
            if (response.isSuccess()) {
                reviews.remove(review);
                initializeUI();
            } else {
                DialogUtil.showErrorDialog("Error", response.getMessage());
            }
        }));
    }
}