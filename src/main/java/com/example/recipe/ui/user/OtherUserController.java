package com.example.recipe.ui.user;

import com.example.recipe.Constants;
import com.example.recipe.domain.enums.MenuListingType;
import com.example.recipe.services.BaseRecipeListing;
import com.example.recipe.services.UserService;
import com.example.recipe.utils.DialogUtil;
import com.example.recipe.utils.ImageUtil;
import com.example.recipe.utils.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.Map;

public class OtherUserController extends BaseRecipeListing {
    @FXML
    public ImageView profileImage;
    @FXML
    public Label usernameLabel;
    @FXML
    public Label emailLabel;
    @FXML
    public Label fullName;
    @FXML
    public GridPane menuGrid;
    @FXML
    public VBox progressContainer;
    @FXML
    public VBox noRecipeFound;

    private Long userId;

    public static void navigate(Map<String, Long> params) {
        NavigationUtil.insertChild("other-user-profile-view.fxml", params);
    }

    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        userId = (Long) NavigationUtil.getParam(Constants.USER_ID_PARAM);
        if (userId != null) {
            getUserProfile();
        }
    }

    private void getUserProfile() {
        var response = userService.getUserById(userId);
        if (response.isSuccess()) {
            var user = response.getData();
            ImageUtil.loadImageAsync(user.getProfilePicture(), profileImage);
            usernameLabel.setText(user.getUsername());
            emailLabel.setText(user.getEmail());
            fullName.setText(user.getFullName());
            getRecipes();
        } else {
            DialogUtil.showErrorDialog("Error", response.getMessage());
        }
    }

    private void getRecipes() {
        userRecipeService.getRecipeByUserId(userId, (response) -> {
            if (response.isSuccess()) {
                var recipes = response.getData();
                if (recipes != null) {
                    loadRecipeComponents(recipes, noRecipeFound);
                }
            } else {
                DialogUtil.showErrorDialog("Error", response.getMessage());
            }
        });
    }

    public void onBackPressed(MouseEvent mouseEvent) {

    }

    @Override
    protected GridPane getMenuGrid() {
        return menuGrid;
    }

    @Override
    protected VBox getProgressContainer() {
        return progressContainer;
    }

    @Override
    protected MenuListingType getMenuListingType() {
        return MenuListingType.OTHER_USER;
    }

    @Override
    protected String getScreenId() {
        return this.getClass().getSimpleName();
    }
}
