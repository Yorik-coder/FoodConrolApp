package com.example.foodcontrol.mapper;

import com.example.foodcontrol.dto.FoodDto;
import com.example.foodcontrol.entity.Food;

public class FoodMapper {

    public static FoodDto toDto(Food food) {
        if (food == null) {
            return null;
        }

        FoodDto dto = new FoodDto();
        dto.setId(food.getId());
        dto.setName(food.getName());
        dto.setCalories(food.getCalories());
        dto.setProtein(food.getProtein());
        dto.setFat(food.getFat());
        dto.setCarbs(food.getCarbs());

        return dto;
    }

    public static Food toEntity(FoodDto dto) {
        if (dto == null) {
            return null;
        }

        Food food = new Food();
        food.setId(dto.getId());
        food.setName(dto.getName());
        food.setCalories(dto.getCalories());
        food.setProtein(dto.getProtein());
        food.setFat(dto.getFat());
        food.setCarbs(dto.getCarbs());

        return food;
    }
}
