package com.example.recipe.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.recipe.utils.LoggerUtil.logger;

public class NavigationUtil {

    public static void navigateTo(String fxmlFile) {
        try {
            Stage stage = StageManager.getPrimaryStage();
            if (stage == null) {
                logger.error("Primary stage is not set");
                return;
            }
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource("/com/example/recipe/" + fxmlFile));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Recipe App");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            logger.error("Error navigating to {}", fxmlFile, e);
        }
    }
}