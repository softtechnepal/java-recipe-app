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
        LoginRequest loginRequest = new LoginRequest("admin123", "admin");
        var response = authenticationService.authenticate(loginRequest);
        welcomeText.setText(response.getMessage());


//        var registerRequest = new UserRequest("admin1@gmail.com", "admin1", "admin1", "admin", "admin", false);
//        var response = authenticationService.register(registerRequest);
//        welcomeText.setText(response.getMessage());
    }
}