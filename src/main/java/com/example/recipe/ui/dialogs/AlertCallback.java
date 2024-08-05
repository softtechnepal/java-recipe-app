package com.example.recipe.ui.dialogs;

public interface AlertCallback<T> {
    void onAlertResponse(T data);
}