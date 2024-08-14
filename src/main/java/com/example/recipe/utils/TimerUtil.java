package com.example.recipe.utils;

import com.example.recipe.ui.dialogs.GlobalCallBack;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.recipe.utils.LoggerUtil.logger;

public class TimerUtil {
    private static TimerUtil instance;
    private Timer timer;
    private int seconds;
    private long recipeId;
    private GlobalCallBack<String> timerCallBack;

    private TimerUtil() {
        // Private constructor to prevent instantiation
    }

    public static synchronized TimerUtil getInstance() {
        if (instance == null) {
            instance = new TimerUtil();
        }
        return instance;
    }

    public void start(int seconds, long recipeId, GlobalCallBack<String> callBack) {
        this.timerCallBack = callBack;
        this.recipeId = recipeId;
        this.seconds = seconds;
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (TimerUtil.this.seconds > 0) {
                    TimerUtil.this.seconds--;
                    logger.info("Remaining seconds: {}", TimerUtil.this.seconds);
                    timerCallBack.onAlertResponse(String.valueOf(TimerUtil.this.seconds));
                } else {
                    timer.cancel();
                    notifyUser();
                }
            }
        }, 0, 1000L);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void setCallback(GlobalCallBack<String> callBack) {
        this.timerCallBack = callBack;
    }

    public boolean isRunning(long recipeId) {
        return timer != null && this.recipeId == recipeId;
    }

    private void notifyUser() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cooking Timer");
            alert.setHeaderText(null);
            alert.setContentText("Time's up! Did you complete cooking?");
            alert.showAndWait();
        });
    }
}
