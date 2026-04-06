package com.example.foodcontrol.mapper;

import com.example.foodcontrol.dto.DietDto;
import com.example.foodcontrol.entity.Diet;
import com.example.foodcontrol.entity.Food;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DietMapper {

    @Mapping(target = "foodIds", source = "foods")
    DietDto toDto(Diet diet);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "foods", source = "foods")
    Diet toEntity(DietDto dto, List<Food> foods);
    
    default List<Long> mapFoodsToIds(List<Food> foods) {
        if (foods == null) {
            return Collections.emptyList();
        }

        return foods.stream()
                .map(Food::getId)
                .toList();
    }
}