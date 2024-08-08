package com.example.recipe.domain;

import com.example.recipe.ui.user.MenuItemController;
import javafx.scene.layout.VBox;

import java.util.List;

public class UiModel {
    private MenuItemController menuItemController;
    private VBox vBoxes;

    public UiModel(MenuItemController menuItemController, VBox vBoxes) {
        this.menuItemController = menuItemController;
        this.vBoxes = vBoxes;
    }

    public MenuItemController getMenuItemController() {
        return menuItemController;
    }

    public void setMenuItemController(MenuItemController menuItemController) {
        this.menuItemController = menuItemController;
    }

    public VBox getvBoxes() {
        return vBoxes;
    }

    public void setvBoxes(VBox vBoxes) {
        this.vBoxes = vBoxes;
    }
}
