package com.example.foodcontrol.mapper;

import com.example.foodcontrol.dto.MealFoodDto;
import com.example.foodcontrol.entity.MealFood;

public class MealFoodMapper {

    public static MealFoodDto toDto(MealFood entity) {

        if (entity == null) {
            return null;
        }

        MealFoodDto dto = new MealFoodDto();

        dto.setId(entity.getId());
        dto.setGrams(entity.getGrams());

        if (entity.getMeal() != null) {
            dto.setMealId(entity.getMeal().getId());
        }

        if (entity.getFood() != null) {
            dto.setFoodId(entity.getFood().getId());
        }

        return dto;
    }
}
