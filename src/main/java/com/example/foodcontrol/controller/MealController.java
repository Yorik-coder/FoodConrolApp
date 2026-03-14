package com.example.foodcontrol.controller;

import java.util.List;

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
public class MealController {

    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping
    public MealDto createMeal(@RequestBody MealDto dto) {
        return mealService.createMeal(dto);
    }

    @GetMapping
    public List<MealDto> getMeals() {
        return mealService.getMeals();
    }

    @GetMapping("/{id}")
    public MealDto getMeal(@PathVariable Long id) {
        return mealService.getMeal(id);
    }

    @DeleteMapping("/{id}")
    public void deleteMeal(@PathVariable Long id) {
        mealService.deleteMeal(id);
    }
}
