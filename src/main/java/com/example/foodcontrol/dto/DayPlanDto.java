package com.example.foodcontrol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Day plan payload")
public class DayPlanDto {

    @NotNull(message = "date is required")
    @FutureOrPresent(message = "date must be today or in the future")
    @Schema(description = "Planned date", example = "2026-04-10")
    private LocalDate date;

    @NotNull(message = "userId is required")
    @Positive(message = "userId must be greater than 0")
    @Schema(description = "Owner user id", example = "1")
    private Long userId;

    @Schema(description = "Related meal ids")
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
