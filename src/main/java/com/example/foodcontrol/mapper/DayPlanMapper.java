package com.example.foodcontrol.mapper;

import com.example.foodcontrol.dto.DayPlanDto;
import com.example.foodcontrol.entity.DayPlan;
import com.example.foodcontrol.entity.Meal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DayPlanMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "mealIds", source = "meals")
    DayPlanDto toDto(DayPlan plan);

    default List<Long> mapMealsToIds(List<Meal> meals) {

        if (meals == null) {
            return Collections.emptyList();
        }

        return meals.stream()
                .map(Meal::getId)
                .toList();
    }
}