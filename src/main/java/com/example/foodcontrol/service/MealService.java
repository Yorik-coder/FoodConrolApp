package com.example.foodcontrol.service;

import com.example.foodcontrol.dto.MealDto;
import com.example.foodcontrol.entity.DayPlan;
import com.example.foodcontrol.entity.Meal;
import com.example.foodcontrol.mapper.MealMapper;
import com.example.foodcontrol.repository.DayPlanRepository;
import com.example.foodcontrol.repository.MealRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealService {

    private final MealRepository mealRepository;
    private final DayPlanRepository dayPlanRepository;

    public MealService(MealRepository mealRepository,
                       DayPlanRepository dayPlanRepository) {
        this.mealRepository = mealRepository;
        this.dayPlanRepository = dayPlanRepository;
    }

    public MealDto createMeal(MealDto dto) {

        DayPlan plan = dayPlanRepository.findById(dto.getDayPlanId())
                .orElseThrow();

        Meal meal = new Meal();
        meal.setName(dto.getName());
        meal.setDayPlan(plan);

        Meal saved = mealRepository.save(meal);

        return MealMapper.toDto(saved);
    }

    public List<MealDto> getMeals() {

        return mealRepository.findAll()
                .stream()
                .map(MealMapper::toDto)
                .collect(Collectors.toList());
    }

    public MealDto getMeal(Long id) {

        return mealRepository.findById(id)
                .map(MealMapper::toDto)
                .orElse(null);
    }

    public void deleteMeal(Long id) {
        mealRepository.deleteById(id);
    }
}
