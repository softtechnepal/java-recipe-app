package com.example.recipe.repositories.impl;

import com.example.recipe.config.DatabaseConfig;
import com.example.recipe.domain.User;
import com.example.recipe.domain.common.DatabaseCallback;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.domain.recipe.Category;
import com.example.recipe.repositories.iadmin.IAdminUserRepository;
import com.example.recipe.repositories.iuser.UserRepository;
import com.example.recipe.services.UserDetailStore;
import com.example.recipe.utils.DatabaseThread;
import com.example.recipe.utils.ImageUtil;
import com.example.recipe.utils.PasswordUtil;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static com.example.recipe.utils.LoggerUtil.logger;

public class UserRepositoryImpl implements IAdminUserRepository, UserRepository {

    @Override
    public DbResponse<ArrayList<User>> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "SELECT * FROM users ORDER BY user_id DESC";
            try (PreparedStatement st = conn.prepareStatement(query)) {
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    User user = new User(
                            rs.getLong("user_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("gender"),
                            rs.getDate("dob"),
                            rs.getBoolean("is_admin"),
                            rs.getString("status"),
                            rs.getDate("created_at")
                    );
                    users.add(user);
                }
            }
        } catch (Exception e) {
            logger.error("Error while retrieving data", e);
            return new DbResponse.Failure<>(e.getMessage());
        }
        return new DbResponse.Success<>("Get user success", users);
    }

    @Override
    public DbResponse<User> getUserById(long userId) {
        User user = null;
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "SELECT * FROM users WHERE user_id = ?";
            try (PreparedStatement st = conn.prepareStatement(query)) {
                st.setLong(1, userId);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    user = new User(
                            rs.getLong("user_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("gender"),
                            rs.getDate("dob"),
                            rs.getBoolean("is_admin"),
                            rs.getString("status"),
                            rs.getDate("created_at")
                    );

                    user.setProfilePicture(rs.getString("profile_picture"));
                    logger.error("User Repo Data: {}", rs.getString("profile_picture"));
                }
            }
        } catch (Exception e) {
            logger.error("Error while retrieving data", e);
            return new DbResponse.Failure<>(e.getMessage());
        }
        if (user == null) {
            return new DbResponse.Failure<>("User not found");
        }
        return new DbResponse.Success<>("Get user success", user);
    }

    @Override
    public void changePassword(String recentPassword, String newPassword, DatabaseCallback<Void> callback) {
        DatabaseThread.runDataOperation(() -> {
            try (Connection connection = DatabaseConfig.getConnection()) {
                // Step 1: Retrieve the user by user_id
                String getUserQuery = "SELECT password FROM users WHERE user_id = ?";
                try (PreparedStatement getUserStmt = connection.prepareStatement(getUserQuery)) {
                    getUserStmt.setLong(1, UserDetailStore.getInstance().getUserId());
                    ResultSet rs = getUserStmt.executeQuery();
                    if (rs.next()) {
                        String storedPassword = rs.getString("password");

                        // Step 2: Check if the provided recentPassword matches the stored encrypted password
                        if (!PasswordUtil.verifyPassword(recentPassword, storedPassword)) {
                            return new DbResponse.Failure<>("Invalid recent password");
                        }

                        // Step 3: Update the password
                        String updatePasswordQuery = "UPDATE users SET password = ? WHERE user_id = ?";
                        try (PreparedStatement updatePasswordStmt = connection.prepareStatement(updatePasswordQuery)) {
                            String encryptedNewPassword = PasswordUtil.hashPassword(newPassword);
                            updatePasswordStmt.setString(1, encryptedNewPassword);
                            updatePasswordStmt.setLong(2, UserDetailStore.getInstance().getUserId());
                            updatePasswordStmt.executeUpdate();
                            return new DbResponse.Success<>("Password changed successfully", null);
                        }
                    } else {
                        return new DbResponse.Failure<>("User not found");
                    }
                }
            } catch (Exception e) {
                logger.error("Error while changing password", e);
                return DbResponse.failure(e.getMessage());
            }
        }, callback);
    }

    @Override
    public DbResponse<User> toggleUserStatus(long userId, String status) {
        if (!status.equals("active") && !status.equals("disabled")) {
            return new DbResponse.Failure<>("Invalid status parameter");
        }
        String query = "UPDATE users SET status = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {
            st.setString(1, status);
            st.setLong(2, userId);
            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0) {
                return new DbResponse.Failure<>("User not found");
            }
        } catch (Exception e) {
            logger.error("Error while updating user status", e);
            return new DbResponse.Failure<>(e.getMessage());
        }
        return new DbResponse.Success<>("User status updated to " + status, null);
    }

    @Override
    public void updateProfile(User updateRequest, DatabaseCallback<User> result) {
        String query = """
                    UPDATE users SET first_name = ?, last_name = ?, gender = ?, dob = ?, profile_picture = ? WHERE user_id = ?
                """;

        DatabaseThread.runDataOperation(() -> {
            try (Connection connection = DatabaseConfig.getConnection();
                 PreparedStatement st = connection.prepareStatement(query)) {
                st.setString(1, updateRequest.getFirstName());
                st.setString(2, updateRequest.getLastName());
                st.setString(3, updateRequest.getGender());
                st.setDate(4, updateRequest.getDob());
                st.setString(5, updateRequest.getProfilePicture());
                st.setLong(6, updateRequest.getUserId());
                int rowsAffected = st.executeUpdate();
                if (rowsAffected == 0) {
                    return new DbResponse.Failure<>("User not found");
                }
            } catch (Exception e) {
                logger.error("Error while logging in", e);
                return DbResponse.failure(e.getMessage());
            }
            return new DbResponse.Success<>("User details updated successfully", updateRequest);
        }, result);
    }

    @Override
    public DbResponse<ArrayList<User>> getAllUsersByParams(String params) {
        ArrayList<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "SELECT * FROM users WHERE first_name ILIKE ? OR last_name ILIKE ? OR username ILIKE ? OR email ILIKE ?";
            try (PreparedStatement st = conn.prepareStatement(query)) {
                st.setString(1, "%" + params + "%");
                st.setString(2, "%" + params + "%");
                st.setString(3, "%" + params + "%");
                st.setString(4, "%" + params + "%");
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    User user = new User(
                            rs.getLong("user_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("gender"),
                            rs.getDate("dob"),
                            rs.getBoolean("is_admin"),
                            rs.getString("status"),
                            rs.getDate("created_at")
                    );
                    users.add(user);
                }
            }
        } catch (Exception e) {
            logger.error("Error while retrieving data", e);
            return new DbResponse.Failure<>(e.getMessage());
        }
        return new DbResponse.Success<>("Get users by params success", users);
    }
}
