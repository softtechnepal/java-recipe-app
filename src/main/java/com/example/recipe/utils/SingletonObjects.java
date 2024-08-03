package com.example.recipe.utils;

import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SingletonObjects {
    // Private constructor to prevent instantiation
    private SingletonObjects() {
    }

    // Static inner class for lazy initialization
    private static class SingletonHelper {
        private static final SingletonObjects INSTANCE = new SingletonObjects();
    }

    // Public method to provide access to the singleton instance
    public static SingletonObjects getInstance() {
        return SingletonHelper.INSTANCE;
    }

    // Encapsulated fields
    private Stage primaryStage;
    private HBox mainBox;

    // Getter and setter for primaryStage
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    // Getter and setter for mainBox
    public HBox getMainBox() {
        return mainBox;
    }

    public void setMainBox(HBox box) {
        this.mainBox = box;
    }
}