package com.example.recipe.ui.admin;

import com.example.recipe.domain.recipe.Category;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.services.admin.AdminCategoryService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;

import static com.example.recipe.utils.DialogUtil.showErrorDialog;
import static com.example.recipe.utils.DialogUtil.showInfoDialog;
import static com.example.recipe.utils.LoggerUtil.logger;

public class CategoryController {
    @FXML
    private TableView<Category> categoryTable;
    @FXML
    private TableColumn<Category, String> categoryId;
    @FXML
    private TableColumn<Category, String> categoryName;
    @FXML
    private TableColumn<Category, Void> actions;
    @FXML
    public TextField searchInput;

    private AdminCategoryService categoryService;

    public void initialize() {
        categoryService = new AdminCategoryService();
        configureTable();
        loadTableData();
        searchInput.textProperty().addListener((observable, oldValue, newValue) -> {
            searchCategories(newValue);
        });
    }

    private void configureTable() {
        categoryId.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        categoryName.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        // Add buttons to the actions column
        actions.setCellFactory(new Callback<TableColumn<Category, Void>, TableCell<Category, Void>>() {
            @Override
            public TableCell<Category, Void> call(TableColumn<Category, Void> param) {
                return new TableCell<Category, Void>() {
                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");
                    private final HBox hBox = new HBox(10, editButton, deleteButton);
                    {
                        editButton.getStyleClass().add("table-header-button");
                        deleteButton.getStyleClass().add("table-header-button");
                    }
                    {
                        // Handle delete button action
                        deleteButton.setOnAction(event -> {
                            Category category = getTableView().getItems().get(getIndex());
                            if (category != null) {
                                handleDeleteAction(category.getCategoryId());
                            }
                        });

                        // Handle edit button action
                        editButton.setOnAction(event -> {
                            Category category = getTableView().getItems().get(getIndex());
                            if (category != null) {
                                handleEditAction(category);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || getTableRow() == null) {
                            setGraphic(null);
                        } else {
                            setGraphic(hBox);
                        }
                    }
                };
            }
        });
    }

    private void loadTableData() {
        DbResponse<ArrayList<Category>> response = categoryService.getAllCategories();
        if (response.isSuccess()) {
            ArrayList<Category> categoryArrayList = response.getData();
            categoryTable.getItems().setAll(categoryArrayList);
        } else {
            logger.error("Error retrieving data: " + response.getMessage());
        }
    }

    private void searchCategories(String query) {
        DbResponse<ArrayList<Category>> response = categoryService.getAllByParams(query);
        if (response.isSuccess()) {
            ArrayList<Category> categoryArrayList = response.getData();
            categoryTable.getItems().setAll(categoryArrayList);
        } else {
            logger.error("Error retrieving data: " + response.getMessage());
        }
    }

    private void handleDeleteAction(long categoryId) {
        DbResponse<Category> response = categoryService.deleteCategory(categoryId);
        if (response.isSuccess()) {
            showInfoDialog("Success", "Category deleted successfully.");
            loadTableData();
        } else {
            logger.error("Error deleting category: " + response.getMessage());
            showErrorDialog("Error", "Failed to delete category: " + response.getMessage());
        }
    }

    private void handleEditAction(Category category) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/recipe/admin/categories-dialog.fxml"));
            Pane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Category");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(categoryTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            CategoryDialogController dialogController = loader.getController();
            dialogController.setDialogStage(dialogStage);
            dialogController.setCategory(category);
            dialogStage.showAndWait();
            loadTableData();
        } catch (Exception e) {
            logger.error("Error loading the add category dialog", e);
            showErrorDialog("Error", "Failed to open the category dialog");
        }
    }

    public void handleAddCategory(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/recipe/admin/categories-dialog.fxml"));
            Pane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Category");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(categoryTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            CategoryDialogController dialogController = loader.getController();
            dialogController.setDialogStage(dialogStage);
            dialogController.prepareForAdd();
            dialogStage.showAndWait();
            loadTableData();
        } catch (Exception e) {
            logger.error("Error loading the add category dialog", e);
            showErrorDialog("Error", "Failed to open the category dialog");
        }
    }
}
