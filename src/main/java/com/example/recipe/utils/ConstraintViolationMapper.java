package com.example.recipe.utils;

import java.util.HashMap;
import java.util.Map;

public class ConstraintViolationMapper {
    private static final Map<String, String> constraintMessages = new HashMap<>();

    static {
        constraintMessages.put("ck_users_email_validate", "Invalid email format.");
        constraintMessages.put("ck_users_phone_validate", "Invalid phone format.");
    }

    public static String getUserFriendlyMessage(String constraintName) {
        return constraintMessages.getOrDefault(constraintName, "An error occurred.");
    }
}