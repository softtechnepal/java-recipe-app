package com.example.recipe.repositories.impl;

import com.example.recipe.config.DatabaseConfig;
import com.example.recipe.domain.User;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.repositories.iadmin.IAdminUserRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import static com.example.recipe.utils.LoggerUtil.logger;

public class UserRepositoryImpl implements IAdminUserRepository {

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
        return new DbResponse.Success<>("Get user success",users);
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
                            rs.getBoolean("is_admin"),
                            rs.getString("status"),
                            rs.getDate("created_at")
                    );
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
}
