package com.example.recipe.ui.admin;

import com.example.recipe.domain.Recipe;
import com.example.recipe.domain.User;
import com.example.recipe.domain.common.DbResponse;
import com.example.recipe.services.admin.AdminRecipeService;
import com.example.recipe.services.admin.AdminUserService;
import com.example.recipe.services.user.UserRecipeService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
    public TableColumn<Recipe, String> category;
    @FXML
    public TableColumn<Recipe, String> ingredients;
    @FXML
    public TableColumn<Recipe, Integer> totalReviews;
    @FXML
    public TableColumn<Recipe, Integer> totalSaved;
    @FXML
    public TableColumn<Recipe, Void> actions;
    @FXML
    public TextField searchInput;
    @FXML
    public TableView<Recipe> recipeTable;
    @FXML
    public Text totalRecipesText;
    @FXML
    public ComboBox<String> sortByValue;

    private final AdminRecipeService recipeService = new AdminRecipeService();
    private final AdminUserService userService = new AdminUserService();


    public void initialize() {
        configureTable();
        loadTableData();
        searchInput.textProperty().addListener((observable, oldValue, newValue) -> {
            searchRecipe(newValue);
        });
    }

    Task<Void> searchTask;

    private void searchRecipe(String newValue) {
        if (searchTask != null && searchTask.isRunning()) {
            searchTask.cancel();
        }
        searchTask = new Task<>() {
            @Override
            protected Void call() {
                recipeService.searchRecipe(newValue, (response -> {
                    if (response.isSuccess()) {
                        recipeTable.getItems().setAll(response.getData());
                    } else {
                        logger.error("Error retrieving data: " + response.getMessage());
                    }
                }));
                return null;
            }
        };

        new Thread(searchTask).start();
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
        totalReviews.setCellValueFactory(new PropertyValueFactory<>("totalReviews"));
        totalSaved.setCellValueFactory(new PropertyValueFactory<>("totalSaved"));
        category.setCellValueFactory(new PropertyValueFactory<>("categories"));
        ingredients.setCellValueFactory(new PropertyValueFactory<>("ingredients"));
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
            Thread thread = new Thread(() -> {
                recipeTable.getItems().setAll(recipeArrayList);
                totalRecipesText.setText("List of Recipes (Total Recipes: " + recipeArrayList.size() + ")");
            });
            new Thread(thread).start();
        } else {
            logger.error("Error retrieving data: " + response.getMessage());
        }
    }

    private void handleDeleteAction(long recipeId) {
        UserRecipeService recipeService = new UserRecipeService();
        recipeService.deleteRecipe(recipeId, (response -> {
            if (response.isSuccess()) {
                showInfoDialog("Success", "Recipe deleted successfully.");
                loadTableData();
            } else {
                logger.error("Error deleting recipe: " + response.getMessage());
                showErrorDialog("Error", "Failed to delete recipe: " + response.getMessage());
            }
        }));
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

    public void handleSortByValue(ActionEvent actionEvent) {
        String selectedValue = sortByValue.getValue();
        if (selectedValue != null) {
            sortTableData(selectedValue);
        }
    }

    private void sortTableData(String sortBy) {
        DbResponse<ArrayList<Recipe>> response = recipeService.getAllRecipes();
        if (response.isSuccess()) {
            ArrayList<Recipe> recipeArrayList = response.getData();
            switch (sortBy) {
                case "Sort By title in descending":
                    recipeArrayList.sort(Comparator.comparing(Recipe::getTitle));
                    break;
                case "Sort By title in ascending":
                    recipeArrayList.sort(Comparator.comparing(Recipe::getTitle).reversed());
                    break;
                case "Sort By created date in ascending":
                    recipeArrayList.sort(Comparator.comparing(Recipe::getCreatedAt));
                    break;
                case "Sort By created date in descending":
                    recipeArrayList.sort(Comparator.comparing(Recipe::getCreatedAt).reversed());
                    break;
                default:
                    logger.error("Unknown sort option: " + sortBy);
                    return;
            }
            recipeTable.getItems().setAll(recipeArrayList);
        } else {
            logger.error("Error retrieving data: " + response.getMessage());
        }
    }
}
