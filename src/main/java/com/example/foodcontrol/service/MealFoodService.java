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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
@Service
public class MealFoodService {

    private final MealFoodRepository mealFoodRepository;
    private final MealRepository mealRepository;
    private final FoodRepository foodRepository;
    private final MealFoodMapper mealFoodMapper;

    public MealFoodService(MealFoodRepository mealFoodRepository,
                           MealRepository mealRepository,
                           FoodRepository foodRepository,
                           MealFoodMapper mealFoodMapper) {

        this.mealFoodRepository = mealFoodRepository;
        this.mealRepository = mealRepository;
        this.foodRepository = foodRepository;
        this.mealFoodMapper = mealFoodMapper;
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

        return mealFoodMapper.toDto(saved);
    }

    public MealFoodDto updateGrams(Long id, MealFoodDto dto) {
        MealFood mealFood = mealFoodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MealFood not found with id: " + id));

    
        mealFood.setGrams(dto.getGrams());

        MealFood updated = mealFoodRepository.save(mealFood);
        return mealFoodMapper.toDto(updated);
    }

    public void deleteMealFood(Long id) {
        MealFood mealFood = mealFoodRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MealFood not found with id: " + id));
        mealFoodRepository.delete(mealFood);
    }
}
