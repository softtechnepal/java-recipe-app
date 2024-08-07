package com.example.recipe.utils;

import javafx.concurrent.Task;

import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    private static final TaskManager instance = new TaskManager();
    private final Map<String, Task<Void>> tasks = new HashMap<>();

    private TaskManager() {
    }

    public static TaskManager getInstance() {
        return instance;
    }

    public void startTask(Task<Void> task, String screenId) {
        if (tasks.get(screenId) != null) {
            stopTask(screenId);
        }
        tasks.put(screenId, task);
        new Thread(task).start();
    }

    public boolean isTaskRunning(String taskId) {
        Task<Void> task = tasks.get(taskId);
        return task != null && task.isRunning();
    }

    public Task<Void> getTask(String taskId) {
        return tasks.get(taskId);
    }

    public void stopTask(String taskId) {
        Task<Void> task = tasks.get(taskId);
        if (task != null) {
            task.cancel();
            tasks.remove(taskId);
        }
    }
}