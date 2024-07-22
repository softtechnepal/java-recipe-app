package com.example.recipe.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static com.example.recipe.utils.LoggerUtil.logger;

public class DatabaseConfig {
    private static final String url;
    private static final String username;
    private static final String password;
    private static Connection connection;

    static {
        Properties properties = new Properties();
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                logger.error("Sorry, unable to find database.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            logger.error("Failed to load database properties");
        }

        url = properties.getProperty("url") + properties.getProperty("database");
        username = properties.getProperty("username");
        password = properties.getProperty("password");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("PostgreSQL Driver not found", e);
        }
    }

    private DatabaseConfig() {
        // Private constructor to prevent instantiation
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, username, password);
            logger.info("Database connected!");
        }
        return connection;
    }

    public static synchronized void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            logger.info("Database connection closed!");
        }
    }
}
