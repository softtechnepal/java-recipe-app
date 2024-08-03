package com.example.recipe;

import com.example.recipe.utils.NavigationUtil;
import com.example.recipe.utils.SingletonObjects;
import javafx.application.Application;
import javafx.stage.Stage;

public class RecipeApplication extends Application {

    @Override
    public void start(Stage stage) {
        SingletonObjects.getInstance().setPrimaryStage(stage);
        NavigationUtil.navigateTo("login-view.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}