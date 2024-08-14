// src/main/java/com/example/recipe/ui/user/DashboardController.java
package com.example.recipe.ui.user;

import com.example.recipe.components.CustomMenuItem;
import com.example.recipe.services.MenuComponentStore;
import com.example.recipe.services.UserDetailStore;
import com.example.recipe.ui.common.authentication.LoginController;
import com.example.recipe.utils.*;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.List;

public class DashboardController {
    @FXML
    public HBox hBoxContainer;
    @FXML
    public CustomMenuItem profileMenuItem;
    @FXML
    public CustomMenuItem recipeMenuItem;
    @FXML
    public CustomMenuItem myRecipeMenuItem;
    @FXML
    public CustomMenuItem logoutMenuItem;
    @FXML
    public CustomMenuItem savedRecipeMenuItem;

    private CustomMenuItem activeMenuItem;

    @FXML
    public ImageView imageView;


    @FXML
    private void initialize() {
        ImageUtil.loadImageAsync("src/main/resources/assets/logo.png", imageView);

        SingletonObjects.getInstance().setMainBox(hBoxContainer);

        List<CustomMenuItem> menuItems = List.of(recipeMenuItem, myRecipeMenuItem, savedRecipeMenuItem, profileMenuItem);

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
            NavigationUtil.clearCache();
            UserDetailStore.getInstance().clear();
            MenuComponentStore.getInstance().clearAllMenuComponents();
            TaskManager.getInstance().cancelAllTasks();
            NavigationUtil.navigateTo(LoginController.LOGIN_ROUTE);
        }

    }
}