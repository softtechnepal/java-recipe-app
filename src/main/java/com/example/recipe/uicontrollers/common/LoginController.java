package com.example.recipe.uicontrollers.common;

import com.example.recipe.domain.request.LoginRequest;
import com.example.recipe.services.AuthenticationService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LoginController {

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        AuthenticationService authenticationService = new AuthenticationService();
        LoginRequest loginRequest = new LoginRequest("admin", "admin");
        var response = authenticationService.authenticate(loginRequest);
        welcomeText.setText(response.getMessage());
    }
}