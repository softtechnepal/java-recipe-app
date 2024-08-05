package com.example.recipe.components;

import com.example.recipe.utils.LoggerUtil;
import com.example.recipe.utils.NavigationUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.Objects;

public class CustomMenuItem extends HBox {
    private final StringProperty navigateToPath = new SimpleStringProperty();
    private final StringProperty label = new SimpleStringProperty();
    private final StringProperty iconPath = new SimpleStringProperty();
    private final StringProperty activeIconPath = new SimpleStringProperty();


    private final ImageView menuIcon = new ImageView();
    private final Label menuLabel = new Label();

    public CustomMenuItem() {
        this.setAlignment(Pos.CENTER_LEFT);
        this.getStyleClass().add("custom-menu-item");

        this.getChildren().addAll(menuIcon, menuLabel);
        this.initialize();
    }

    @FXML
    private void initialize() {
        menuLabel.textProperty().bind(labelProperty());
        iconPath.addListener((observable, oldValue, newValue) -> {
            LoggerUtil.logger.error("Icon path: {}", newValue);
            loadImage(newValue);
        });
    }

    private void loadImage(String path) {
        if (path != null && !path.isEmpty()) {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/" + path)));
            menuIcon.setImage(image);
        }
    }

    public void activate() {
        this.getStyleClass().remove("inactive");
        this.getStyleClass().add("active");
        loadImage(activeIconPath.getValue());
        NavigationUtil.insertChild(getNavigateToPath());
    }

    public void deactivate() {
        this.getStyleClass().remove("active");
        this.getStyleClass().add("inactive");
        loadImage(iconPath.getValue());
    }

    // Getter for navigateTo
    public String getNavigateToPath() {
        return navigateToPath.get();
    }

    // Setter for navigateTo
    public void setNavigateToPath(String navigateToPath) {
        this.navigateToPath.set(navigateToPath);
    }

    // Getter for navigateTo
    public String getLabel() {
        return label.get();
    }

    // Setter for navigateTo
    public void setLabel(String navigateToPath) {
        this.label.set(navigateToPath);
    }

    public StringProperty labelProperty() {
        return label;
    }

    // Getter for iconPath
    public String getIconPath() {
        return iconPath.getValue();
    }

    // Setter for iconPath
    public void setIconPath(String iconPath) {
        this.iconPath.set(iconPath);
    }

    public StringProperty iconPathProperty() {
        return iconPath;
    }

    // Getter for activeIconPath
    public String getActiveIconPath() {
        return activeIconPath.getValue();
    }

    // Setter for activeIconPath
    public void setActiveIconPath(String activeIconPath) {
        this.activeIconPath.set(activeIconPath);
    }

    public StringProperty activeIconPathProperty() {
        return activeIconPath;
    }
}

