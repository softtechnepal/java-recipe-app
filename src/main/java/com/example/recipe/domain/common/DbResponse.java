package com.example.recipe.domain.common;

public sealed class DbResponse<T> permits DbResponse.Failure, DbResponse.Loading, DbResponse.Success {
    private final String message;
    private final T data;
    private final boolean success;

    public DbResponse(String message, T data, boolean success) {
        this.message = message;
        this.data = data;
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public static <T> DbResponse<T> loading() {
        return new DbResponse.Loading<T>();
    }

    public static <T> DbResponse<T> success(String message, T data) {
        return new DbResponse.Success<T>(message, data);
    }

    public static <T> DbResponse<T> failure(String message) {
        return new DbResponse.Failure<T>(message);
    }

    public static final class Loading<T> extends DbResponse<T> {
        public Loading() {
            super("Loading", null, false);
        }
    }

    public static final class Success<T> extends DbResponse<T> {
        public Success(String message, T data) {
            super(message, data, true);
        }
    }

    public static final class Failure<T> extends DbResponse<T> {
        public Failure(String message) {
            super(message, null, false);
        }
    }
}