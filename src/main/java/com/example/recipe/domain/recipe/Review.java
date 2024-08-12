package com.example.recipe.domain.recipe;

import com.example.recipe.domain.User;

import java.time.LocalDateTime;

public class Review {
    private long id;
    private long recipeId;
    private long userId;
    private String review;
    private int rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private User user;

    public Review() {
    }

    public Review(long id, long recipeId, long userId, String review, int rating, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.recipeId = recipeId;
        this.userId = userId;
        this.review = review;
        this.rating = rating;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
