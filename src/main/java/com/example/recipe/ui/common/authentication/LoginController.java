package com.example.recipe.ui.common.authentication;

import com.example.recipe.domain.request.LoginRequest;
import com.example.recipe.services.AuthenticationService;
import com.example.recipe.utils.NavigationUtil;
import com.example.recipe.utils.ViewUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import static com.example.recipe.utils.LoggerUtil.logger;

public class LoginController {
    @FXML
    public Label forgotPasswordLabel;
    @FXML
    public Label registerLabel;
    @FXML
    public Label guestLabel;
    @FXML
    public Label usernameErrorLabel;
    @FXML
    public Label passwordErrorLabel;
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
            handleLogin();
        });
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        LoginRequest loginRequest = new LoginRequest(username, password);
        if (validateLogin(loginRequest)) {
            AuthenticationService authenticationService = new AuthenticationService();
            var result = authenticationService.authenticate(loginRequest);
            if (result.isSuccess()) {
                ViewUtil.setVisibility(usernameErrorLabel, false);
                // Navigate to home screen
            } else {
                ViewUtil.setTextAndVisibility(passwordErrorLabel, result.getMessage(), true);
            }
        }
    }

    private boolean validateLogin(LoginRequest loginRequest) {
        if (loginRequest.getUsername().isBlank()) {
            ViewUtil.setTextAndVisibility(usernameErrorLabel, "Username cannot be empty", true);
            return false;
        }
        ViewUtil.setTextAndVisibility(usernameErrorLabel, "", false);

        if (loginRequest.getPassword().isEmpty()) {
            ViewUtil.setTextAndVisibility(passwordErrorLabel, "Password cannot be empty", true);
            return false;
        }
        ViewUtil.setTextAndVisibility(passwordErrorLabel, "", false);
        return true;
    }

    @FXML
    private void handleRegister() {
        // Navigate to registration screen
        logger.info("Navigating to register-view.fxml");
        NavigationUtil.navigateTo("register-view.fxml");
    }

    @FXML
    private void handleForgotPassword() {
        // Navigate to forgot password screen
    }

    public void handleContinueAsGuest(MouseEvent mouseEvent) {
        // Navigate to home screen as guest
    }
}