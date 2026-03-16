package com.example.foodcontrol.mapper;

import com.example.foodcontrol.dto.MealDto;
import com.example.foodcontrol.entity.Meal;
import com.example.foodcontrol.entity.MealFood;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Collections;
@Mapper(componentModel = "spring")
public interface MealMapper {

    @Mapping(target = "dayPlanId", source = "dayPlan.id")
    @Mapping(target = "mealFoodIds", source = "mealFoods")
    MealDto toDto(Meal meal);

    default List<Long> mapMealFoodsToIds(List<MealFood> mealFoods) {

        if (mealFoods == null) {
            return Collections.emptyList();
        }

        return mealFoods.stream()
                .map(MealFood::getId)
                .toList();
    }
}