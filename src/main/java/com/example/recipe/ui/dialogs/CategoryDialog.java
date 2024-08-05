package com.example.recipe.ui.dialogs;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class CategoryDialog extends Stage {

    private final List<CheckBox> categoryCheckBoxes;

    public CategoryDialog(String title, List<String> categories) {
        setTitle(title);
        initModality(Modality.APPLICATION_MODAL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(25);
        gridPane.setVgap(25);
        categoryCheckBoxes = categories.stream()
                .map(CheckBox::new)
                .toList();

        for (int i = 0; i < categoryCheckBoxes.size(); i++) {
            gridPane.add(categoryCheckBoxes.get(i), i % 4, i / 4);
        }

        Button doneButton = new Button("Done");
        doneButton.setOnAction(e -> close());
        doneButton.setStyle("-fx-background-color: #3e8ee4; -fx-text-fill: #ffffff; -fx-border-radius: 5px; -fx-pref-width: 100");

        VBox layout = new VBox(40);
        layout.getChildren().addAll(gridPane, doneButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        this.setMinHeight(200);
        this.setMinWidth(400);

        Scene scene = new Scene(layout);
        setScene(scene);
    }

    public List<String> getSelectedCategories() {
        return categoryCheckBoxes.stream()
                .filter(CheckBox::isSelected)
                .map(CheckBox::getText)
                .toList();
    }
}