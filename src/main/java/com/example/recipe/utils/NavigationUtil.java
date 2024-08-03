package com.example.recipe.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static com.example.recipe.utils.LoggerUtil.logger;

public class NavigationUtil {
    private static final String FXML_PATH = "/com/example/recipe/";
    private static final String CSS_PATH = "/com/example/recipe/css/main.css";


    public static void navigateTo(String fxmlFile) {
        try {
            Stage primaryStage = PrimaryStageUtil.getPrimaryStage();
            if (primaryStage == null) {
                logger.error("Primary stage is not set");
                return;
            }

            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(FXML_PATH + fxmlFile));
            Parent root = loader.load();

            Scene scene = primaryStage.getScene();
            if (scene == null) {
                scene = new Scene(root);
                primaryStage.setMinHeight(800);
                primaryStage.setMinWidth(800);
            } else {
                scene.setRoot(root);
            }
            String css = Objects.requireNonNull(NavigationUtil.class.getResource(CSS_PATH)).toExternalForm();
            scene.getStylesheets().add(css);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Recipe App");
            primaryStage.show();
        } catch (IOException e) {
            logger.error("Error navigating to {}", fxmlFile, e);
        }
    }
}
