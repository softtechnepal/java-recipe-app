// src/main/java/com/example/recipe/ui/user/DashboardController.java
package com.example.recipe.ui.user;

import com.example.recipe.components.CustomMenuItem;
import com.example.recipe.utils.SingletonObjects;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

import java.util.List;

public class DashboardController {
    @FXML
    public HBox hBoxContainer;
    @FXML
    public CustomMenuItem profileMenuItem;
    @FXML
    public CustomMenuItem recipeMenuItem;

    private List<CustomMenuItem> menuItems;
    private CustomMenuItem activeMenuItem;

    @FXML
    private void initialize() {
        SingletonObjects.getInstance().setMainBox(hBoxContainer);

        menuItems = List.of(recipeMenuItem, profileMenuItem);

        for (CustomMenuItem menuItem : menuItems) {
            menuItem.deactivate();
            menuItem.setOnMouseClicked(event -> activateMenuItem(menuItem));
        }

        activateMenuItem(menuItems.get(0));
    }

    private void activateMenuItem(CustomMenuItem menuItem) {
        if (activeMenuItem != null) {
            activeMenuItem.deactivate();
        }
        menuItem.activate();
        activeMenuItem = menuItem;
    }
}