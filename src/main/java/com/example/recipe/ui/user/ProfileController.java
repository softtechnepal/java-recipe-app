package com.example.recipe.ui.user;

import com.example.recipe.services.UserService;
import com.example.recipe.utils.DialogUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class ProfileController {
    @FXML
    public DatePicker dobDatePicker;
    @FXML
    public ComboBox<String> genderComboBox;
    @FXML
    public Label fullName;
    @FXML
    public Label email;
    @FXML
    public TextField tfLastName;
    @FXML
    public TextField tfFirstName;

    private UserService userService;

    public void initialize() {
        userService = new UserService();
        getUserProfile();
        configureDatePicker();
    }

    private void getUserProfile() {
        var response = userService.getUserById(107);

        if (response.isSuccess()) {
            var user = response.getData();

            email.setText(user.getEmail());

            if (user.getFirstName() != null)
                tfFirstName.setText(user.getFirstName());

            if (user.getLastName() != null)
                tfLastName.setText(user.getLastName());

            if (user.getFirstName() != null || user.getLastName() != null)
                fullName.setText(user.getFirstName() + " " + user.getLastName());

            if (user.getGender() != null && !user.getGender().isEmpty() && genderComboBox.getItems().contains(user.getGender()))
                genderComboBox.setValue(user.getGender());

            if (user.getDob() != null)
                dobDatePicker.setValue(LocalDate.parse(user.getDob().toString()));
        } else {
            DialogUtil.showErrorDialog("Error", response.getMessage());
        }
    }

    private void configureDatePicker() {
        dobDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });
    }

    public void saveProfile(ActionEvent actionEvent) {

    }
}
