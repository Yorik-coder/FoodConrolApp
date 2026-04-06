package com.example.foodcontrol.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodcontrol.dto.MealDto;
import com.example.foodcontrol.service.MealService;

@RestController
@RequestMapping("/meals")
@Validated
@Tag(name = "Meals", description = "Operations for meals")
public class MealController {

    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping
    @Operation(summary = "Create meal")
    public MealDto createMeal(@Valid @RequestBody MealDto dto) {
        return mealService.createMeal(dto);
    }

    @GetMapping
    @Operation(summary = "Get all meals")
    public List<MealDto> getMeals() {
        return mealService.getMeals();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get meal by id")
    public MealDto getMeal(@PathVariable @Positive Long id) {
        return mealService.getMeal(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete meal")
    public void deleteMeal(@PathVariable @Positive Long id) {
        mealService.deleteMeal(id);
    }
}
