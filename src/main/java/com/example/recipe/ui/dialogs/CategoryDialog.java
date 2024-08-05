package com.example.recipe.ui.dialogs;

import com.example.recipe.domain.recipe.Category;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryDialog extends Stage {

    private final List<CheckBox> categoryCheckBoxes;
    private final List<Category> categories;

    public CategoryDialog(String title, List<Category> selectedCategories, ArrayList<Category> categories, AlertCallback<List<Category>> callback) {
        this.categories = categories;
        setTitle(title);
        initModality(Modality.APPLICATION_MODAL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(25);
        gridPane.setVgap(25);
        categoryCheckBoxes = categories.stream()
                .map(category -> {
                    CheckBox box = new CheckBox(category.getCategoryName());
                    for (Category selectedCategory : selectedCategories) {
                        if (selectedCategory.getCategoryId().equals(category.getCategoryId())) {
                            box.setSelected(true);
                            break;
                        }
                    }
                    return box;
                })
                .toList();

        for (int i = 0; i < categoryCheckBoxes.size(); i++) {
            gridPane.add(categoryCheckBoxes.get(i), i % 4, i / 4);
        }

        Button doneButton = new Button("Done");
        doneButton.setOnAction(e -> {
            callback.onAlertResponse(getSelectedCategories());
            close();
        });
        doneButton.setStyle("-fx-background-color: #3e8ee4; -fx-text-fill: #ffffff; -fx-border-radius: 5px; -fx-pref-width: 100");

        VBox layout = new VBox(40);
        layout.getChildren().addAll(gridPane, doneButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        this.setMinHeight(200);
        this.setMinWidth(400);

        Scene scene = new Scene(layout);
        setScene(scene);
    }

    public List<Category> getSelectedCategories() {
        return categoryCheckBoxes.stream()
                .filter(CheckBox::isSelected)
                .map(checkBox -> categories.get(categoryCheckBoxes.indexOf(checkBox)))
                .collect(Collectors.toList());
    }
}