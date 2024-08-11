package com.example.recipe.utils;

import javafx.scene.control.Label;

import java.util.List;

public class ValidationUtil {

    public static boolean validateListings(List<?> list, Label fieldName, String title) {
        if (list == null || list.isEmpty()) {
            ViewUtil.setTextAndVisibility(fieldName, title + " should be added.", true);
            return false;
        }
        ViewUtil.setVisibility(fieldName, false);
        return true;
    }

    public static boolean validateString(String value, Label fieldName, String title) {
        if (value == null || value.trim().isEmpty()) {
            ViewUtil.setTextAndVisibility(fieldName, title + " should be added.", true);
            return false;
        }
        ViewUtil.setVisibility(fieldName, false);
        return true;
    }

    public static boolean validateInt(String value, Label fieldName, String title) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            ViewUtil.setTextAndVisibility(fieldName, title + " should be valid number", true);
            return false;
        }
        ViewUtil.setVisibility(fieldName, false);
        return true;
    }

    public static boolean validateDouble(String value, Label fieldName, String title) {
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            ViewUtil.setTextAndVisibility(fieldName, title + " should be valid number", true);
            return false;
        }
        ViewUtil.setVisibility(fieldName, false);
        return true;
    }
}
