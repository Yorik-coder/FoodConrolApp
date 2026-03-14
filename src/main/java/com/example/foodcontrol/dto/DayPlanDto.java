package com.example.foodcontrol.dto;

import java.time.LocalDate;
import java.util.List;

public class DayPlanDto {

    private Long id;
    private LocalDate date;
    private Long userId;
    private List<Long> mealIds;

    public DayPlanDto() {
    }

    public DayPlanDto(Long id, LocalDate date, Long userId, List<Long> mealIds) {
        this.id = id;
        this.date = date;
        this.userId = userId;
        this.mealIds = mealIds;
    }

    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
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
