package com.example.recipe.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

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
}
