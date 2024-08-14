package com.example.recipe.domain;

public class AdminDashboard {
    private  Integer usersCount;
    private  Integer activeUsersCount;
    private  Integer recipesCount;
    private  Integer categoriesCount;
    private  Integer ingredientsCount;
    private  Integer reviewsCount;

    public AdminDashboard() {
    }

    public AdminDashboard(Integer usersCount, Integer activeUsersCount, Integer recipesCount, Integer categoriesCount, Integer ingredientsCount, Integer reviewsCount) {
        this.usersCount = usersCount;
        this.activeUsersCount = activeUsersCount;
        this.recipesCount = recipesCount;
        this.categoriesCount = categoriesCount;
        this.ingredientsCount = ingredientsCount;
        this.reviewsCount = reviewsCount;
    }

    public Integer getUsersCount() {
        return usersCount;
    }
    public void setUsersCount(Integer usersCount) {
        this.usersCount = usersCount;
    }
    public Integer getActiveUsersCount() {
        return activeUsersCount;
    }
    public void setActiveUsersCount(Integer activeUsersCount) {
        this.activeUsersCount = activeUsersCount;
    }
    public  Integer getRecipesCount() {
        return recipesCount;
    }
    public void setRecipesCount(Integer recipesCount) {
        this.recipesCount = recipesCount;
    }
    public Integer getCategoriesCount() {
        return categoriesCount;
    }
    public void setCategoriesCount(Integer categoriesCount) {
        this.categoriesCount = categoriesCount;
    }
    public Integer getIngredientsCount() {
        return ingredientsCount;
    }
    public void setIngredientsCount(Integer ingredientsCount) {
        this.ingredientsCount = ingredientsCount;
    }
    public Integer getReviewsCount() {
        return reviewsCount;
    }
    public void setReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

}

