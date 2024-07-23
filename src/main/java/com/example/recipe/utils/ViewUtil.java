package com.example.recipe.utils;

import javafx.scene.Node;
import javafx.scene.control.Label;

public class ViewUtil {
    public static void setVisibility(Node node, boolean isVisible) {
        node.setVisible(isVisible);
        node.setManaged(isVisible);
    }

    public static void setTextAndVisibility(Label node, String text, boolean isVisible) {
        setVisibility(node, isVisible);
        if (isVisible) {
            node.setText(text);
        }
    }
}
