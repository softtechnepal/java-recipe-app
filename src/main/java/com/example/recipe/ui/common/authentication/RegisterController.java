package com.example.recipe.ui.common.authentication;

import com.example.recipe.utils.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class RegisterController {
    @FXML
    public Button btnLogin;

    public void initialize() {
        btnLogin.setOnAction(event -> {
            // Navigate to login screen
            NavigationUtil.navigateTo("login-view.fxml");
        });
    }
}
