package com.example.recipe.ui.user;

import com.example.recipe.services.UserService;
import com.example.recipe.utils.DialogUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class ChangePasswordController {
    @FXML
    private PasswordField recentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmNewPasswordField;

    @FXML
    private Button changePasswordButton;

    private UserService userService;
    private Stage dialogStage;

    @FXML
    public void initialize() {
        userService = new UserService();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleChangePassword(ActionEvent event) {
        String recentPassword = recentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmNewPassword = confirmNewPasswordField.getText();

        if (newPassword == null || newPassword.isEmpty() || !newPassword.equals(confirmNewPassword)) {
            DialogUtil.showErrorDialog("Validation Error", "New Password and Confirm New Password must match and cannot be empty.");
            return;
        }

        userService.changePassword(recentPassword, newPassword, (response) -> {
            if (response.isSuccess()) {
                DialogUtil.showInfoDialog("Success", response.getMessage());
                dialogStage.close();
            } else {
                DialogUtil.showErrorDialog("Error", response.getMessage());
            }
        });
    }
}
