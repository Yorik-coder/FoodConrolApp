package com.example.foodcontrol.mapper;

import com.example.foodcontrol.dto.MealDto;
import com.example.foodcontrol.entity.Meal;
import com.example.foodcontrol.entity.MealFood;

import java.util.List;
import java.util.stream.Collectors;

public class MealMapper {

    public static MealDto toDto(Meal meal) {

        if (meal == null) {
            return null;
        }

        MealDto dto = new MealDto();

        dto.setId(meal.getId());
        dto.setName(meal.getName());

        if (meal.getDayPlan() != null) {
            dto.setDayPlanId(meal.getDayPlan().getId());
        }

        if (meal.getMealFoods() != null) {

            List<Long> ids = meal.getMealFoods()
                    .stream()
                    .map(MealFood::getId)
                    .collect(Collectors.toList());

            dto.setMealFoodIds(ids);
        }

        return dto;
    }
}
