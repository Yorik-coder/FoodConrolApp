package com.example.foodcontrol.controller;

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
public class MealFoodController {

    private final MealFoodService mealFoodService;

    public MealFoodController(MealFoodService mealFoodService) {
        this.mealFoodService = mealFoodService;
    }

    @PostMapping
    public MealFoodDto addFoodToMeal(@RequestBody MealFoodDto dto) {
        return mealFoodService.addFoodToMeal(dto);
    }

    @PutMapping("/{id}")
    public MealFoodDto updateGrams(@PathVariable Long id, @RequestBody MealFoodDto dto) {
        return mealFoodService.updateGrams(id, dto);
    }

    @DeleteMapping("/{id}")
    public void removeFoodFromMeal(@PathVariable Long id) {
        mealFoodService.deleteMealFood(id);
    }
}
