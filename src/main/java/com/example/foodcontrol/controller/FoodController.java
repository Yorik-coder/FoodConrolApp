package com.example.foodcontrol.controller;

import java.util.List;

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
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping
    public FoodDto createFood(@RequestBody FoodDto dto) {
        return foodService.createFood(dto);
    }

    @GetMapping("/{id}")
    public FoodDto getFoodById(@PathVariable Long id) {
        return foodService.getFoodById(id);
    }

    @GetMapping
    public List<FoodDto> getFoods(
            @RequestParam(required = false) Integer minCalories
    ) {

        if (minCalories != null) {
            return foodService.getFoodsByMinCalories(minCalories);
        }

        return foodService.getAllFoods();
    }

    @PutMapping("/{id}")
    public FoodDto updateFood(
            @PathVariable Long id,
            @RequestBody FoodDto dto
    ) {
        return foodService.updateFood(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
    }
}
