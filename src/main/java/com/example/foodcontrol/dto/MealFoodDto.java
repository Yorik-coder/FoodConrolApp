package com.example.foodcontrol.dto;

public class MealFoodDto {

    private Long id;
    private Long mealId;
    private Long foodId;
    private double grams;

    public MealFoodDto() {
    }

    public MealFoodDto(Long id, Long mealId, Long foodId, double grams) {
        this.id = id;
        this.mealId = mealId;
        this.foodId = foodId;
        this.grams = grams;
    }

    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
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
