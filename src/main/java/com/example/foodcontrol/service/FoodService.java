package com.example.foodcontrol.service;

import com.example.foodcontrol.dto.FoodDto;
import com.example.foodcontrol.entity.Food;
import com.example.foodcontrol.mapper.FoodMapper;
import com.example.foodcontrol.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private final FoodMapper foodMapper;
    private final DayPlanService dayPlanService;

    public FoodService(FoodRepository foodRepository,
                       FoodMapper foodMapper,
                       DayPlanService dayPlanService) {
        this.foodRepository = foodRepository;
        this.foodMapper = foodMapper;
        this.dayPlanService = dayPlanService;
    }

    public FoodDto createFood(FoodDto dto) {
        Food food = foodMapper.toEntity(dto);
        Food saved = foodRepository.save(food);
        dayPlanService.invalidateSearchCache();
        return foodMapper.toDto(saved);
    }

    public FoodDto getFoodById(Long id) {
        Optional<Food> food = foodRepository.findById(id);
        return food.map(foodMapper::toDto).orElse(null);
    }

    public List<FoodDto> getAllFoods() {
        return foodRepository.findAll()
                .stream()
                .map(foodMapper::toDto)
                .toList();
    }

    public List<FoodDto> getFoodsByMinCalories(int minCalories) {
        return foodRepository.findByCaloriesGreaterThan(minCalories)
                .stream()
                .map(foodMapper::toDto)
                .toList();
    }

    public FoodDto updateFood(Long id, FoodDto dto) {

        Optional<Food> existing = foodRepository.findById(id);

        if (existing.isEmpty()) {
            return null;
        }

        Food food = existing.get();

        food.setName(dto.getName());
        food.setCalories(dto.getCalories());
        food.setProtein(dto.getProtein());
        food.setFat(dto.getFat());
        food.setCarbs(dto.getCarbs());

        Food saved = foodRepository.save(food);
        dayPlanService.invalidateSearchCache();

        return foodMapper.toDto(saved);
    }

    public void deleteFood(Long id) {
        foodRepository.deleteById(id);
        dayPlanService.invalidateSearchCache();
    }
}
