package com.example.foodcontrol.mapper;

import com.example.foodcontrol.dto.FoodDto;
import com.example.foodcontrol.entity.Food;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FoodMapper {

    FoodDto toDto(Food food);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "mealFoods", ignore = true)
    @Mapping(target = "diets", ignore = true)
    Food toEntity(FoodDto dto);

}