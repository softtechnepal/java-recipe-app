package com.example.recipe.domain.recipe;

public class Steps {
    private Long stepId;
    private Integer stepOrder;
    private String stepName;
    private String stepDescription;
    private Long recipeId;

    public Steps(Long stepId, Integer stepOrder, String stepName, String stepDescription, Long recipeId) {
        this.stepId = stepId;
        this.stepOrder = stepOrder;
        this.stepName = stepName;
        this.stepDescription = stepDescription;
        this.recipeId = recipeId;
    }

    public Long getStepId() {
        return stepId;
    }

    public void setStepId(Long stepId) {
        this.stepId = stepId;
    }

    public Integer getStepOrder() {
        return stepOrder;
    }

    public void setStepOrder(Integer stepOrder) {
        this.stepOrder = stepOrder;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }
}
