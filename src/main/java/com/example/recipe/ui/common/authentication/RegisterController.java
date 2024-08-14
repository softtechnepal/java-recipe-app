package com.example.recipe.ui.common.authentication;

import com.example.recipe.domain.request.UserRequest;
import com.example.recipe.services.AuthenticationService;
import com.example.recipe.utils.DialogUtil;
import com.example.recipe.utils.NavigationUtil;
import com.example.recipe.utils.ViewUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.regex.Pattern;

public class RegisterController {
    @FXML
    public ImageView logoImageView;
    @FXML
    public Button registerButton;
    @FXML
    public PasswordField confirmPasswordField;
    @FXML
    public Label confirmPasswordErrorLabel;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label passwordErrorLabel;
    @FXML
    public Label emailErrorLabel;
    @FXML
    public TextField emailField;
    @FXML
    public Label usernameErrorLabel;
    @FXML
    public TextField usernameField;
    @FXML
    public Label lastNameErrorLabel;
    @FXML
    public TextField lastNameField;
    @FXML
    public Label firstNameErrorLabel;
    @FXML
    public TextField firstNameField;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public void initialize() {
        Image logoImage = new Image("file:src/main/resources/assets/logo.png");
        logoImageView.setImage(logoImage);
        registerButton.setOnAction(event -> {
            handleRegister();
        });
    }

    private void handleRegister() {
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        UserRequest request = new UserRequest(email, username, password, firstName, lastName, confirmPassword);
        if (!validateRegistration(request)) {
            return;
        }
        AuthenticationService authenticationService = new AuthenticationService();
        authenticationService.register(request, data -> {
            if (data.isSuccess()) {
                DialogUtil.showInfoDialog("Success", data.getMessage());
                NavigationUtil.navigateTo("login-view.fxml");
            } else {
                DialogUtil.showErrorDialog("Error", data.getMessage());
            }
        });

    }

    private boolean validateRegistration(UserRequest request) {
        boolean isValid = true;

        if (request.getFirstName().isBlank()) {
            ViewUtil.setTextAndVisibility(firstNameErrorLabel, "First name cannot be empty", true);
            isValid = false;
        } else {
            ViewUtil.setTextAndVisibility(firstNameErrorLabel, "", false);
        }

        if (request.getLastName().isBlank()) {
            ViewUtil.setTextAndVisibility(lastNameErrorLabel, "Last name cannot be empty", true);
            isValid = false;
        } else {
            ViewUtil.setTextAndVisibility(lastNameErrorLabel, "", false);
        }

        if (request.getUsername().isBlank()) {
            ViewUtil.setTextAndVisibility(usernameErrorLabel, "Username cannot be empty", true);
            isValid = false;
        } else {
            ViewUtil.setTextAndVisibility(usernameErrorLabel, "", false);
        }

        if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            ViewUtil.setTextAndVisibility(emailErrorLabel, "Invalid email format", true);
            isValid = false;
        } else {
            ViewUtil.setTextAndVisibility(emailErrorLabel, "", false);
        }

        if (request.getPassword().isEmpty()) {
            ViewUtil.setTextAndVisibility(passwordErrorLabel, "Password cannot be empty", true);
            isValid = false;
        } else {
            ViewUtil.setTextAndVisibility(passwordErrorLabel, "", false);
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            ViewUtil.setTextAndVisibility(confirmPasswordErrorLabel, "Passwords do not match", true);
            isValid = false;
        } else {
            ViewUtil.setTextAndVisibility(confirmPasswordErrorLabel, "", false);
        }

        return isValid;
    }

    public void handleLogin(MouseEvent mouseEvent) {
        NavigationUtil.navigateTo("login-view.fxml");
    }

    public void handleContinueAsGuest(MouseEvent mouseEvent) {
        NavigationUtil.navigateTo("dashboard-view.fxml");
    }
}
