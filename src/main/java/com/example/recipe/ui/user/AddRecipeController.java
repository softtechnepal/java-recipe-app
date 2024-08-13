package com.example.recipe.ui.user;

import com.example.recipe.domain.recipe.*;
import com.example.recipe.services.user.UserCategoryService;
import com.example.recipe.services.user.UserRecipeService;
import com.example.recipe.ui.dialogs.AddStepDialog;
import com.example.recipe.ui.dialogs.CategoryDialog;
import com.example.recipe.ui.dialogs.IngredientDialog;
import com.example.recipe.utils.DialogUtil;
import com.example.recipe.utils.NavigationUtil;
import com.example.recipe.utils.ValidationUtil;
import com.example.recipe.utils.ViewUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.recipe.utils.LoggerUtil.logger;

public class AddRecipeController {
    @FXML
    public TextField tfRecipeName;
    @FXML
    public TextArea tfRecipeDescription;
    @FXML
    public TextField tfVideoLink;
    @FXML
    public TextField tfCalories;
    @FXML
    public Label tfProtein;
    @FXML
    public TextField tfFat;
    @FXML
    public TextField tfCarbohydrates;
    @FXML
    public TextField tfOther;
    @FXML
    public TextField tfWarnings;
    /*@FXML
    public TextField tfCategories;*/
    @FXML
    public Label recipeNameError;
    @FXML
    public Label descriptionError;
    @FXML
    public Label ingredientError;
    @FXML
    public Label categoriesError;
    @FXML
    public Label stepError;
    @FXML
    public HBox hBoxImageSection;
    @FXML
    public GridPane gridCategories;
    @FXML
    private TableView<Ingredient> ingredientsTable;
    @FXML
    private TableColumn<Ingredient, String> ingredientNameColumn;
    @FXML
    private TableColumn<Ingredient, String> ingredientQtyColumn;
    @FXML
    private TableColumn<Ingredient, String> ingredientUnitColumn;
    @FXML
    private TableColumn<Ingredient, Void> actionsColumn;
    @FXML
    private TableView<Steps> stepsTable;
    @FXML
    private TableColumn<Steps, String> stepNameColumn;
    @FXML
    private TableColumn<Steps, String> stepDescColumn;
    @FXML
    private TableColumn<Steps, Void> stepActionsColumn;


    private String imagePath;
    private final List<Category> categories = new ArrayList<>();
    private final ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();
    private final ObservableList<Steps> steps = FXCollections.observableArrayList();
    private final UserCategoryService userCategoryService = new UserCategoryService();
    private final UserRecipeService userRecipeService = new UserRecipeService();

