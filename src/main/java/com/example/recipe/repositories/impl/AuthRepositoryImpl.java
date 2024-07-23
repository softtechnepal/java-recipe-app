package com.example.recipe.repositories.impl;

import com.example.recipe.config.DatabaseConfig;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.domain.request.LoginRequest;
import com.example.recipe.domain.request.UserRequest;
import com.example.recipe.domain.response.LoginResponse;
import com.example.recipe.repositories.AuthRepository;
import com.example.recipe.utils.ConstraintViolationMapper;
import com.example.recipe.utils.PasswordUtil;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

import static com.example.recipe.utils.LoggerUtil.logger;

public class AuthRepositoryImpl implements AuthRepository {

    @Override
    public DbResponse<LoginResponse> login(LoginRequest loginRequest) {
        try (Connection connection = DatabaseConfig.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, loginRequest.getUsername());
                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        var password = resultSet.getString("password");
                        if (!PasswordUtil.verifyPassword(loginRequest.getPassword(), password)) {
                            throw new Exception("Invalid username or password");
                        }
                        // User found, return success response
                        LoginResponse loginResponse = new LoginResponse(
                                resultSet.getString("username"),
                                resultSet.getInt("user_id"),
                                resultSet.getString("username"),
                                resultSet.getString("email"),
                                resultSet.getString("first_name"),
                                resultSet.getString("last_name"),
                                resultSet.getBoolean("is_admin")
                        );
                        return new DbResponse.Success<>("Login successful", loginResponse);
                    } else {
                        // User not found, return failure response
                        throw new Exception("Invalid username or password");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error while logging in", e);
            return new DbResponse.Failure<>(e.getMessage());
        }
    }

    @Override
    public DbResponse<String> register(UserRequest register) {
        try (Connection connection = DatabaseConfig.getConnection()) {
            // Register user
            String checkUser = "SELECT * FROM users WHERE email = ? OR username = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkUser)) {
                checkStatement.setString(1, register.getEmail());
                checkStatement.setString(2, register.getUsername());
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next()) {
                        throw new Exception("User with email or username already exists");
                    }
                }
            }
            String insertQuery = "INSERT INTO Users " +
                    "(email, password, username, first_name, last_name, is_admin) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setString(1, register.getEmail());
                insertStmt.setString(2, PasswordUtil.hashPassword(register.getPassword()));
                insertStmt.setString(3, register.getUsername());
                insertStmt.setString(4, register.getFirstName());
                insertStmt.setString(5, register.getLastName());
                insertStmt.setBoolean(6, false); // Assuming new users are not admins by default
                insertStmt.executeUpdate();
            }
            return new DbResponse.Success<>("User registered successfully", "Register successful!");
        } catch (PSQLException e) {
            String constraintName = Objects.requireNonNull(e.getServerErrorMessage()).getConstraint();
            String userFriendlyMessage = ConstraintViolationMapper.getUserFriendlyMessage(constraintName);
            logger.error("Error while registering user: {}", userFriendlyMessage, e);
            return new DbResponse.Failure<>(userFriendlyMessage);
        } catch (Exception e) {
            logger.error("Error while registering user", e);
            return new DbResponse.Failure<>(e.getMessage());
        }
    }
}
