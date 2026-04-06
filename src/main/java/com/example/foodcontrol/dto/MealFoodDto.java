package com.example.foodcontrol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Meal-food link payload")
public class MealFoodDto {
    @NotNull(message = "mealId is required")
    @Positive(message = "mealId must be greater than 0")
    @Schema(description = "Meal id", example = "1")
    private Long mealId;

    @NotNull(message = "foodId is required")
    @Positive(message = "foodId must be greater than 0")
    @Schema(description = "Food id", example = "2")
    private Long foodId;

    @Positive(message = "grams must be greater than 0")
    @Schema(description = "Weight in grams", example = "120.5")
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
