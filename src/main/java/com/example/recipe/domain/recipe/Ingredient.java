package com.example.recipe.domain.recipe;


public class Ingredient {
    private Long ingredientId;
    private Long recipeId;
    private String ingredientName;
    private Double quantity;
    private String unit;
    private String ingredientImage;

    public Ingredient() {
    }
    public Ingredient(Long ingredientId, Long recipeId, String ingredientName, Double quantity, String unit, String ingredientImage) {
        this.ingredientId = ingredientId;
        this.recipeId = recipeId;
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unit = unit;
        this.ingredientImage = ingredientImage;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIngredientImage() {
        return ingredientImage;
    }

    public void setIngredientImage(String ingredientImage) {
        this.ingredientImage = ingredientImage;
    }
}