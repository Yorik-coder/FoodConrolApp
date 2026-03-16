package com.example.foodcontrol.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodcontrol.dto.DayPlanDto;
import com.example.foodcontrol.dto.MealDto;
import com.example.foodcontrol.service.MealPlanService;

@RestController
@RequestMapping("/test")
public class TestTransactionController {

    private final MealPlanService mealPlanService;

    public TestTransactionController(MealPlanService mealPlanService) {
        this.mealPlanService = mealPlanService;
    }

    @PostMapping("/create-plan-without-tx")
    public String createWithoutTransaction(@RequestBody DayPlanWithMealsRequest request) {
        try {
            mealPlanService.createDayPlanWithMealsWithoutTransaction(request.getDayPlan(), request.getMeals());
            return "Success (should not happen)";
        } catch (Exception e) {
            return "Error: " + e.getMessage() + " — check database for partial save";
        }
    }

    @PostMapping("/create-plan-with-tx")
    public String createWithTransaction(@RequestBody DayPlanWithMealsRequest request) {
        try {
            mealPlanService.createDayPlanWithMealsWithTransaction(request.getDayPlan(), request.getMeals());
            return "Success";
        } catch (Exception e) {
            return "Transaction rolled back: " + e.getMessage();
        }
    }

    // Вспомогательный класс для приёма данных
    public static class DayPlanWithMealsRequest {
        private DayPlanDto dayPlan;
        private List<MealDto> meals;

        public DayPlanDto getDayPlan() { return dayPlan; }
        public void setDayPlan(DayPlanDto dayPlan) { this.dayPlan = dayPlan; }
        public List<MealDto> getMeals() { return meals; }
        public void setMeals(List<MealDto> meals) { this.meals = meals; }
    }
}