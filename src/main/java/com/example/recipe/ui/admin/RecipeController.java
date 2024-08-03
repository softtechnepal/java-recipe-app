package com.example.recipe.ui.admin;

import com.example.recipe.domain.Recipe;
import com.example.recipe.domain.User;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.services.admin_access.AdminRecipeService;
import com.example.recipe.services.admin_access.AdminUserService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.ArrayList;

import static com.example.recipe.utils.DialogUtil.showErrorDialog;
import static com.example.recipe.utils.DialogUtil.showInfoDialog;
import static com.example.recipe.utils.LoggerUtil.logger;

public class RecipeController {
    @FXML
    public TableColumn<Recipe, String> recipeId;

    @FXML
    public TableColumn<Recipe, String> title;

    @FXML
    public TableColumn<Recipe, String> user;

    @FXML
    public TableColumn<Recipe, String> createdAt;

    @FXML
    public TableColumn<Recipe, Void> actions;

    @FXML
    public TableView<Recipe> recipeTable;

    private AdminRecipeService recipeService;
    private AdminUserService userService;

    public void initialize() {
        recipeService = new AdminRecipeService();
        userService = new AdminUserService();
        configureTable();
        loadTableData();
    }

    private void configureTable() {
        recipeId.setCellValueFactory(new PropertyValueFactory<>("recipeId"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        user.setCellValueFactory(cellData -> {
            long userId = cellData.getValue().getUserId();
            return new SimpleStringProperty(getUserName(userId));
        });
        createdAt.setCellValueFactory(cellData -> {
            String[] parts = cellData.getValue().getCreatedAt().split(" ");
            String datePart = parts[0];
            return new SimpleStringProperty(datePart);
        });

        // Add buttons to the actions column
        actions.setCellFactory(new Callback<TableColumn<Recipe, Void>, TableCell<Recipe, Void>>() {
            @Override
            public TableCell<Recipe, Void> call(TableColumn<Recipe, Void> param) {
                return new TableCell<Recipe, Void>() {
                    private final Button deleteButton = new Button("Delete");
                    private final HBox hBox = new HBox(20, deleteButton);

                    {
                        deleteButton.setOnAction(event -> {
                            Recipe recipe = getTableView().getItems().get(getIndex());
                            if (recipe != null) {
                                handleDeleteAction(recipe.getRecipeId());
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
        DbResponse<ArrayList<Recipe>> response = recipeService.getAllRecipes();
        if (response.isSuccess()) {
            ArrayList<Recipe> recipeArrayList = response.getData();
            recipeTable.getItems().setAll(recipeArrayList);
        } else {
            logger.error("Error retrieving data: " + response.getMessage());
        }
    }

    private void handleDeleteAction(long recipeId) {
        DbResponse<Recipe> res = recipeService.deleteRecipe(recipeId);
        if (res.isSuccess()) {
            showInfoDialog("Success", "Recipe deleted successfully.");
            loadTableData();
        } else {
            logger.error("Error deleting recipe: " + res.getMessage());
            showErrorDialog("Error", "Failed to delete recipe: " + res.getMessage());
        }
    }

    private String getUserName(long userId) {
        DbResponse<User> response = userService.getUserById(userId);
        if (response.isSuccess()) {
            User user = response.getData();
            return user.getUsername();
        } else {
            logger.error("Error retrieving user: " + response.getMessage());
            return "Unknown User";
        }
    }
}
