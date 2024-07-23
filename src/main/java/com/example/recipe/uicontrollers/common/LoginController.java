package com.example.recipe.uicontrollers.common;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoginController {
    @FXML
    public Label forgotPasswordLabel;
    @FXML
    public Label registerLabel;
    @FXML
    private ImageView logoImageView;
    @FXML
    private Label titleLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    public void initialize() {
        // Load the logo image here
        Image logoImage = new Image("file:src/main/resources/assets/app_logo.png");
        logoImageView.setImage(logoImage);

        // Add event handlers for buttons here
        loginButton.setOnAction(event -> {
            // Handle login logic
        });
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        // Validate username and password
        // Perform login logic
    }

    @FXML
    private void handleRegister() {
        // Navigate to registration screen
    }

    @FXML
    private void handleForgotPassword() {

    }
}