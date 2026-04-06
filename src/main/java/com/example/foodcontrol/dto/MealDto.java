package com.example.foodcontrol.dto;

import java.util.List;

import com.example.foodcontrol.entity.MealType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Meal payload")
public class MealDto {
    @NotNull(message = "mealType is required")
    @Schema(description = "Meal type", example = "BREAKFAST")
    private MealType mealType;

    @NotNull(message = "dayPlanId is required")
    @Positive(message = "dayPlanId must be greater than 0")
    @Schema(description = "Related day plan id", example = "1")
    private Long dayPlanId;

    @Schema(description = "Related meal-food ids")
    private List<Long> mealFoodIds;

    public MealDto() {
    }

    public MealDto(MealType mealType, Long dayPlanId, List<Long> mealFoodIds) {
        this.mealType = mealType;
        this.dayPlanId = dayPlanId;
        this.mealFoodIds = mealFoodIds;
    }

    public MealType getMealType() {
        return mealType;
    }

    public Long getDayPlanId() {
        return dayPlanId;
    }

    public List<Long> getMealFoodIds() {
        return mealFoodIds;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public void setDayPlanId(Long dayPlanId) {
        this.dayPlanId = dayPlanId;
    }

    public void setMealFoodIds(List<Long> mealFoodIds) {
        this.mealFoodIds = mealFoodIds;
    }
}
