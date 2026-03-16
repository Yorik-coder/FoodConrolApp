package com.example.foodcontrol.dto;

import java.time.LocalDate;
import java.util.List;

public class DayPlanDto {

    private LocalDate date;
    private Long userId;
    private List<Long> mealIds;

    public DayPlanDto() {
    }

    public DayPlanDto(LocalDate date, Long userId, List<Long> mealIds) {
        this.date = date;
        this.userId = userId;
        this.mealIds = mealIds;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getUserId() {
        return userId;
    }

    public List<Long> getMealIds() {
        return mealIds;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setMealIds(List<Long> mealIds) {
        this.mealIds = mealIds;
    }
}
