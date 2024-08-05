package com.example.recipe.domain.common;

public interface DatabaseCallback<T> {
    void onDbResponse(DbResponse<T> data);
}


