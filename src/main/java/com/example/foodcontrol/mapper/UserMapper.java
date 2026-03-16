package com.example.foodcontrol.mapper;

import com.example.foodcontrol.dto.UserDto;
import com.example.foodcontrol.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dayPlans", ignore = true)
    User toEntity(UserDto dto);
}