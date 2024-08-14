package com.example.recipe.ui.dialogs;

import com.example.recipe.utils.DialogUtil;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class EmailVerificationDialog extends Stage {

    private final GlobalCallBack<DialogResponse> alertCallback;

    public EmailVerificationDialog(
            String title,
            String label,
            String prompt,
            String bntLabel,
            GlobalCallBack<DialogResponse> alertCallback
    ) {
        this.alertCallback = alertCallback;
        setTitle(title);
        initModality(Modality.APPLICATION_MODAL);
        initializeUI(label, prompt, bntLabel);
        this.centerOnScreen();
    }

    private void initializeUI(String label, String prompt, String btnLabel) {
        VBox layout = new VBox(20);
        layout.setStyle("-fx-padding: 20px;");
        layout.setAlignment(Pos.CENTER);

        Label emailLabel = new Label(label);
        TextField emailField = new TextField();
        emailField.setPromptText(prompt);

        Button sendButton = new Button(btnLabel);
        sendButton.setOnAction(e -> {
            String email = emailField.getText();
            if (email.isEmpty() || email.isBlank()) {
                DialogUtil.showErrorDialog("Error", "Field cannot be empty");
                return;
            }
            alertCallback.onAlertResponse(new DialogResponse(this, email));
        });

        layout.getChildren().addAll(emailLabel, emailField, sendButton);

        Scene scene = new Scene(layout, 400, 200);
        setScene(scene);
    }

    public static class DialogResponse {
        private Stage stage;
        private String data;

        public DialogResponse(Stage stage, String data) {
            this.stage = stage;
            this.data = data;
        }

        public Stage getStage() {
            return stage;
        }

        public void setStage(Stage stage) {
            this.stage = stage;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
