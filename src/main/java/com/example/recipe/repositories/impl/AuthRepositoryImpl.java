package com.example.recipe.repositories.impl;

import com.example.recipe.config.DatabaseConfig;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.domain.common.DatabaseCallback;
import com.example.recipe.domain.request.LoginRequest;
import com.example.recipe.domain.request.UserRequest;
import com.example.recipe.domain.response.LoginResponse;
import com.example.recipe.repositories.AuthRepository;
import com.example.recipe.utils.*;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static com.example.recipe.utils.LoggerUtil.logger;

public class AuthRepositoryImpl implements AuthRepository {

    @Override
    public void login(LoginRequest loginRequest, DatabaseCallback<LoginResponse> result) {
        DatabaseThread.runDataOperation(() -> {
            try (Connection connection = DatabaseConfig.getConnection()) {
                if (userExists(connection, loginRequest.getUsername())) {
                    return authenticateUser(connection, loginRequest);
                } else {
                    return DbResponse.failure("Invalid username or password");
                }
            } catch (Exception e) {
                logger.error("Error while logging in", e);
                return DbResponse.failure(e.getMessage());
            }
        }, result);
    }

    private boolean userExists(Connection connection, String username) throws SQLException {
        String query = "SELECT 1 FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet resultSet = stmt.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private DbResponse<LoginResponse> authenticateUser(Connection connection, LoginRequest loginRequest) throws Exception {
        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, loginRequest.getUsername());
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    String password = resultSet.getString("password");
                    if (PasswordUtil.verifyPassword(loginRequest.getPassword(), password)) {
                        LoginResponse loginResponse = new LoginResponse(
                                resultSet.getString("username"),
                                resultSet.getInt("user_id"),
                                resultSet.getString("username"),
                                resultSet.getString("email"),
                                resultSet.getString("first_name"),
                                resultSet.getString("last_name"),
                                resultSet.getBoolean("is_admin")
                        );
                        return DbResponse.success("Login successful", loginResponse);
                    } else {
                        throw new Exception("Invalid username or password");
                    }
                }
            }
        }
        return DbResponse.failure("Invalid username or password");
    }


    @Override
    public void register(UserRequest register, DatabaseCallback<String> result) {
        DatabaseThread.runDataOperation(() -> {
            try (Connection connection = DatabaseConfig.getConnection()) {
                if (userExistsByEmailOrUsername(connection, register.getEmail(), register.getUsername())) {
                    return DbResponse.failure("User with email or username already exists");
                }
                registerNewUser(connection, register);
                return DbResponse.success("User registered successfully", "Register successful!");
            } catch (PSQLException e) {
                String constraintName = Objects.requireNonNull(e.getServerErrorMessage()).getConstraint();
                String userFriendlyMessage = ConstraintViolationMapper.getUserFriendlyMessage(constraintName);
                logger.error("Error while registering user: {}", userFriendlyMessage, e);
                return DbResponse.failure(userFriendlyMessage);
            } catch (Exception e) {
                logger.error("Error while registering user", e);
                return DbResponse.failure(e.getMessage());
            }
        }, result);
    }

    private void registerNewUser(Connection connection, UserRequest register) throws SQLException {
        String insertQuery = "INSERT INTO Users " +
                "(email, password, username, first_name, last_name, is_admin) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
            insertStmt.setString(1, register.getEmail());
            insertStmt.setString(2, PasswordUtil.hashPassword(register.getPassword()));
            insertStmt.setString(3, register.getUsername());
            insertStmt.setString(4, register.getFirstName());
            insertStmt.setString(5, register.getLastName());
            insertStmt.setBoolean(6, false); // todo Assuming new users are not admins by default
            insertStmt.executeUpdate();
        }
    }

    private boolean userExistsByEmailOrUsername(Connection connection, String email, String username) throws SQLException {
        String query = "SELECT 1 FROM users WHERE email = ? OR username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, username);
            try (ResultSet resultSet = stmt.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public DbResponse<String> forgotPassword(String email) {
        return DbResponse.failure("Not implemented");
    }
}
