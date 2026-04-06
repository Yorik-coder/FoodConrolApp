package com.example.foodcontrol.service;

import com.example.foodcontrol.dto.DayPlanDto;
import com.example.foodcontrol.dto.MealDto;
import com.example.foodcontrol.entity.DayPlan;
import com.example.foodcontrol.entity.Meal;
import com.example.foodcontrol.entity.User;
import com.example.foodcontrol.repository.DayPlanRepository;
import com.example.foodcontrol.repository.MealRepository;
import com.example.foodcontrol.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MealPlanService {

    private final UserRepository userRepository;
    private final DayPlanRepository dayPlanRepository;
    private final MealRepository mealRepository;

    public MealPlanService(UserRepository userRepository,
                           DayPlanRepository dayPlanRepository,
                           MealRepository mealRepository) {
        this.userRepository = userRepository;
        this.dayPlanRepository = dayPlanRepository;
        this.mealRepository = mealRepository;
    }

    public void createDayPlanWithMealsWithoutTransaction(DayPlanDto dayPlanDto, List<MealDto> mealDtos) {
        User user = userRepository.findById(dayPlanDto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + dayPlanDto.getUserId()));

        DayPlan dayPlan = new DayPlan();
        dayPlan.setDate(dayPlanDto.getDate());
        dayPlan.setUser(user);
        DayPlan savedDayPlan = dayPlanRepository.save(dayPlan); 

        int count = 0;
        for (MealDto mealDto : mealDtos) {
            Meal meal = new Meal();
            meal.setMealType(mealDto.getMealType());
            meal.setDayPlan(savedDayPlan);
            mealRepository.save(meal); 
            count++;

            if (count == 2) {
                throw new IllegalStateException("Simulated error after saving 2 meals");
            }
        }
    }

    @Transactional
    public void createDayPlanWithMealsWithTransaction(DayPlanDto dayPlanDto, List<MealDto> mealDtos) {
        User user = userRepository.findById(dayPlanDto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + dayPlanDto.getUserId()));

        DayPlan dayPlan = new DayPlan();
        dayPlan.setDate(dayPlanDto.getDate());
        dayPlan.setUser(user);

        for (MealDto mealDto : mealDtos) {
            Meal meal = new Meal();
            meal.setMealType(mealDto.getMealType());
            dayPlan.addMeal(meal); 
        }

        dayPlanRepository.save(dayPlan); 

        if (!mealDtos.isEmpty()) {
            throw new IllegalStateException("Simulated error");
        }
    }
}