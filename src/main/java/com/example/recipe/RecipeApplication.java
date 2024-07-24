package com.example.recipe;

import com.example.recipe.utils.NavigationUtil;
import com.example.recipe.utils.StageManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class RecipeApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        StageManager.setPrimaryStage(stage);
        NavigationUtil.navigateTo("login-view.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}