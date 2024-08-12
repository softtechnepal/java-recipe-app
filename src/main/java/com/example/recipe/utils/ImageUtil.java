package com.example.recipe.utils;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static com.example.recipe.utils.LoggerUtil.logger;

public class ImageUtil {
    public static String copyImageToDbImages(File sourceFile) throws IOException {
        Path destinationDir = Path.of("dbimages");
        if (!Files.exists(destinationDir)) {
            Files.createDirectories(destinationDir);
        }

        // Generate new file name
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int randomNum = new Random().nextInt(1000);
        String newFileName = timestamp + "_" + randomNum + getFileExtension(sourceFile.getName());

        Path destinationFile = destinationDir.resolve(newFileName);
        Files.copy(sourceFile.toPath(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
        return destinationFile.toString();
    }

    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }


    public static void loadImageAsync(String imageUrl, ImageView imageView){
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    URL url = new URL(imageUrl);
                    Image image = new Image(url.openStream());
                    Platform.runLater(() -> imageView.setImage(image));
                } catch (IOException e) {
                    logger.error("Failed to load image {}", e.getMessage());
                }
                return null;
            }
        };
        new Thread(task).start();
    }
}
