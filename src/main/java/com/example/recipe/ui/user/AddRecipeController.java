package com.example.recipe.ui.user;

import com.example.recipe.domain.recipe.*;
import com.example.recipe.ui.dialogs.CategoryDialog;
import com.example.recipe.ui.dialogs.IngredientDialog;
import com.example.recipe.utils.NavigationUtil;
import com.example.recipe.utils.ValidationUtil;
import com.example.recipe.utils.ViewUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeController {
    @FXML
    public TextField tfRecipeName;
    @FXML
    public TextField tfRecipeDescription;
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
    @FXML
    public TextField tfStepDescription;
    @FXML
    public TextField tfStepTitle;

    @FXML
    public VBox vBoxAddedSteps;
    @FXML
    public TextField tfIngredientName;
    @FXML
    public TextField tfCategories;
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

    private String imagePath;
    private final List<Category> categories = new ArrayList<>();
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final List<Steps> steps = new ArrayList<>();

    @FXML
    private void initialize() {
        tfIngredientName.setOnMouseClicked((event) -> onAddIngredient(null));
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

        }
    }

    public void onAddIngredient(ActionEvent actionEvent) {
        var dialog = new IngredientDialog("Add Ingredient", data -> {
            if (data != null)
                ingredients.add(data);

            StringBuilder ingredientNames = new StringBuilder();
            ingredients.forEach(ingredient -> ingredientNames.append(ingredient.getIngredientName()).append(", "));
            tfIngredientName.setText(ingredientNames.toString());
        });
        dialog.showAndWait();
    }

    public void onAddCategories(ActionEvent actionEvent) {
        var dialog = new CategoryDialog("Add Categories", List.of("Breakfast", "Lunch", "Dinner", "Snack", "Dessert", "Appetizer", "Drink", "Other"));
        dialog.showAndWait();
    }

    public void onAddStep(ActionEvent actionEvent) {

    }

    public void onSelectImage(MouseEvent event) {

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
