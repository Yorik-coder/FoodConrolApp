package com.example.foodcontrol.mapper;

import com.example.foodcontrol.dto.MealFoodDto;
import com.example.foodcontrol.entity.MealFood;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MealFoodMapper {

    @Mapping(target = "mealId", source = "meal.id")
    @Mapping(target = "foodId", source = "food.id")
    MealFoodDto toDto(MealFood entity);
}