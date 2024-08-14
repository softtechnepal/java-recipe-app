package com.example.recipe.ui.user;

import com.example.recipe.Constants;
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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
    public TextField tfProtein;
    @FXML
    public TextField tfFat;
    @FXML
    public TextField tfCarbohydrates;
    @FXML
    public TextField tfOther;
    @FXML
    public TextField tfWarnings;
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
    public Label carbohydratesError;
    @FXML
    public Label fatError;
    @FXML
    public Label proteinError;
    @FXML
    public Label caloriesError;
    @FXML
    public Label imageError;
    @FXML
    public Label videoError;
    @FXML
    public Text textTitle;
    @FXML
    public Button btnUpdateRecipe;
    @FXML
    public TextField tfTotalServings;
    @FXML
    public Label totalServingError;
    @FXML
    public TextField tvPreparationTime;
    @FXML
    public Label preparationTimeError;

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
    private Recipe updateRecipe;

    public static void navigateToEditRecipe(Map<String, Recipe> params) {
        NavigationUtil.insertChild("add-recipe-view.fxml", params);
    }

    public static void navigateToAddRecipe() {
        NavigationUtil.insertChild("add-recipe-view.fxml");
    }

    @FXML
    private void initialize() {
        updateRecipe = (Recipe) NavigationUtil.getParam(Constants.RECIPE_DETAIL_PARAM);
        if (updateRecipe != null) {
            loadUpdateRecipe(updateRecipe);
        }
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

    private void loadUpdateRecipe(Recipe updateRecipe) {
        textTitle.setText("Edit Recipe");
        tfRecipeName.setText(updateRecipe.getTitle());
        tfRecipeDescription.setText(updateRecipe.getDescription());
        tfTotalServings.setText(String.valueOf(updateRecipe.getTotalServings()));
        tvPreparationTime.setText(String.valueOf(updateRecipe.getPrepTime()));

        categories.addAll(updateRecipe.getCategory());
        addCategoriesToGridPane(categories.stream().map(Category::getCategoryName).toList());

        ingredients.addAll(updateRecipe.getIngredients());
        steps.addAll(updateRecipe.getSteps());

        imagePath = updateRecipe.getImage();
        addSelectedImage(updateRecipe.getImage());

        tfVideoLink.setText(updateRecipe.getVideoUrl());
        tfWarnings.setText(updateRecipe.getWarnings());

        tfCalories.setText(String.valueOf(updateRecipe.getNutritionalInformation().getCalories()));
        tfProtein.setText(String.valueOf(updateRecipe.getNutritionalInformation().getProtein()));
        tfFat.setText(String.valueOf(updateRecipe.getNutritionalInformation().getFat()));
        tfCarbohydrates.setText(String.valueOf(updateRecipe.getNutritionalInformation().getCarbohydrates()));
        tfOther.setText(updateRecipe.getNutritionalInformation().getOther());

        btnUpdateRecipe.setText("Update Recipe");
    }

    public void onAddRecipe(ActionEvent actionEvent) {
        Recipe recipe = new Recipe();
        if (updateRecipe != null) {
            recipe.setRecipeId(updateRecipe.getRecipeId());
        }
        recipe.setTitle(tfRecipeName.getText());
        recipe.setDescription(tfRecipeDescription.getText());
        recipe.setIngredients(ingredients);
        recipe.setCategory(categories);
        recipe.setSteps(steps);
        recipe.setImage(imagePath);
        recipe.setVideoUrl(tfVideoLink.getText());
        recipe.setWarnings(tfWarnings.getText());

        if (validateRecipe(recipe) && getNutritionalInformation() != null) {
            recipe.setNutritionalInformation(getNutritionalInformation());

            if (updateRecipe == null) {
                userRecipeService.addRecipe(recipe, response -> {
                    if (response.isSuccess()) {
                        DialogUtil.showInfoDialog("Success", "Recipe added successfully");
                        NavigationUtil.insertChild("my-recipe-view.fxml");
                    } else {
                        DialogUtil.showErrorDialog("Error", response.getMessage());
                    }
                });
            } else {
                userRecipeService.updateRecipe(recipe, response -> {
                    if (response.isSuccess()) {
                        DialogUtil.showInfoDialog("Success", "Recipe updated successfully");
                        Map<String, Long> params = Map.of(Constants.RECIPE_ID_PARAM, updateRecipe.getRecipeId());
                        RecipeDetailController.navigate(params);
                    } else {
                        DialogUtil.showErrorDialog("Error", response.getMessage());
                    }
                });
            }
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
        });
        dialog.showAndWait();
    }

    private void handleEditStep(Steps step) {
        var dialog = new AddStepDialog("Edit Step", step, (Steps data) -> {
            if (data != null) {
                int index = steps.indexOf(step);
                if (index != -1) {
                    steps.set(index, data);
                } else {
                    steps.add(data);
                }
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
            addSelectedImage(imagePath);
        }
    }

    private void addSelectedImage(String imagePath) {
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

    public void onBackPressed(MouseEvent event) {
        NavigationUtil.insertChild("my-recipe-view.fxml");
    }

    private NutritionalInformation getNutritionalInformation() {
        NutritionalInformation nutritionalInformation = new NutritionalInformation();

        if (updateRecipe != null) {
            nutritionalInformation.setRecipeId(updateRecipe.getNutritionalInformation().getRecipeId());
        }

        if (!isValidIntNutrition(tfCalories.getText(), caloriesError, "Calories", nutritionalInformation::setCalories)) {
            return null;
        }

        if (!invalidDoubleNutrition(tfProtein.getText(), proteinError, "Protein", nutritionalInformation::setProtein)) {
            return null;
        }

        if (!invalidDoubleNutrition(tfFat.getText(), fatError, "Fat", nutritionalInformation::setFat)) {
            return null;
        }

        if (!invalidDoubleNutrition(tfCarbohydrates.getText(), carbohydratesError, "Carbohydrates", nutritionalInformation::setCarbohydrates)) {
            return null;
        }

        nutritionalInformation.setOther(tfOther.getText());
        return nutritionalInformation;
    }

    private boolean isValidIntNutrition(String value, Label errorLabel, String fieldName, Consumer<Integer> setter) {
        if (value.isEmpty()) {
            ViewUtil.setVisibility(errorLabel, false);
            return true;
        }
        if (ValidationUtil.isValidInt(value, errorLabel, fieldName)) {
            setter.accept(Integer.parseInt(value));
            return true;
        }
        return false;
    }

    private boolean invalidDoubleNutrition(String value, Label errorLabel, String fieldName, Consumer<Double> setter) {
        logger.error("Value: {}", value);
        if (value.isEmpty()) {
            ViewUtil.setVisibility(errorLabel, false);
            return true;
        }
        if (ValidationUtil.isValidDouble(value, errorLabel, fieldName)) {
            setter.accept(Double.parseDouble(value));
            return true;
        }
        return false;
    }

    private boolean validateRecipe(Recipe recipe) {
        return ValidationUtil.isValidString(recipe.getTitle(), recipeNameError, "Recipe Name") &&
                ValidationUtil.isValidString(recipe.getDescription(), descriptionError, "Description") &&
                isValidIntNutrition(tfTotalServings.getText(), totalServingError, "Total Servings", recipe::setTotalServings) &&
                isValidIntNutrition(tvPreparationTime.getText(), preparationTimeError, "Preparation Time", recipe::setPrepTime) &&
                ValidationUtil.isValidList(recipe.getCategory(), categoriesError, "Categories") &&
                ValidationUtil.isValidList(recipe.getIngredients(), ingredientError, "Ingredients") &&
                ValidationUtil.isValidList(recipe.getSteps(), stepError, "Steps") &&
                ValidationUtil.isValidString(recipe.getImage(), imageError, "Image") &&
                ValidationUtil.isValidWebURL(recipe.getVideoUrl(), videoError, "Video");

    }
}
