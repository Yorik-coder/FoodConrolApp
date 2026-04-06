package com.example.foodcontrol.dto;

import java.util.List;

import com.example.foodcontrol.entity.MealType;

public class MealDto {
    private MealType mealType;
    private Long dayPlanId;
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
