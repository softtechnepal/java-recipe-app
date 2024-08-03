package com.example.recipe.ui.user;

import com.example.recipe.utils.LoggerUtil;
import com.example.recipe.utils.NavigationUtil;
import com.example.recipe.utils.SingletonObjects;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.net.URL;

public class DashboardController {
    @FXML
    public Button btnRecipe;
    @FXML
    public Button btnProfile;
    @FXML
    public HBox hBoxContainer;
    @FXML
    public AnchorPane borderPane;

    @FXML
    private void initialize() {
        SingletonObjects.getInstance().setMainBox(hBoxContainer);
        btnProfile.setOnAction(this::onPressProfile);
        btnRecipe.setOnAction(this::onPress);
    }

    public void onPress(Event event) {
        NavigationUtil.insertChild("recipe-view.fxml");
    }

    public void onPressProfile(Event event) {
        NavigationUtil.insertChild("profile-view.fxml");
    }
}
