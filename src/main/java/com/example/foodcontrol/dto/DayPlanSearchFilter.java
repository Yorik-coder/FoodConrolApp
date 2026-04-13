package com.example.foodcontrol.dto;

import com.example.foodcontrol.entity.MealType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Schema(description = "Search filter for day plans")
public class DayPlanSearchFilter {

    @Size(max = 100, message = "userName length must be <= 100")
    @Schema(description = "User name substring", example = "Иван Иванов")
    private String userName;

    @Schema(description = "Meal type filter", example = "BREAKFAST")
    private MealType mealType;

    @Size(max = 100, message = "foodName length must be <= 100")
    @Schema(description = "Food name substring", example = "egg")
    private String foodName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "Start date inclusive", example = "2026-01-01")
    private LocalDate fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "End date inclusive", example = "2026-12-31")
    private LocalDate toDate;

    @Schema(description = "Use native SQL query instead of JPQL", example = "false")
    private boolean useNative;

    @AssertTrue(message = "fromDate must be before or equal to toDate")
    public boolean isDateRangeValid() {
        return fromDate == null || toDate == null || !fromDate.isAfter(toDate);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public boolean isUseNative() {
        return useNative;
    }

    public void setUseNative(boolean useNative) {
        this.useNative = useNative;
    }
}