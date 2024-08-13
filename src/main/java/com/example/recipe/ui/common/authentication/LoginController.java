package com.example.recipe.ui.common.authentication;

import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.domain.request.LoginRequest;
import com.example.recipe.domain.response.LoginResponse;
import com.example.recipe.services.AuthenticationService;
import com.example.recipe.services.UserDetailStore;
import com.example.recipe.utils.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    public final static String LOGIN_ROUTE = "login-view.fxml";

    @FXML
    public void initialize() {
        // Load the logo image here
        Image logoImage = new Image("file:src/main/resources/assets/logo.png");
        logoImageView.setImage(logoImage);

        //Test Data
        usernameField.setText("prachan");
        passwordField.setText("Internal@1");

        // Add event handlers for buttons here
        loginButton.setOnAction(event -> handleLogin());
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        LoginRequest loginRequest = new LoginRequest(username, password);

        if (validateLogin(loginRequest)) {
            AuthenticationService authenticationService = new AuthenticationService();
            authenticationService.authenticate(loginRequest, this::handleLoginResponse);
        }
    }

    private void handleLoginResponse(DbResponse<LoginResponse> response) {
        LoggerUtil.logger.error("Login response: {}", response instanceof DbResponse.Success ? "Success" : "Failure");
        if (response instanceof DbResponse.Success) {
            UserDetailStore userDetailStore = UserDetailStore.getInstance();
            userDetailStore.setUserEmail(response.getData().getEmail());
            userDetailStore.setUserId(response.getData().getId());
            userDetailStore.setUserName(response.getData().getUsername());
            if (response.getData().isAdmin()) {
                NavigationUtil.navigateTo("admin/base-view.fxml");
                return;
            }
            SingletonUser.getInstance().setLoginResponse(response.getData());
            NavigationUtil.navigateTo("dashboard-view.fxml");
        } else if (response instanceof DbResponse.Failure) {
            DialogUtil.showErrorDialog("Login Failed", response.getMessage());
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
        NavigationUtil.navigateTo("register-view.fxml");
    }

    @FXML
    private void handleForgotPassword() {
        // Navigate to forgot password screen
    }

    public void handleContinueAsGuest() {
        // Navigate to home screen as guest
    }
}