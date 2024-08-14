package com.example.recipe.utils;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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

        // Create a temporary file for the compressed image
        File tempCompressedFile = File.createTempFile("compressed_", getFileExtension(sourceFile.getName()));
        try {
            // Compress the image
            compressImage(sourceFile, tempCompressedFile, 0.25f); // Compress with 75% quality

            // Copy the compressed image to the destination
            Files.copy(tempCompressedFile.toPath(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            // Delete the temporary file
            tempCompressedFile.deleteOnExit();
        }

        return destinationFile.toString();
    }

    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }


    public static void loadImageAsync(String imageUrl, ImageView imageView) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    URL url = new URL("file:" + imageUrl);
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


    public static void compressImage(File inputFile, File outputFile, float quality) throws IOException {
        BufferedImage image = ImageIO.read(inputFile);
        if (image == null) {
            throw new IOException("Could not read image file: " + inputFile.getAbsolutePath());
        }

        // Get a ImageWriter for jpeg format.
        var writers = ImageIO.getImageWritersByFormatName("jpeg");
        if (!writers.hasNext()) {
            throw new IllegalStateException("No writers found");
        }
        var writer = writers.next();

        // Set the compression quality.
        var param = writer.getDefaultWriteParam();
        param.setCompressionMode(javax.imageio.ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);

        // Write the image to the output file.
        try (var outputStream = ImageIO.createImageOutputStream(outputFile)) {
            writer.setOutput(outputStream);
            writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
        }
    }

    public static Node createCircle(int width, int height) {
        Circle circle = new Circle();
        double radius = Math.min(width, height) / 2.0;
        circle.setRadius(radius);
        circle.setCenterX(width / 2.0);
        circle.setCenterY(height / 2.0);
        return circle;
    }
}
