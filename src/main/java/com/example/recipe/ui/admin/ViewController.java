package com.example.recipe.ui.admin;

import com.example.recipe.utils.NavigationUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import java.io.IOException;

public class ViewController {
    @FXML
    public Button dashboardButton;
    @FXML
    public Button usersButton;
    @FXML
    public Button recipeButton;
    @FXML
    public Button categoryButton;
    public Text pageDescription;
    @FXML
    private ScrollPane containerPane;

    @FXML
    private Text pageTitle;

    @FXML
    private void initialize() {
        // Load initial content
        loadContent("Dashboard", "admin/dashboard.fxml","This page shows the overview analytics of the entire recipe application");

        // Set button actions
        dashboardButton.setOnAction(e -> loadContent("Dashboard", "admin/dashboard.fxml","This page shows the overview analytics of the entire recipe application"));
        usersButton.setOnAction(e -> loadContent("Users", "admin/users.fxml","This page shows the shows the total users of entire recipe application"));
        recipeButton.setOnAction(e -> loadContent("Recipe", "admin/recipe.fxml","This page shows the shows the total recipe of entire recipe application"));
        categoryButton.setOnAction(e -> loadContent("Category", "admin/categories.fxml","This page shows the shows the total categories of entire recipe application"));
    }

    public void loadContent(String title, String fxmlPath, String desc) {
        try {
            if (pageTitle == null || containerPane == null) {
                System.err.println("Error: pageTitle or containerPane is null.");
                return;
            }

            pageTitle.setText(title);
            pageDescription.setText(desc);

            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource("/com/example/recipe/" + fxmlPath));
            Pane newContent = loader.load();

            // Set content to ScrollPane
            containerPane.setContent(newContent);

            // Ensure scroll policies are set
            containerPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            containerPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        } catch (IOException e) {
            System.err.println("Failed to load content: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
