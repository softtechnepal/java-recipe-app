package com.example.recipe;

import com.example.recipe.utils.NavigationUtil;
import com.example.recipe.utils.PrimaryStageUtil;
import javafx.application.Application;
import javafx.stage.Stage;

public class RecipeApplication extends Application {

    @Override
    public void start(Stage stage) {
        PrimaryStageUtil.setPrimaryStage(stage);
        NavigationUtil.navigateTo("login-view.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}