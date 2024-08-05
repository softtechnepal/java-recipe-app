package com.example.recipe.ui.dialogs;

import com.example.recipe.domain.recipe.Ingredient;
import com.example.recipe.utils.DialogUtil;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class IngredientDialog extends Stage {

    private final TextField tfIngredientName;
    private final TextField tfQuantity;
    private final TextField tfUnit;
    private final AlertCallback<Ingredient> callback;

    public IngredientDialog(String title, AlertCallback<Ingredient> callback) {
        this.callback = callback;
        setTitle(title);
        initModality(Modality.APPLICATION_MODAL);

        Label nameLabel = new Label("Ingredient Name:");
        tfIngredientName = new TextField();
        Label quantityLabel = new Label("Quantity:");
        tfQuantity = new TextField();
        Label unitLabel = new Label("Unit:");
        tfUnit = new TextField();

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> onAdd());
        addButton.setStyle("-fx-background-color: #3e8ee4; -fx-text-fill: #ffffff; -fx-border-radius: 5px; -fx-pref-width: 60");

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> close());
        cancelButton.setStyle("-fx-background-color: #da6767; -fx-text-fill: #ffffff; -fx-border-radius: 5px; -fx-pref-width: 60");
        HBox hLayout = new HBox(10);
        hLayout.getChildren().addAll(cancelButton, addButton);
        hLayout.setStyle("-fx-padding: 10; -fx-alignment: center-right;");
        VBox layout = new VBox(10);
        layout.getChildren().addAll(nameLabel, tfIngredientName, quantityLabel, tfQuantity, unitLabel, tfUnit, hLayout);
        layout.setStyle("-fx-padding: 10; -fx-alignment: center-left;");

        this.setMinHeight(300);
        this.setMinWidth(400);
        Scene scene = new Scene(layout);
//        scene.getStylesheets().add("/com/example/recipe/css/main.css");
        setScene(scene);
    }

    private void onAdd() {
        String name = tfIngredientName.getText();
        if (name.isEmpty()) {
            DialogUtil.showErrorDialog("Error", "Ingredient name cannot be empty");
            return;
        }

        Double quantity = null;
        if (!tfQuantity.getText().isEmpty()) {
            try {
                quantity = Double.parseDouble(tfQuantity.getText());
            } catch (NumberFormatException e) {
                DialogUtil.showErrorDialog("Error", "Quantity should be a valid number");
                return;
            }
        }

        String unit = tfUnit.getText();

        Ingredient ingredient = new Ingredient(null, null, name, quantity, unit, null);
        callback.onAlertResponse(ingredient);
        close();
    }
}