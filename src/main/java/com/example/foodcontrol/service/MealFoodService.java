package com.example.foodcontrol.service;

import com.example.foodcontrol.dto.MealFoodDto;
import com.example.foodcontrol.entity.Food;
import com.example.foodcontrol.entity.Meal;
import com.example.foodcontrol.entity.MealFood;
import com.example.foodcontrol.mapper.MealFoodMapper;
import com.example.foodcontrol.repository.FoodRepository;
import com.example.foodcontrol.repository.MealFoodRepository;
import com.example.foodcontrol.repository.MealRepository;
import org.springframework.stereotype.Service;

@Service
public class MealFoodService {

    private final MealFoodRepository mealFoodRepository;
    private final MealRepository mealRepository;
    private final FoodRepository foodRepository;

    public MealFoodService(MealFoodRepository mealFoodRepository,
                           MealRepository mealRepository,
                           FoodRepository foodRepository) {

        this.mealFoodRepository = mealFoodRepository;
        this.mealRepository = mealRepository;
        this.foodRepository = foodRepository;
    }

    public MealFoodDto addFoodToMeal(MealFoodDto dto) {

        Meal meal = mealRepository.findById(dto.getMealId())
                .orElseThrow();

        Food food = foodRepository.findById(dto.getFoodId())
                .orElseThrow();

        MealFood mealFood = new MealFood();
        mealFood.setMeal(meal);
        mealFood.setFood(food);
        mealFood.setGrams(dto.getGrams());

        MealFood saved = mealFoodRepository.save(mealFood);

        return MealFoodMapper.toDto(saved);
    }
}
