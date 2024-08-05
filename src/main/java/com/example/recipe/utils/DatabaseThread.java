package com.example.recipe.utils;

import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.domain.common.DatabaseCallback;
import javafx.concurrent.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import static com.example.recipe.utils.LoggerUtil.logger;

public class DatabaseThread {
    // Create a cached thread pool to manage background threads
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    // Generic method to run a database operation on a background thread
    public static <T> void runDataOperation(Supplier<DbResponse<T>> operation, DatabaseCallback<T> callback) {
        // Create a Task to perform the database operation
        Task<DbResponse<T>> task = new Task<>() {
            @Override
            protected DbResponse<T> call() {
                return operation.get();
            }
        };

        // Set the callback for when the task succeeds
        task.setOnSucceeded(event -> {
            logger.info("Task succeeded {}", task.getValue());
            callback.onDbResponse(task.getValue());
        });

        // Set the callback for when the task fails
        task.setOnFailed(event -> {
            logger.error("Task failed {}", task.getException().getLocalizedMessage());
            callback.onDbResponse(new DbResponse.Failure<T>(task.getException().getLocalizedMessage()));
        });

        // Submit the task to the executor service to run it in a background thread
        executorService.submit(task);
    }
}