package com.example.recipe.ui.user;

import com.example.recipe.utils.NavigationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.recipe.utils.LoggerUtil.logger;

public class MyRecipeController {
    @FXML
    public GridPane menuGrid;
    @FXML
    public Button btnAddRecipe;

    @FXML
    public void initialize() {
        btnAddRecipe.setOnAction(this::onAddRecipe);
        try {
            loadMenuItems();
        } catch (IOException e) {
            logger.error("Error loading menu items", e);
        }
    }

    private void onAddRecipe(ActionEvent actionEvent) {
        NavigationUtil.insertChild("add-recipe-view.fxml");
    }


    private void loadMenuItems() throws IOException {

//        List<VBox> menuItems = new ArrayList<>();
//        int column = 0;
//        int row = 1;
//        for (int i = 0; i < 10; i++) {
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("/com/example/recipe/menu-item.fxml"));
//            VBox cardBox = fxmlLoader.load();
//            menuItems.add(cardBox);
//
//            if (column == 4) {
//                column = 0;
//                ++row;
//            }
//            menuGrid.add(cardBox, column++, row);
////            GridPane.setMargin(cardBox, new Insets(10));
//        }
    }


}