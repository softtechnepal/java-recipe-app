package com.example.recipe.ui.admin;

import com.example.recipe.services.admin.AdminUserService;
import com.example.recipe.domain.common.DbResponse;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import static com.example.recipe.utils.DialogUtil.showErrorDialog;
import static com.example.recipe.utils.DialogUtil.showInfoDialog;

public class AdminChangePasswordController {
    @FXML
    public PasswordField oldPasswordField;
    @FXML
    public Label oldPasswordErrorField;
    @FXML
    public PasswordField newPasswordField;
    @FXML
    public Label newPasswordErrorLabel;
    @FXML
    public PasswordField confirmPasswordField;
    @FXML
    public Label confirmPasswordErrorLabel;
    @FXML
    public Button changePwd;

    private final AdminUserService adminUserService = new AdminUserService();

    public void initialize() {
        // Initialize error labels to be invisible
        oldPasswordErrorField.setVisible(false);
        newPasswordErrorLabel.setVisible(false);
        confirmPasswordErrorLabel.setVisible(false);

        changePwd.setOnAction(event -> {
            handleChangePassword();
        });
    }

    private void handleChangePassword() {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!validatePasswords(oldPassword, newPassword, confirmPassword)) {
            return;
        }

        DbResponse<Void> response = adminUserService.changePassword(oldPassword, newPassword);
        if (response.isSuccess()) {
            showSuccess("Success", "Password changed successfully");
        } else {
            showError("Error", response.getMessage());
        }
    }

    private boolean validatePasswords(String oldPassword, String newPassword, String confirmPassword) {
        boolean isValid = true;

        if (oldPassword.isBlank()) {
            oldPasswordErrorField.setText("Old password cannot be empty");
            oldPasswordErrorField.setVisible(true);
            isValid = false;
        } else {
            oldPasswordErrorField.setVisible(false);
        }

        if (newPassword.isBlank()) {
            newPasswordErrorLabel.setText("New password cannot be empty");
            newPasswordErrorLabel.setVisible(true);
            isValid = false;
        } else {
            newPasswordErrorLabel.setVisible(false);
        }

        if (!newPassword.equals(confirmPassword)) {
            confirmPasswordErrorLabel.setText("Passwords do not match");
            confirmPasswordErrorLabel.setVisible(true);
            isValid = false;
        } else {
            confirmPasswordErrorLabel.setVisible(false);
        }

        return isValid;
    }

    private void showSuccess(String title, String message) {
        showInfoDialog(title, message);
    }

    private void showError(String title, String message) {
        showErrorDialog(title, message);
    }
}