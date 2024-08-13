package com.example.recipe.repositories.impl;

import com.example.recipe.config.DatabaseConfig;
import com.example.recipe.domain.recipe.Category;
import com.example.recipe.domain.common.DatabaseCallback;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.repositories.iadmin.IAdminCategoryRepository;
import com.example.recipe.repositories.iuser.IUserCategoryRepository;
import com.example.recipe.utils.DatabaseThread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.example.recipe.utils.LoggerUtil.logger;

public class CategoryRepositoryImpl implements IAdminCategoryRepository, IUserCategoryRepository {

    @Override
    public DbResponse<ArrayList<Category>> getAllCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "SELECT * FROM categories ORDER BY category_id DESC";
            try (PreparedStatement st = conn.prepareStatement(query)) {
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    Category category = new Category(
                            rs.getLong("category_id"),
                            rs.getString("category_name")
                    );
                    categories.add(category);
                }
            }
        } catch (Exception e) {
            logger.error("Error while retrieving data", e);
            return new DbResponse.Failure<>(e.getMessage());
        }
        return new DbResponse.Success<>("Get categories success", categories);
    }

    @Override
    public void getAllCategories(DatabaseCallback<ArrayList<Category>> callback) {
        DatabaseThread.runDataOperation(this::getAllCategories, callback);
    }


    @Override
    public DbResponse<Category> getCategoryById(long categoryId) {
        Category category = null;
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "SELECT * FROM categories WHERE category_id = ?";
            try (PreparedStatement st = conn.prepareStatement(query)) {
                st.setLong(1, categoryId);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    category = new Category(
                            rs.getLong("category_id"),
                            rs.getString("category_name")
                    );
                }
            }
        } catch (Exception e) {
            logger.error("Error while retrieving data", e);
            return new DbResponse.Failure<>(e.getMessage());
        }
        if (category == null) {
            return new DbResponse.Failure<>("Category not found");
        }
        return new DbResponse.Success<>("Get category success", category);
    }

    @Override
    public DbResponse<Category> addCategory(Category category) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "INSERT INTO categories (category_name) VALUES (?)";
            try (PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                st.setString(1, category.getCategoryName());
                int rowsAffected = st.executeUpdate();

                if (rowsAffected > 0) {
                    ResultSet generatedKeys = st.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        long generatedId = generatedKeys.getLong(1);
                        category.setCategoryId(generatedId);
                        return new DbResponse.Success<>("Category added successfully", category);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error while adding category", e);
            return new DbResponse.Failure<>(e.getMessage());
        }
        return new DbResponse.Failure<>("Failed to add category");
    }

    @Override
    public DbResponse<Category> updateCategory(Category category) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "UPDATE categories SET category_name = ? WHERE category_id = ?";
            try (PreparedStatement st = conn.prepareStatement(query)) {
                st.setString(1, category.getCategoryName());
                st.setLong(2, category.getCategoryId());
                int rowsAffected = st.executeUpdate();

                if (rowsAffected > 0) {
                    return new DbResponse.Success<>("Category updated successfully", category);
                } else {
                    return new DbResponse.Failure<>("Category not found");
                }
            }
        } catch (Exception e) {
            logger.error("Error while updating category", e);
            return new DbResponse.Failure<>(e.getMessage());
        }
    }

    @Override
    public DbResponse<Category> deleteCategory(long categoryId) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "DELETE FROM categories WHERE category_id = ?";
            try (PreparedStatement st = conn.prepareStatement(query)) {
                st.setLong(1, categoryId);
                int rowsAffected = st.executeUpdate();
                if (rowsAffected == 0) {
                    return new DbResponse.Failure<>("Category not found");
                }
            }
        } catch (Exception e) {
            logger.error("Error while deleting category", e);
            return new DbResponse.Failure<>(e.getMessage());
        }
        return new DbResponse.Success<>("Category deleted successfully", null);
    }

    @Override
    public DbResponse<ArrayList<Category>> getAllByParams(String params) {
        ArrayList<Category> categories = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "SELECT * FROM categories WHERE category_name ILIKE ?";
            try (PreparedStatement st = conn.prepareStatement(query)) {
                st.setString(1, "%" + params + "%");
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    Category category = new Category(
                            rs.getLong("category_id"),
                            rs.getString("category_name")
                    );
                    categories.add(category);
                }
            }
        } catch (Exception e) {
            logger.error("Error while retrieving data", e);
            return new DbResponse.Failure<>(e.getMessage());
        }
        return new DbResponse.Success<>("Get categories by params success", categories);
    }
}
