package com.example.foodcontrol.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodcontrol.dto.MealFoodDto;
import com.example.foodcontrol.service.MealFoodService;

@RestController
@RequestMapping("/meal-foods")
@Validated
@Tag(name = "Meal Foods", description = "Operations for meal-food links")
public class MealFoodController {

    private final MealFoodService mealFoodService;

    public MealFoodController(MealFoodService mealFoodService) {
        this.mealFoodService = mealFoodService;
    }

    @PostMapping
    @Operation(summary = "Add food to meal")
    public MealFoodDto addFoodToMeal(@Valid @RequestBody MealFoodDto dto) {
        return mealFoodService.addFoodToMeal(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update meal-food grams")
    public MealFoodDto updateGrams(@PathVariable @Positive Long id, @Valid @RequestBody MealFoodDto dto) {
        return mealFoodService.updateGrams(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove food from meal")
    public void removeFoodFromMeal(@PathVariable @Positive Long id) {
        mealFoodService.deleteMealFood(id);
    }
}
