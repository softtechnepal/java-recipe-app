package com.example.recipe.domain.recipe;

import com.example.recipe.domain.User;

import java.sql.Timestamp;
import java.util.List;

public class Recipe {
    private Long recipeId;
    private Integer userId;
    private String title;
    private String description;
    private String image;
    private String warnings;
    private String videoUrl;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer totalServings;
    private Integer prepTime;
    private User user;
    private boolean isSaved;

    private NutritionalInformation nutritionalInformation;

    private List<Category> category;
    private List<Ingredient> ingredients;
    private List<Steps> steps;

    public Recipe() {
    }

    public Recipe(Long recipeId, Integer userId, String title, String description, String image, String warnings, String videoUrl, Timestamp createdAt, Timestamp updatedAt, NutritionalInformation nutritionalInformation, List<Category> category, List<Ingredient> ingredients, List<Steps> steps) {
        this.recipeId = recipeId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.image = image;
        this.warnings = warnings;
        this.videoUrl = videoUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.nutritionalInformation = nutritionalInformation;
        this.category = category;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        if (description == null) return "Description not available";
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWarnings() {
        return warnings;
    }

    public void setWarnings(String warnings) {
        this.warnings = warnings;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public NutritionalInformation getNutritionalInformation() {
        return nutritionalInformation;
    }

    public void setNutritionalInformation(NutritionalInformation nutritionalInformation) {
        this.nutritionalInformation = nutritionalInformation;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    // Getters and setters for the new field
    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getTotalServings() {
        return totalServings;
    }

    public void setTotalServings(Integer totalServings) {
        this.totalServings = totalServings;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }
}