    @FXML
    private void initialize() {
        // tfCategories.setOnMouseClicked((event) -> onAddCategories(null));
        // TODO: Prachan: CRUD For Ingredients
        ingredientNameColumn.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
        ingredientUnitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        ingredientQtyColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox pane = new HBox(10, editButton, deleteButton);

            {
                editButton.getStyleClass().add("table-header-button");
                deleteButton.getStyleClass().add("table-header-button");
            }

            {
                editButton.setOnAction(event -> {
                    Ingredient ingredient = getTableView().getItems().get(getIndex());
                    if (ingredient != null) {
                        handleEditIngredient(ingredient);
                    }
                });
                deleteButton.setOnAction(event -> {
                    Ingredient ingredient = getTableView().getItems().get(getIndex());
                    ingredients.remove(ingredient);
                });
            }


            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });

        ingredientsTable.setItems(ingredients);


        // TODO: Prachan: CRUD For Steps
        stepNameColumn.setCellValueFactory(new PropertyValueFactory<>("stepName"));
        stepDescColumn.setCellValueFactory(new PropertyValueFactory<>("stepDescription"));
        stepActionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox pane = new HBox(10, editButton, deleteButton);

            {
                editButton.getStyleClass().add("table-header-button");
                deleteButton.getStyleClass().add("table-header-button");
            }

            {
                editButton.setOnAction(event -> {
                    Steps step = getTableView().getItems().get(getIndex());
                    if (step != null) {
                        handleEditStep(step);
                    }
                });
                deleteButton.setOnAction(event -> {
                    Steps step = getTableView().getItems().get(getIndex());
                    steps.remove(step);
                });
            }


            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });

        stepsTable.setItems(steps);

    }


    public void onAddRecipe(ActionEvent actionEvent) {
        Recipe recipe = new Recipe();
        recipe.setTitle(tfRecipeName.getText());
        recipe.setDescription(tfRecipeDescription.getText());
        recipe.setImage(imagePath);
        recipe.setWarnings(tfWarnings.getText());
        recipe.setVideoUrl(tfVideoLink.getText());
        recipe.setCategory(categories);
        recipe.setIngredients(ingredients);
        recipe.setSteps(steps);

        if (validateRecipe(recipe)) {
            recipe.setNutritionalInformation(getNutritionalInformation());
            userRecipeService.addRecipe(recipe, response -> {
                if (response.isSuccess()) {
                    DialogUtil.showInfoDialog("Success", "Recipe added successfully");
                    // NavigationUtil.insertChild("my-recipe-view.fxml");
                } else {
                    DialogUtil.showErrorDialog("Error", response.getMessage());
                }
            });
        }
    }

    public void onAddIngredient(ActionEvent actionEvent) {
        var dialog = new IngredientDialog("Add Ingredient", null, (Ingredient data) -> {
            if (data != null)
                ingredients.add(data);
        });
        dialog.showAndWait();
    }

    private void handleEditIngredient(Ingredient ingredient) {
        var dialog = new IngredientDialog("Edit Ingredient", ingredient, (Ingredient data) -> {
            if (data != null) {
                ingredients.remove(ingredient);
                ingredients.add(data);
            }
        });
        dialog.showAndWait();
    }

    public void onAddCategories(ActionEvent actionEvent) {
        userCategoryService.getAllCategories(response -> {
            if (response.isSuccess()) {
                userCategoryService.setCategories(response.getData());
                var dialog = new CategoryDialog("Add Categories", categories, response.getData(), data -> {
                    categories.clear();
                    categories.addAll(data);
                    addCategoriesToGridPane(categories.stream().map(Category::getCategoryName).toList());
                });
                dialog.showAndWait();
            }
        });
    }

    private void addCategoriesToGridPane(List<String> categories) {
        gridCategories.getChildren().clear(); // Clear existing items
        int columns = 6; // Maximum 5 items per row
        for (int i = 0; i < categories.size(); i++) {
            Label categoryLabel = new Label(categories.get(i));
            categoryLabel.getStyleClass().add("grid-item");
            int row = i / columns;
            int col = i % columns;
            gridCategories.add(categoryLabel, col, row);
        }
    }

    public void onAddStep(ActionEvent actionEvent) {
        var dialog = new AddStepDialog("Add Step", null, data -> {
            if (data != null)
                steps.add(data);

//            List<VBox> stepViews = new ArrayList<>();
//            steps.forEach(step -> {
//                VBox vBox = new VBox(4);
//                vBox.setStyle("-fx-background-radius: 4px; -fx-padding: 10px; -fx-background-color:  #dcdcdc; " +
//                        "-fx-border-radius: 4px; -fx-max-width: 500px; -fx-border-color: #dcdcdc; -fx-border-width: 1px");
//                Label title = new Label(step.getStepName());
//                title.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: #293846");
//                Text description = new Text(step.getStepDescription());
//                description.setStyle("-fx-font-size: 16px; -fx-text-fill:  #2d2d2d; -fx-wrap-text: true; max-width: 400px");
//                description.setWrappingWidth(480);
//                vBox.getChildren().addAll(title, description);
//                stepViews.add(vBox);
//            });
//
//            vBoxAddedSteps.getChildren().clear();
//            vBoxAddedSteps.getChildren().addAll(stepViews);
        });
        dialog.showAndWait();
    }

    private void handleEditStep(Steps step) {
        var dialog = new AddStepDialog("Edit Step", step, (Steps data) -> {
            if (data != null) {
                steps.remove(step);
                steps.add(data);
            }
        });
        dialog.showAndWait();
    }

    public void onSelectImage(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (selectedFile != null) {

            imagePath = selectedFile.getAbsolutePath();
            logger.info("Selected file: {}", imagePath);
            Image image = new Image("file:" + imagePath);
            ImageView imageView = new ImageView();
            imageView.setId("recipe-image");
            imageView.setImage(image);
            imageView.setFitHeight(200);
            imageView.setFitWidth(200);
            imageView.setPreserveRatio(true);

            hBoxImageSection.getChildren().clear();
            hBoxImageSection.getChildren().add(imageView);
        }
    }

    public void onBackPressed(MouseEvent event) {
        NavigationUtil.insertChild("my-recipe-view.fxml");
    }

    private NutritionalInformation getNutritionalInformation() {
        NutritionalInformation nutritionalInformation = new NutritionalInformation();
        try {
            if (!tfCalories.getText().isEmpty()) {
                nutritionalInformation.setCalories(Integer.parseInt(tfCalories.getText()));
            }
        } catch (NumberFormatException e) {
            ViewUtil.setTextAndVisibility(recipeNameError, "Calories should be a valid number", true);
        }
        try {
            nutritionalInformation.setProtein(Double.parseDouble(tfProtein.getText()));
        } catch (NumberFormatException e) {
            ViewUtil.setTextAndVisibility(recipeNameError, "Protein should be a valid number", true);
        }
        try {
            nutritionalInformation.setFat(Double.parseDouble(tfFat.getText()));
        } catch (NumberFormatException e) {
            ViewUtil.setTextAndVisibility(recipeNameError, "Fat should be a valid number", true);
        }
        try {
            nutritionalInformation.setCarbohydrates(Double.parseDouble(tfCarbohydrates.getText()));
        } catch (NumberFormatException e) {
            ViewUtil.setTextAndVisibility(recipeNameError, "Carbohydrates should be a valid number", true);
        }
        nutritionalInformation.setOther(tfOther.getText());
        return nutritionalInformation;
    }

    private boolean validateRecipe(Recipe recipe) {
        return ValidationUtil.validateString(recipe.getTitle(), recipeNameError, "Recipe Name") &&
                ValidationUtil.validateString(recipe.getDescription(), descriptionError, "Description") &&
                ValidationUtil.validateListings(recipe.getIngredients(), ingredientError, "Ingredients") &&
                ValidationUtil.validateListings(recipe.getCategory(), categoriesError, "Categories") &&
                ValidationUtil.validateListings(recipe.getSteps(), stepError, "Steps") &&
                validateNutritionalInformation(getNutritionalInformation());
    }

    private boolean validateNutritionalInformation(NutritionalInformation nutritionalInformation) {
        return (nutritionalInformation.getCalories() == null || ValidationUtil.validateInt(String.valueOf(nutritionalInformation.getCalories()), recipeNameError, "Calories")) &&
                (nutritionalInformation.getProtein() == null || ValidationUtil.validateDouble(String.valueOf(nutritionalInformation.getProtein()), recipeNameError, "Protein")) &&
                (nutritionalInformation.getFat() == null || ValidationUtil.validateDouble(String.valueOf(nutritionalInformation.getFat()), recipeNameError, "Fat")) &&
                (nutritionalInformation.getCarbohydrates() == null || ValidationUtil.validateDouble(String.valueOf(nutritionalInformation.getCarbohydrates()), recipeNameError, "Carbohydrates")) &&
                (nutritionalInformation.getOther() == null || ValidationUtil.validateString(nutritionalInformation.getOther(), recipeNameError, "Other"));
    }
}
