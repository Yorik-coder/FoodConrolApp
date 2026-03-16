package com.example.foodcontrol.service;

import com.example.foodcontrol.dto.MealDto;
import com.example.foodcontrol.entity.DayPlan;
import com.example.foodcontrol.entity.Meal;
import com.example.foodcontrol.mapper.MealMapper;
import com.example.foodcontrol.repository.DayPlanRepository;
import com.example.foodcontrol.repository.MealRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealService {

    private final MealRepository mealRepository;
    private final DayPlanRepository dayPlanRepository;
    private final MealMapper mealMapper;

    public MealService(MealRepository mealRepository,
                       DayPlanRepository dayPlanRepository,
                       MealMapper mealMapper) {

        this.mealRepository = mealRepository;
        this.dayPlanRepository = dayPlanRepository;
        this.mealMapper = mealMapper;
    }

    public MealDto createMeal(MealDto dto) {

        DayPlan plan = dayPlanRepository.findById(dto.getDayPlanId())
                .orElseThrow();

        Meal meal = new Meal();
        meal.setName(dto.getName());
        meal.setDayPlan(plan);

        Meal saved = mealRepository.save(meal);

        return mealMapper.toDto(saved);
    }

    public List<MealDto> getMeals() {

        return mealRepository.findAll()
                .stream()
                .map(mealMapper::toDto)
                .toList();
    }

    public MealDto getMeal(Long id) {

        return mealRepository.findById(id)
                .map(mealMapper::toDto)
                .orElse(null);
    }

    public void deleteMeal(Long id) {
        mealRepository.deleteById(id);
    }
}
