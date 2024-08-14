package com.example.recipe.ui.admin;

import com.example.recipe.domain.Recipe;
import com.example.recipe.domain.User;
import com.example.recipe.domain.common.DbResponse;

import static com.example.recipe.utils.DialogUtil.showErrorDialog;
import static com.example.recipe.utils.DialogUtil.showInfoDialog;
import static com.example.recipe.utils.LoggerUtil.logger;

import com.example.recipe.services.admin.AdminRecipeService;
import com.example.recipe.services.admin.AdminUserService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Comparator;

public class UserController {
    @FXML
    public Text totalUsersText;
    @FXML
    public ComboBox<String> sortByValue;
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> userId;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> typeColumn;
    @FXML
    public TableColumn<User, String> statusColumn;
    @FXML
    public TableColumn<User, Integer> totalRecipesColumn;
    @FXML
    private TableColumn<User, String> createdAtColumn;
    @FXML
    public TableColumn<User, String> useremailColumn;
    @FXML
    private TableColumn<User, Void> actions;
    @FXML
    public TextField searchInput;

    private final AdminRecipeService adminRecipeService = new AdminRecipeService();
    private final AdminUserService userService = new AdminUserService();

    public void initialize() {
        configureTable();
        loadTableData();
        searchInput.textProperty().addListener((observable, oldValue, newValue) -> {
            searchUser(newValue);
        });
    }

    @FXML
    public void handleSortByValue() {
        String selectedValue = sortByValue.getValue();
        if (selectedValue != null) {
            sortTableData(selectedValue);
        }
    }

    private void sortTableData(String sortBy) {
        DbResponse<ArrayList<User>> response = userService.getAllUsers();
        if (response.isSuccess()) {
            ArrayList<User> userArrayList = response.getData();
            switch (sortBy) {
                case "Sort By username in ascending":
                    userArrayList.sort(Comparator.comparing(User::getUsername));
                    break;
                case "Sort By username in descending":
                    userArrayList.sort(Comparator.comparing(User::getUsername).reversed());
                    break;
                case "Sort By created date in ascending":
                    userArrayList.sort(Comparator.comparing(User::getCreated_at));
                    break;
                case "Sort By created date in descending":
                    userArrayList.sort(Comparator.comparing(User::getCreated_at).reversed());
                    break;
                default:
                    logger.error("Unknown sort option: " + sortBy);
                    return;
            }
            userTable.getItems().setAll(userArrayList);
        } else {
            logger.error("Error retrieving data: " + response.getMessage());
        }
    }

    private void configureTable() {
        userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        useremailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        typeColumn.setCellValueFactory(cellData -> {
            boolean isAdmin = cellData.getValue().isAdmin();
            return new SimpleStringProperty(isAdmin ? "Admin" : "User");
        });
        statusColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            return new SimpleStringProperty(user.getStatus());
        });
        createdAtColumn.setCellValueFactory(cellData -> {
            String[] parts = cellData.getValue().getCreated_at().toString().split(" ");
            String datePart = parts[0];
            return new SimpleStringProperty(datePart);
        });
        totalRecipesColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            DbResponse<ArrayList<Recipe>> response = adminRecipeService.getRecipesByUserId(user.getUserId());
            if (response.isSuccess()) {
                return new SimpleIntegerProperty(response.getData().size()).asObject();
            } else {
                return new SimpleIntegerProperty(0).asObject();
            }
        });

        // Add buttons to the actions column
        actions.setCellFactory(new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
            @Override
            public TableCell<User, Void> call(TableColumn<User, Void> param) {
                return new TableCell<User, Void>() {
                    private final ToggleButton toggleButton = new ToggleButton("Active");
                    private final HBox hBox = new HBox(10, toggleButton);
                    {
                        toggleButton.getStyleClass().add("button");
                    }
                    {
                        toggleButton.setOnAction(event -> {
                            User user = getTableView() != null ? getTableView().getItems().get(getIndex()) : null;
                            if (user != null) {
                                String newStatus = toggleButton.isSelected() ? "active" : "disabled";
                                handleStatusChange(user.getUserId(), newStatus);
                            }
                        });
                        updateToggleButtonState();
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || getTableRow() == null || getTableView() == null) {
                            setGraphic(null);
                        } else {
                            updateToggleButtonState();
                            setGraphic(hBox);
                        }
                    }

                    private void updateToggleButtonState() {
                        if (getTableView() != null) {
                            User user = getTableView().getItems().get(getIndex());
                            if (user != null) {
                                toggleButton.setSelected("active".equals(user.getStatus()));
                                toggleButton.setText(toggleButton.isSelected() ? "Active" : "Disabled");
                            }
                        }
                    }
                };
            }
        });

    }

    private void loadTableData() {
        DbResponse<ArrayList<User>> response = userService.getAllUsers();
        if (response.isSuccess()) {
            ArrayList<User> userList = response.getData();
            userTable.getItems().setAll(userList);
            totalUsersText.setText("List of Users (Total: " + userList.size() + ")");
        } else {
            logger.error("Error retrieving data: " + response.getMessage());
        }
    }

    private void handleStatusChange(long userId, String status) {
        DbResponse<User> response = userService.toggleUserStatus(userId, status);
        if (response.isSuccess()) {
            showInfoDialog("Success", "User status changed to " + status);
            loadTableData();
        } else {
            logger.error("Error changing status: " + response.getMessage());
            showErrorDialog("Error", "Failed to change status: " + response.getMessage());
        }
    }

    private void searchUser(String searchValue) {
        DbResponse<ArrayList<User>> response = userService.getAllUsersByParams(searchValue);
        if (response.isSuccess()) {
            ArrayList<User> filteredUsers = response.getData();
            userTable.getItems().setAll(filteredUsers);
        } else {
            logger.error("Error retrieving data: " + response.getMessage());
        }
    }
}