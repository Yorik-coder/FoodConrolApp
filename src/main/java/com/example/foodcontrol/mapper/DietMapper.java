package com.example.foodcontrol.mapper;


import com.example.foodcontrol.dto.DietDto;
import com.example.foodcontrol.entity.Diet;
import com.example.foodcontrol.entity.Food;

import java.util.List;
import java.util.stream.Collectors;

public class DietMapper {

    public static DietDto toDto(Diet diet) {
        if (diet == null) {
            return null;
        }

        DietDto dto = new DietDto();
        dto.setId(diet.getId());
        dto.setName(diet.getName());
        dto.setDescription(diet.getDescription());

        if (diet.getFoods() != null) {
            List<Long> foodIds = diet.getFoods()
                    .stream()
                    .map(Food::getId)
                    .collect(Collectors.toList());

            dto.setFoodIds(foodIds);
        }

        return dto;
    }

    public static Diet toEntity(DietDto dto, List<Food> foods) {
        if (dto == null) {
            return null;
        }

        Diet diet = new Diet();
        diet.setId(dto.getId());
        diet.setName(dto.getName());
        diet.setDescription(dto.getDescription());
        diet.setFoods(foods);

        return diet;
    }
}
