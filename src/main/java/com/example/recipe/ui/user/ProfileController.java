package com.example.recipe.ui.user;

import com.example.recipe.domain.User;
import com.example.recipe.services.UserDetailStore;
import com.example.recipe.services.UserService;
import com.example.recipe.utils.DialogUtil;
import com.example.recipe.utils.ImageUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import static com.example.recipe.utils.LoggerUtil.logger;

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
    @FXML
    public ImageView profileImage;

    private UserService userService;
    private User user;

    public void initialize() {
        userService = new UserService();
        getUserProfile();
        configureDatePicker();
    }

    private void getUserProfile() {
        var response = userService.getUserById(UserDetailStore.getInstance().getUserId());

        if (response.isSuccess()) {
            user = response.getData();
            setProfile(user);
        } else {
            DialogUtil.showErrorDialog("Error", response.getMessage());
        }
    }

    private void setProfile(User userData) {
        if (userData.getProfilePicture() != null) {
            ImageUtil.loadImageAsync(userData.getProfilePicture(), profileImage);
        }

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

        updateUserProfile(user);
    }

    private void updateUserProfile(User user) {
        userService.updateProfile(user, (response) -> {
            if (response.isSuccess()) {
                setProfile(response.getData());
                DialogUtil.showInfoDialog("Success", "Profile updated successfully.");
            } else {
                DialogUtil.showErrorDialog("Error", response.getMessage());
            }
        });
    }

    public void changePhoto(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(((Node) mouseEvent.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            try {
                String newImagePath = ImageUtil.copyImageToDbImages(selectedFile);
                user.setProfilePicture(newImagePath);
                updateUserProfile(user);
            } catch (Exception e) {
                DialogUtil.showErrorDialog("Error", "Error while uploading image");
            }
        }
    }

    public void onChangePassword(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/recipe/change-password-dialog.fxml"));
            Parent root = loader.load();
            ChangePasswordController controller = loader.getController();
            Stage stage = new Stage();
            controller.setDialogStage(stage);
            stage.setTitle("Change Password");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            DialogUtil.showErrorDialog("Error", "Unable to load change password dialog.");
        }
    }
}
