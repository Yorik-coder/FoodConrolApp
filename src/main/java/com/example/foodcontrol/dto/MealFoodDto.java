package com.example.foodcontrol.dto;

public class MealFoodDto {
    private Long mealId;
    private Long foodId;
    private double grams;

    public MealFoodDto() {
    }

    public MealFoodDto(Long mealId, Long foodId, double grams) {
        this.mealId = mealId;
        this.foodId = foodId;
        this.grams = grams;
    }

    public Long getMealId() {
        return mealId;
    }

    public Long getFoodId() {
        return foodId;
    }

    public double getGrams() {
        return grams;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }

    public void setGrams(double grams) {
        this.grams = grams;
    }
}
