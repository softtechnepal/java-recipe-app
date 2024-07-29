package com.example.recipe.domain;

public class Recipe {
    private long recipeId;
    private long userId;
    private String title;
    private String description;
    private String image;
    private String videoUrl;
    private String createdAt;

    public Recipe() {
    }

    public Recipe(long recipeId, long userId, String title, String description, String image, String videoUrl, String createdAt) {
        this.recipeId = recipeId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.image = image;
        this.videoUrl = videoUrl;
        this.createdAt = createdAt;
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
}
