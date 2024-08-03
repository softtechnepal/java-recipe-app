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
            Stage stage = StageManager.getPrimaryStage();
            if (stage == null) {
                logger.error("Primary stage is not set");
                return;
            }

            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(FXML_PATH + fxmlFile));
            Parent root = loader.load();

            Scene scene = stage.getScene();
            if (scene == null) {
                scene = new Scene(root);
                stage.setMinHeight(800);
                stage.setMinWidth(800);
            } else {
                scene.setRoot(root);
            }
            String css = Objects.requireNonNull(NavigationUtil.class.getResource(CSS_PATH)).toExternalForm();
            scene.getStylesheets().add(css);

            stage.setScene(scene);
            stage.setTitle("Recipe App");
            stage.show();
        } catch (IOException | NullPointerException e) {
            logger.error("Error navigating to {}", fxmlFile, e);
        }
    }
}
