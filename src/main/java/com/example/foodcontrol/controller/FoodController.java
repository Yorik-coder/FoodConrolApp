package com.example.foodcontrol.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodcontrol.dto.FoodDto;
import com.example.foodcontrol.service.FoodService;

@RestController
@RequestMapping("/foods")
@Validated
@Tag(name = "Foods", description = "Operations for foods")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping
    @Operation(summary = "Create food")
    public FoodDto createFood(@Valid @RequestBody FoodDto dto) {
        return foodService.createFood(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get food by id")
    public FoodDto getFoodById(@PathVariable @Positive Long id) {
        return foodService.getFoodById(id);
    }

    @GetMapping
    @Operation(summary = "Get foods with optional min-calories filter")
    public List<FoodDto> getFoods(
            @RequestParam(required = false) @PositiveOrZero Integer minCalories
    ) {

        if (minCalories != null) {
            return foodService.getFoodsByMinCalories(minCalories);
        }

        return foodService.getAllFoods();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update food")
    public FoodDto updateFood(
            @PathVariable @Positive Long id,
            @Valid @RequestBody FoodDto dto
    ) {
        return foodService.updateFood(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete food")
    public void deleteFood(@PathVariable @Positive Long id) {
        foodService.deleteFood(id);
    }
}
