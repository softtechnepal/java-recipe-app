package com.example.recipe.ui.dialogs;

import com.example.recipe.domain.recipe.Steps;
import com.example.recipe.utils.DialogUtil;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddStepDialog extends Stage {

    private final TextField tfStepName;
    private final TextArea tfStepDescription;
    private final GlobalCallBack<Steps> callback;

    public AddStepDialog(String title, Steps step, GlobalCallBack<Steps> callback) {
        this.callback = callback;
        setTitle(title);
        initModality(Modality.APPLICATION_MODAL);

        Label nameLabel = new Label("Step Name:");
        tfStepName = new TextField(step != null ? step.getStepName() : "");
        tfStepName.setPromptText("Step 1 | Step 2 | Step 3");
        Label quantityLabel = new Label("Step Description: ");
        tfStepDescription = new TextArea(step != null ? step.getStepDescription() : "");
        tfStepDescription.setPromptText("Description of the step");
        tfStepDescription.setStyle("-fx-pref-height: 120; -fx-alignment: top-left;");
        tfStepDescription.setWrapText(true);
        Button addButton = new Button("Add Step");
        addButton.setOnAction(e -> onAddStep());
        addButton.setStyle("-fx-background-color: #3e8ee4; -fx-text-fill: #ffffff; -fx-border-radius: 8px; -fx-pref-width: 80");

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> close());
        cancelButton.setStyle("-fx-background-color: #da6767; -fx-text-fill: #ffffff; -fx-border-radius: 8px; -fx-pref-width: 80");
        HBox hLayout = new HBox(10);
        hLayout.getChildren().addAll(cancelButton, addButton);
        hLayout.setStyle("-fx-padding: 10; -fx-alignment: center-right;");
        VBox layout = new VBox(10);
        layout.getChildren().addAll(nameLabel, tfStepName, quantityLabel, tfStepDescription, hLayout);
        layout.setStyle("-fx-padding: 10; -fx-alignment: center-left;");

        this.setMinHeight(300);
        this.setMinWidth(400);
        Scene scene = new Scene(layout);
//        scene.getStylesheets().add("/com/example/recipe/css/main.css");
        setScene(scene);
    }

    private void onAddStep() {
        String name = tfStepName.getText();
        if (name.isEmpty()) {
            DialogUtil.showErrorDialog("Error", "Step name cannot be empty");
            return;
        }
        String description = tfStepDescription.getText();
        if (description.isEmpty()) {
            DialogUtil.showErrorDialog("Error", "Step description cannot be empty");
            return;
        }

        Steps ingredient = new Steps(null, null, name, description, null);
        callback.onAlertResponse(ingredient);
        close();
    }
}