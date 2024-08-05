package com.example.recipe.ui.user;

import com.example.recipe.domain.recipe.*;
import com.example.recipe.utils.NavigationUtil;
import com.example.recipe.utils.ViewUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
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
    private List<Category> categories = new ArrayList<>();
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Steps> steps = new ArrayList<>();

    @FXML
    private void initialize() {

    }

    public void onAddIngredient(ActionEvent actionEvent) {

    }

    public void onSelectImage(MouseEvent event) {

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

    public void onBackPressed(MouseEvent event) {
        NavigationUtil.insertChild("my-recipe-view.fxml");
    }

    public void onAddStep(ActionEvent actionEvent) {

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
        return validateString(recipe.getTitle(), recipeNameError, "Recipe Name") &&
                validateString(recipe.getDescription(), descriptionError, "Description") &&
                validateListings(recipe.getIngredients(), ingredientError, "Ingredients") &&
                validateListings(recipe.getCategory(), categoriesError, "Categories") &&
                validateListings(recipe.getSteps(), stepError, "Steps") &&
                validateNutritionalInformation(getNutritionalInformation());
    }

    private boolean validateNutritionalInformation(NutritionalInformation nutritionalInformation) {
        return (nutritionalInformation.getCalories() == null || validateInt(String.valueOf(nutritionalInformation.getCalories()), recipeNameError, "Calories")) &&
                (nutritionalInformation.getProtein() == null || validateDouble(String.valueOf(nutritionalInformation.getProtein()), recipeNameError, "Protein")) &&
                (nutritionalInformation.getFat() == null || validateDouble(String.valueOf(nutritionalInformation.getFat()), recipeNameError, "Fat")) &&
                (nutritionalInformation.getCarbohydrates() == null || validateDouble(String.valueOf(nutritionalInformation.getCarbohydrates()), recipeNameError, "Carbohydrates")) &&
                (nutritionalInformation.getOther() == null || validateString(nutritionalInformation.getOther(), recipeNameError, "Other"));
    }

    private boolean validateListings(List<?> list, Label fieldName, String title) {
        if (list.isEmpty()) {
            ViewUtil.setTextAndVisibility(fieldName, title + " should be added.", true);
            return false;
        }
        ViewUtil.setVisibility(fieldName, false);
        return true;
    }

    private boolean validateString(String value, Label fieldName, String title) {
        if (value == null || value.trim().isEmpty()) {
            ViewUtil.setTextAndVisibility(fieldName, title + " should be added.", true);
            return false;
        }
        ViewUtil.setVisibility(fieldName, false);
        return true;
    }

    private boolean validateInt(String value, Label fieldName, String title) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            ViewUtil.setTextAndVisibility(fieldName, title + " should be valid number", true);
            return false;
        }
        ViewUtil.setVisibility(fieldName, false);
        return true;
    }

    private boolean validateDouble(String value, Label fieldName, String title) {
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            ViewUtil.setTextAndVisibility(fieldName, title + " should be valid number", true);
            return false;
        }
        ViewUtil.setVisibility(fieldName, false);
        return true;
    }
}
