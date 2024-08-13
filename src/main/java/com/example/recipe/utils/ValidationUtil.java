package com.example.recipe.utils;

import javafx.scene.control.Label;

import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

import static com.example.recipe.utils.LoggerUtil.logger;

public class ValidationUtil {

    public static boolean isValidWebURL(String url, Label fieldName, String title) {
        if (url == null || url.trim().isEmpty()) {
            return true;
        }
        String REGEX = "^(https?|ftp):\\/\\/([a-zA-Z0-9\\-\\.]+)(:\\d+)?(\\/[^\\s]*)?(\\?[^\\s]*)?(#[^\\s]*)?$";
        logger.info("URL: {}", url);
        final Pattern URL_PATTERN = Pattern.compile(REGEX);
        boolean isValid = URL_PATTERN.matcher(url.trim()).matches();
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
