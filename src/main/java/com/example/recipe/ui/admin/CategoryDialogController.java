package com.example.recipe.ui.admin;

import com.example.recipe.domain.recipe.Category;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.services.admin.AdminCategoryService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static com.example.recipe.utils.DialogUtil.showErrorDialog;
import static com.example.recipe.utils.DialogUtil.showInfoDialog;

public class CategoryDialogController {
    @FXML
    public Label dialogTitle;
    @FXML
    public TextField categoryNameField;
    @FXML
    public Button dialogButton;
    private AdminCategoryService categoryService;
    private boolean isEditMode;
    private Category category;
    private Stage dialogStage;

    @FXML
    private void initialize() {
        categoryService = new AdminCategoryService();
    }

    public void handleSubmit(ActionEvent actionEvent) {
        String categoryName = categoryNameField.getText();
        if (categoryName == null || categoryName.trim().isEmpty()) {
            showErrorDialog("Validation Error", "Category Name is required.");
            return;
        }
        if (isEditMode) {
            // Update the existing category
            category.setCategoryName(categoryName);
            DbResponse<Category> res = categoryService.updateCategory(category);
            if (res.isSuccess()) {
                showInfoDialog("Success", "Category Updated Successfully");
                dialogStage.close();
            } else {
                showErrorDialog("Error", "Failed to update the category");
            }
        } else {
            // Add a new category
            Category newCategory = new Category();
            newCategory.setCategoryName(categoryName);
            DbResponse<Category> res = categoryService.addCategory(newCategory);
            if (res.isSuccess()) {
                showInfoDialog("Success", "Category added Successfully");
                dialogStage.close();
            } else {
                showErrorDialog("Error", "Failed to add the category");
            }
        }

    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCategory(Category category) {
        this.category = category;
        this.isEditMode = true;
        dialogTitle.setText("Edit Category");
        dialogButton.setText("Edit");
        categoryNameField.setText(category.getCategoryName());
    }

    public void prepareForAdd() {
        this.isEditMode = false;
        dialogTitle.setText("Add New Category");
        dialogButton.setText("Add");
        categoryNameField.clear();
    }
}
