package com.example.recipe.ui.common.authentication;

import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.domain.enums.UserStatus;
import com.example.recipe.domain.request.LoginRequest;
import com.example.recipe.domain.response.LoginResponse;
import com.example.recipe.services.AuthenticationService;
import com.example.recipe.services.UserDetailStore;
import com.example.recipe.ui.dialogs.EmailVerificationDialog;
import com.example.recipe.ui.dialogs.UpdatePasswordDialog;
import com.example.recipe.utils.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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
    private final AuthenticationService authenticationService = new AuthenticationService();

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
            authenticationService.authenticate(loginRequest, this::handleLoginResponse);
        }
    }

    private void handleLoginResponse(DbResponse<LoginResponse> response) {
        LoggerUtil.logger.error("Login response: {}", response instanceof DbResponse.Success ? "Success" : "Failure");
        if (response.isSuccess()) {
            UserDetailStore userDetailStore = UserDetailStore.getInstance();
            userDetailStore.setUserEmail(response.getData().getEmail());
            userDetailStore.setUserId(response.getData().getId());
            userDetailStore.setUserName(response.getData().getUsername());
            if (response.getData().isAdmin()) {
                NavigationUtil.navigateTo("admin/base-view.fxml");
                return;
            }
            if (response.getData().getStatus().equalsIgnoreCase(UserStatus.DISABLED.name())) {
                DialogUtil.showErrorDialog("Login Failed", "Your account is disabled. Please contact the administrator.");
                return;
            }
            SingletonUser.getInstance().setLoginResponse(response.getData());
            NavigationUtil.navigateTo("dashboard-view.fxml");
        } else {
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
        EmailVerificationDialog dialog = new EmailVerificationDialog(
                "Forgot Password",
                "Enter your email address",
                "Email",
                "Verify my email",
                this::handleEmailVerification
        );
        dialog.showAndWait();
    }

    private void handleEmailVerification(EmailVerificationDialog.DialogResponse data) {
        authenticationService.validateEmail(data.getData(), (response -> {
            if (response.isSuccess()) {
                showOtpDialog(data.getData(), data.getStage());
            } else {
                DialogUtil.showErrorDialog("Error", response.getMessage());
            }
        }));
    }

    private void showOtpDialog(String email, Stage stage) {
        EmailVerificationDialog dialog = new EmailVerificationDialog(
                "OTP Verification",
                "Enter the OTP sent to your email",
                "OTP",
                "Verify OTP",
                data -> {
                    if (authenticationService.isValidOtp(data.getData())) {
                        showChangePassword(email);
                        stage.close();
                        data.getStage().close();
                    } else {
                        DialogUtil.showErrorDialog("Error", "Invalid OTP");
                    }
                }
        );
        dialog.showAndWait();
    }

    private void showChangePassword(String email) {
        var dialog = new UpdatePasswordDialog(email, authenticationService, (value) -> {
            DialogUtil.showInfoDialog("Success", "Password updated successfully");
        });
        dialog.showAndWait();
    }

    public void handleContinueAsGuest(MouseEvent mouseEvent) {
        NavigationUtil.navigateTo("dashboard-view.fxml");
    }
}