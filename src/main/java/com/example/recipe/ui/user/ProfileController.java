package com.example.recipe.ui.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class ProfileController {
    @FXML
    public DatePicker dobDatePicker;
    @FXML
    public ComboBox genderComboBox;


    public void initialize() {

        configureDatePicker();
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
