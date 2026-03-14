package com.example.foodcontrol.dto;

import java.util.List;

public class DietDto {

    private Long id;

    private String name;

    private String description;

    private List<Long> foodIds;

    public DietDto() {
    }

    public DietDto(Long id, String name, String description, List<Long> foodIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.foodIds = foodIds;
    }

    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
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
