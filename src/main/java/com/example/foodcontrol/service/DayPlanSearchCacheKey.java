package com.example.foodcontrol.service;

import com.example.foodcontrol.entity.MealType;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Objects;

public final class DayPlanSearchCacheKey {

    private final String userName;
    private final MealType mealType;
    private final String foodName;
    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final Pageable pageable;
    private final boolean nativeQuery;

    public DayPlanSearchCacheKey(String userName,
                                 MealType mealType,
                                 String foodName,
                                 LocalDate fromDate,
                                 LocalDate toDate,
                                 Pageable pageable,
                                 boolean nativeQuery) {

        this.userName = userName;
        this.mealType = mealType;
        this.foodName = foodName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.pageable = pageable;
        this.nativeQuery = nativeQuery;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof DayPlanSearchCacheKey that)) {
            return false;
        }
        return nativeQuery == that.nativeQuery
                && Objects.equals(userName, that.userName)
                && mealType == that.mealType
                && Objects.equals(foodName, that.foodName)
                && Objects.equals(fromDate, that.fromDate)
                && Objects.equals(toDate, that.toDate)
                && Objects.equals(pageable, that.pageable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, mealType, foodName, fromDate, toDate, pageable, nativeQuery);
    }
}