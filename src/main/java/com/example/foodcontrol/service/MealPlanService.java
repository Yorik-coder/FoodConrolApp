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
        DayPlan savedDayPlan = dayPlanRepository.save(dayPlan); // фиксируется сразу

        int count = 0;
        for (MealDto mealDto : mealDtos) {
            Meal meal = new Meal();
            meal.setName(mealDto.getName());
            meal.setDayPlan(savedDayPlan);
            mealRepository.save(meal); // каждый Meal фиксируется сразу
            count++;

            // Искусственная ошибка после сохранения второго Meal было до этого !mealDtos.isEmpty
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
            meal.setName(mealDto.getName());
            dayPlan.addMeal(meal); // устанавливает двунаправленную связь
        }

        dayPlanRepository.save(dayPlan); // сохраняется DayPlan + все Meal (каскадно)

        if (!mealDtos.isEmpty()) {
            throw new IllegalStateException("Simulated error after saving some meals");
        }
    }
}