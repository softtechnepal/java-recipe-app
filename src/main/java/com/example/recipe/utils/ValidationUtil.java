package com.example.recipe.utils;

import javafx.scene.control.Label;

import java.net.URL;
import java.util.List;

public class ValidationUtil {

    public static boolean isValidWebURL(String url, Label fieldName, String title) {
        if (url == null || url.trim().isEmpty()) {
            return true;
        }
        String regex = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$";
        boolean isValid = url.matches(regex);
        if (!isValid) {
            ViewUtil.setTextAndVisibility(fieldName, title + " should be valid URL", true);
        } else {
            ViewUtil.setVisibility(fieldName, false);
        }
        return isValid;
    }

    public static boolean isValidList(List<?> list, Label fieldName, String title) {
        if (list == null || list.isEmpty()) {
            ViewUtil.setTextAndVisibility(fieldName, title + " should be added.", true);
            return false;
        }
        ViewUtil.setVisibility(fieldName, false);
        return true;
    }

    public static boolean isValidString(String value, Label fieldName, String title) {
        if (value == null || value.trim().isEmpty()) {
            ViewUtil.setTextAndVisibility(fieldName, title + " should be added.", true);
            return false;
        }
        ViewUtil.setVisibility(fieldName, false);
        return true;
    }

    public static boolean isValidInt(String value, Label fieldName, String title) {
        try {
            Integer.parseInt(value);
            ViewUtil.setVisibility(fieldName, false);
            return true;
        } catch (NumberFormatException e) {
            ViewUtil.setTextAndVisibility(fieldName, title + " should be valid number", true);
            return false;
        }
    }

    public static boolean isValidDouble(String value, Label fieldName, String title) {
        try {
            Double.parseDouble(value);
            ViewUtil.setVisibility(fieldName, false);
            return true;
        } catch (NumberFormatException e) {
            ViewUtil.setTextAndVisibility(fieldName, title + " should be valid number", true);
            return false;
        }
    }
}
