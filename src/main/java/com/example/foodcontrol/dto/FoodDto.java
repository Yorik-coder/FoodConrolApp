package com.example.foodcontrol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Schema(description = "Food payload")
public class FoodDto {

    @NotBlank(message = "name is required")
    @Size(max = 120, message = "name length must be <= 120")
    @Schema(description = "Food name", example = "Chicken breast")
    private String name;

    @Min(value = 0, message = "calories must be >= 0")
    @Schema(description = "Calories per 100g", example = "165")
    private int calories;

    @PositiveOrZero(message = "protein must be >= 0")
    @Schema(description = "Protein per 100g", example = "31.0")
    private double protein;

    @PositiveOrZero(message = "fat must be >= 0")
    @Schema(description = "Fat per 100g", example = "3.6")
    private double fat;

    @PositiveOrZero(message = "carbs must be >= 0")
    @Schema(description = "Carbs per 100g", example = "0.0")
    private double carbs;

    public FoodDto() {
    }

    public FoodDto(String name, int calories, double protein, double fat, double carbs) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }
}
