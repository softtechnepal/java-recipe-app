package com.example.recipe.utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

import static com.example.recipe.Constants.SPEAKING_TASK_ID;
import static com.example.recipe.utils.LoggerUtil.logger;

public class NavigationUtil {
    private static final String FXML_PATH = "/com/example/recipe/";
    private static final String CSS_PATH = "/com/example/recipe/css/main.css";
    private static String currentChild = "";
    private static final Map<String, Object> currentParams = new HashMap<>();
    private static Stack<NavigationStack> navigationStack = new Stack<>();

    public static void navigateTo(String fxmlFile) {
        // Ensure the following block runs on the JavaFX Application Thread
        stopSpeaking();
        Platform.runLater(() -> {
                    try {
                        Stage primaryStage = SingletonObjects.getInstance().getPrimaryStage();
                        if (primaryStage == null) {
                            logger.error("Primary stage is not set");
                            return;
                        }

                        FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(FXML_PATH + fxmlFile));
                        Parent root = loader.load();

                        Scene scene = primaryStage.getScene();
                        if (scene == null) {
                            scene = new Scene(root);
                            primaryStage.setHeight(800);
                            primaryStage.setWidth(800);
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
        );
    }

    public static void insertChild(String fxmlFile) {
        if (fxmlFile.equals(currentChild)) {
            return;
        }
        navigationStack.push(new NavigationStack(currentChild, currentParams));
        replaceChild(fxmlFile);
    }

    public static void insertChild(String fxmlFile, Map<String, ?> params) {
        if (fxmlFile.equals(currentChild)) {
            return;
        }
        logger.info("Navigating back to {}", currentChild);
        logger.info("Params: {}", currentParams);
        Map<String, Object> paramsCopy = new HashMap<>(currentParams);
        navigationStack.push(new NavigationStack(currentChild, paramsCopy));
        synchronized (currentParams) {
            currentParams.clear();
            currentParams.putAll(params);
        }
        replaceChild(fxmlFile);
    }

    private static void replaceChild(String fxmlFile) {
        try {
            stopSpeaking();
            currentChild = fxmlFile;
            HBox hBoxContainer = SingletonObjects.getInstance().getMainBox();
            URL profile = NavigationUtil.class.getResource(FXML_PATH + fxmlFile);
            assert profile != null;
            Node pane = FXMLLoader.load(profile);
            if (hBoxContainer.getChildren().size() > 1) {
                hBoxContainer.getChildren().remove(1);
            }
            hBoxContainer.getChildren().add(pane);
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
        }
    }

    private static void stopSpeaking() {
        TaskManager.getInstance().stopTask(SPEAKING_TASK_ID);
    }

    public static Object getParam(String key) {
        synchronized (currentParams) {
            return currentParams.get(key);
        }
    }

    public static void refreshCurrentChild() {
        replaceChild(currentChild);
    }

    public static void goBack() {
        if (navigationStack.isEmpty()) {
            return;
        }
        NavigationStack currentStack = navigationStack.pop();;
        synchronized (currentParams) {
            currentParams.clear();
            currentParams.putAll(currentStack.getParams());
        }
        replaceChild(currentStack.getPath());
    }


    public static class NavigationStack {
        private final String path;
        private final Map<String, ?> params;

        public NavigationStack(String filePath, Map<String, ?> params) {
            this.path = filePath;
            this.params = params;
        }

        public String getPath() {
            return path;
        }

        public Map<String, ?> getParams() {
            return params;
        }
    }
}
