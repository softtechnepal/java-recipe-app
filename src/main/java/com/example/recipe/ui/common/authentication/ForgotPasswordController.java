package com.example.recipe.ui.common.authentication;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;

public class ForgotPasswordController {

    @FXML
    public PasswordField confirmPasswordField;
    @FXML
    public Label confirmPasswordErrorLabel;

    @FXML
    public Button changePassword;

    public void initialize() {
        // Add event handlers for buttons here
        changePassword.setOnAction(event -> {
        });
    }

    public void handleLogin(MouseEvent mouseEvent) {

    }
}
