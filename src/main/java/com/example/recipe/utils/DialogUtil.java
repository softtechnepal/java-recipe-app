package com.example.recipe.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DialogUtil {
    public static void showDialog(AlertType alertType,String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}