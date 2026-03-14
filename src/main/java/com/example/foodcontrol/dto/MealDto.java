package com.example.foodcontrol.dto;

import java.util.List;

public class MealDto {

    private Long id;
    private String name;
    private Long dayPlanId;
    private List<Long> mealFoodIds;

    public MealDto() {
    }

    public MealDto(Long id, String name, Long dayPlanId, List<Long> mealFoodIds) {
        this.id = id;
        this.name = name;
        this.dayPlanId = dayPlanId;
        this.mealFoodIds = mealFoodIds;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getDayPlanId() {
        return dayPlanId;
    }

    public List<Long> getMealFoodIds() {
        return mealFoodIds;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDayPlanId(Long dayPlanId) {
        this.dayPlanId = dayPlanId;
    }

    public void setMealFoodIds(List<Long> mealFoodIds) {
        this.mealFoodIds = mealFoodIds;
    }
}
