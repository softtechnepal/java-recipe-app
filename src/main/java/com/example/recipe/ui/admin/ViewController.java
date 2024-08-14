// src/main/java/com/example/recipe/ui/user/DashboardController.java
package com.example.recipe.ui.admin;

import com.example.recipe.components.CustomMenuItem;
import com.example.recipe.services.UserDetailStore;
import com.example.recipe.utils.DialogUtil;
import com.example.recipe.utils.NavigationUtil;
import com.example.recipe.utils.SingletonObjects;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.List;

public class ViewController {
    @FXML
    public HBox hBoxContainer;
    @FXML
    public CustomMenuItem dashboardMenuItem;
    @FXML
    public CustomMenuItem categoryMenuitem;
    @FXML
    public CustomMenuItem recipeMenuItem;
    @FXML
    public CustomMenuItem userMenuItem;
    @FXML
    public CustomMenuItem logoutMenuItem;
    @FXML
    public ImageView imageView;
    @FXML
    public CustomMenuItem changePwdMenuItem;
    @FXML
    private CustomMenuItem activeMenuItem;

    @FXML
    private void initialize() {
        Image logoImage = new Image("file:src/main/resources/assets/logo.png");
        imageView.setImage(logoImage);
        SingletonObjects.getInstance().setMainBox(hBoxContainer);

        List<CustomMenuItem> menuItems = List.of(dashboardMenuItem, categoryMenuitem, recipeMenuItem, userMenuItem, changePwdMenuItem);

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

    public void onLogout(MouseEvent mouseEvent) {
        if (DialogUtil.showLogoutConfirmation()) {
            NavigationUtil.logout();
        }
    }
}