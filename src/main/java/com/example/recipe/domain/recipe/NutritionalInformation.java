package com.example.recipe.domain.recipe;

public class NutritionalInformation {
    private Long recipeId;
    private Integer calories;
    private Double protein;
    private Double fat;
    private Double carbohydrates;
    private String other;

    public NutritionalInformation() {
    }
    public NutritionalInformation(Long recipeId, Integer calories, Double protein, Double fat, Double carbohydrates, String other) {
        this.recipeId = recipeId;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.other = other;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(Double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
