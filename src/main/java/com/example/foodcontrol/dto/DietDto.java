package com.example.foodcontrol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Diet payload")
public class DietDto {
    @NotBlank(message = "name is required")
    @Size(max = 120, message = "name length must be <= 120")
    @Schema(description = "Diet name", example = "Keto")
    private String name;

    @Size(max = 500, message = "description length must be <= 500")
    @Schema(description = "Diet description", example = "Low-carb high-fat diet")
    private String description;

    @Schema(description = "Food ids included in diet")
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
