package com.example.foodcontrol.mapper;

import com.example.foodcontrol.dto.DayPlanDto;
import com.example.foodcontrol.entity.DayPlan;
import com.example.foodcontrol.entity.Meal;

import java.util.List;
import java.util.stream.Collectors;

public class DayPlanMapper {

    public static DayPlanDto toDto(DayPlan plan) {

        if (plan == null) {
            return null;
        }

        DayPlanDto dto = new DayPlanDto();

        dto.setId(plan.getId());
        dto.setDate(plan.getDate());

        if (plan.getUser() != null) {
            dto.setUserId(plan.getUser().getId());
        }

        if (plan.getMeals() != null) {
            List<Long> mealIds = plan.getMeals()
                    .stream()
                    .map(Meal::getId)
                    .collect(Collectors.toList());

            dto.setMealIds(mealIds);
        }

        return dto;
    }
}
