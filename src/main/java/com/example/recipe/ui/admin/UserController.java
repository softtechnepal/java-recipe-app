package com.example.recipe.ui.admin;

import com.example.recipe.domain.User;
import com.example.recipe.domain.common.DbResponse;

import static com.example.recipe.utils.DialogUtil.showErrorDialog;
import static com.example.recipe.utils.DialogUtil.showInfoDialog;
import static com.example.recipe.utils.LoggerUtil.logger;

import com.example.recipe.services.admin_access.AdminUserService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.ArrayList;

public class UserController {
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
    private TableColumn<User, String> createdAtColumn;
    @FXML
    private TableColumn<User, Void> actions;

    public void initialize() {
        configureTable();
        loadTableData();
    }

    private void configureTable() {
        userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        typeColumn.setCellValueFactory(cellData -> {
            boolean isAdmin = cellData.getValue().isAdmin();
            return new SimpleStringProperty(isAdmin ? "Admin" : "User");
        });
        statusColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            return new SimpleStringProperty(user.getStatus());
        });
        createdAtColumn.setCellValueFactory(cellData -> {
            String[] parts = cellData.getValue().getCreatedAt().toString().split(" ");
            String datePart = parts[0];
            return new SimpleStringProperty(datePart);
        });

        // Add buttons to the actions column
        actions.setCellFactory(new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
            @Override
            public TableCell<User, Void> call(TableColumn<User, Void> param) {
                return new TableCell<User, Void>() {
                    private final ToggleButton toggleButton = new ToggleButton("Active");
                    private final HBox hBox = new HBox(10, toggleButton);

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
        AdminUserService userService = new AdminUserService();
        DbResponse<ArrayList<User>> response = userService.getAllUsers();
        if (response.isSuccess()) {
            ArrayList<User> userList = response.getData();
            userTable.getItems().setAll(userList);
        } else {
            logger.error("Error retrieving data: " + response.getMessage());
        }
    }

    private void handleStatusChange(long userId, String status) {
        AdminUserService userService = new AdminUserService();
        DbResponse<User> response = userService.toggleUserStatus(userId, status);
        if (response.isSuccess()) {
            showInfoDialog("Success", "User status changed to " + status);
            loadTableData();
        } else {
            logger.error("Error changing status: " + response.getMessage());
            showErrorDialog("Error", "Failed to change status: " + response.getMessage());
        }
    }


}
