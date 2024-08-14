package com.example.recipe.ui.dialogs;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class EmailVerificationDialog extends Stage {
    public EmailVerificationDialog() {
        setTitle("Email Verification");
        initModality(Modality.APPLICATION_MODAL);
        initializeUI();
        this.centerOnScreen();
    }

    private void initializeUI() {
        VBox layout = new VBox(20);
        layout.setStyle("-fx-padding: 20px;");
        layout.setAlignment(Pos.CENTER);

        Label emailLabel = new Label("Enter your email address:");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter email address");

        Button sendButton = new Button("Send Verification Email");
        sendButton.setOnAction(e -> sendVerificationEmail(emailField.getText()));

        layout.getChildren().addAll(emailLabel, emailField, sendButton);

        Scene scene = new Scene(layout, 400, 200);
        setScene(scene);
    }

    private void sendVerificationEmail(String email) {
        // Send verification email logic here
        System.out.println("Sending verification email to: " + email);
        close();
    }
}
