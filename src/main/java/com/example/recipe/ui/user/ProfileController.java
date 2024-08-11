package com.example.recipe.ui.user;

import com.example.recipe.domain.User;
import com.example.recipe.services.UserService;
import com.example.recipe.utils.DialogUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
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
    private User user;

    public void initialize() {
        userService = new UserService();
        getUserProfile();
        configureDatePicker();
    }

    private void getUserProfile() {
        var response = userService.getUserById(107);

        if (response.isSuccess()) {
            user = response.getData();

            setProfile(user);
        } else {
            DialogUtil.showErrorDialog("Error", response.getMessage());
        }
    }

    private void setProfile(User userData) {
        email.setText(userData.getEmail());

        if (userData.getFirstName() != null)
            tfFirstName.setText(userData.getFirstName());

        if (userData.getLastName() != null)
            tfLastName.setText(userData.getLastName());

        if (userData.getFirstName() != null || userData.getLastName() != null)
            fullName.setText(userData.getFirstName() + " " + userData.getLastName());

        if (userData.getGender() != null && !userData.getGender().isEmpty() && genderComboBox.getItems().contains(user.getGender()))
            genderComboBox.setValue(userData.getGender());

        if (userData.getDob() != null)
            dobDatePicker.setValue(LocalDate.parse(userData.getDob().toString()));
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
        String firstName = tfFirstName.getText();
        String lastName = tfLastName.getText();

        if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
            DialogUtil.showErrorDialog("Validation Error", "First Name and Last Name cannot be empty.");
            return;
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);

        if (genderComboBox.getValue() != null) {
            user.setGender(genderComboBox.getValue());
        }

        if (dobDatePicker.getValue() != null) {
            user.setDob(Date.valueOf(dobDatePicker.getValue()));
        }

        userService.updateProfile(user, (response) -> {
            if (response.isSuccess()) {
                setProfile(response.getData());
                DialogUtil.showInfoDialog("Success", "User profile updated successfully.");
            } else {
                DialogUtil.showErrorDialog("Error", response.getMessage());
            }
        });
    }
}
