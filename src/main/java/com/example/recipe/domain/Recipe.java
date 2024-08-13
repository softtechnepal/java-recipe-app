package com.example.recipe.domain;

import java.util.ArrayList;

public class Recipe {
    private long recipeId;
    private long userId;
    private String title;
    private String description;
    private String image;
    private String videoUrl;
    private String createdAt;
    private String userName;
    private String ingredients;
    private String categories;
    private int totalReviews;
    private int totalSaved;

    public Recipe() {
    }

    public Recipe(long recipeId, long userId, String title, String description, String image, String videoUrl, String createdAt, String userName, String ingredients, String categories, int totalReviews, int totalSaved) {
        this.recipeId = recipeId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.image = image;
        this.videoUrl = videoUrl;
        this.createdAt = createdAt;
        this.userName = userName;
        this.ingredients = ingredients;
        this.categories = categories;
        this.totalReviews = totalReviews;
        this.totalSaved = totalSaved;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getIngredients() {
        return ingredients;
    }
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
    public String getCategories() {
        return categories;
    }
    public void setCategories(String categories) {
        this.categories = categories;
    }
    public int getTotalReviews() {
        return totalReviews;
    }
    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }
    public int getTotalSaved() {
        return totalSaved;
    }
    public void setTotalSaved(int totalSaved) {
        this.totalSaved = totalSaved;
    }
}
