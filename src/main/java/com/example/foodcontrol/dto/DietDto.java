package com.example.foodcontrol.dto;

import java.util.List;

public class DietDto {
    private String name;

    private String description;

    private List<Long> foodIds;

    public DietDto() {
    }

    public DietDto(String name, String description, List<Long> foodIds) {
        this.name = name;
        this.description = description;
        this.foodIds = foodIds;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Long> getFoodIds() {
        return foodIds;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFoodIds(List<Long> foodIds) {
        this.foodIds = foodIds;
    }
}
