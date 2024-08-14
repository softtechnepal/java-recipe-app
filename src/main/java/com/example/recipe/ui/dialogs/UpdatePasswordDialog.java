package com.example.recipe.ui.dialogs;

import com.example.recipe.services.AuthenticationService;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;

import static com.example.recipe.utils.LoggerUtil.logger;

public class UpdatePasswordDialog extends Dialog<Void> {
    private final String email;
    private final AuthenticationService authenticationService;
    private final AlertCallback<Void> callback;
    private final PasswordField passwordField;
    private final PasswordField confirmPasswordField;
    private final Label passwordErrorLabel;
    private final Label confirmPasswordErrorLabel;

    public UpdatePasswordDialog(String email, AuthenticationService authenticationService, AlertCallback<Void> callback) {
        this.callback = callback;
        this.authenticationService = authenticationService;
        this.email = email;
        setTitle("Update Password");
        initStyle(StageStyle.UTILITY);

        // Create the password fields and error labels
        passwordField = new PasswordField();
        confirmPasswordField = new PasswordField();
        passwordErrorLabel = new Label();
        confirmPasswordErrorLabel = new Label();

        // Set error label styles
        passwordErrorLabel.setStyle("-fx-text-fill: red;");
        confirmPasswordErrorLabel.setStyle("-fx-text-fill: red;");

        // Create the grid pane and add components
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("New Password:"), 0, 0);
        grid.add(passwordField, 1, 0);
        grid.add(passwordErrorLabel, 1, 1);

        grid.add(new Label("Confirm Password:"), 0, 2);
        grid.add(confirmPasswordField, 1, 2);
        grid.add(confirmPasswordErrorLabel, 1, 3);

        // Add buttons
        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // Add grid to dialog pane
        getDialogPane().setContent(grid);

        // Handle button actions
        final Button updateButton = (Button) getDialogPane().lookupButton(updateButtonType);
        updateButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            logger.info("Validate {}", validatePassword());
            if (validatePassword()) {
                updatePassword(passwordField.getText(), event);
            } else {
                event.consume();
            }
        });

        // Request focus on the password field by default
        passwordField.requestFocus();
    }

    private void updatePassword(String password, ActionEvent event) {
        authenticationService.updatePassword(email, password, response -> {
            if (response.isSuccess()) {
                callback.onAlertResponse(null);
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update password").showAndWait();
            }
        });
    }


    private boolean validatePassword() {
        boolean valid = true;
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (password.isEmpty()) {
            passwordErrorLabel.setText("Password cannot be empty");
            valid = false;
        } else if (password.length() < 8) {
            passwordErrorLabel.setText("Password must be at least 8 characters");
            valid = false;
        } else {
            passwordErrorLabel.setText("");
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordErrorLabel.setText("Passwords do not match");
            valid = false;
        } else {
            confirmPasswordErrorLabel.setText("");
        }

        return valid;
    }
}
