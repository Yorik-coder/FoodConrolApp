package com.example.foodcontrol.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodcontrol.dto.DayPlanDto;
import com.example.foodcontrol.dto.MealDto;
import com.example.foodcontrol.service.MealPlanService;

@RestController
@RequestMapping("/test")
@Tag(name = "Transactions", description = "Transaction behavior test endpoints")
public class TestTransactionController {

    private final MealPlanService mealPlanService;

    public TestTransactionController(MealPlanService mealPlanService) {
        this.mealPlanService = mealPlanService;
    }

    @PostMapping("/create-plan-without-tx")
    @Operation(summary = "Create plan without transaction")
    public String createWithoutTransaction(@Valid @RequestBody DayPlanWithMealsRequest request) {
        try {
            mealPlanService.createDayPlanWithMealsWithoutTransaction(request.getDayPlan(), request.getMeals());
            return "Success (should not happen)";
        } catch (Exception e) {
            return "Error: " + e.getMessage() + " — check database for partial save";
        }
    }

    @PostMapping("/create-plan-with-tx")
    @Operation(summary = "Create plan with transaction")
    public ResponseEntity<String> createWithTransaction(@Valid @RequestBody DayPlanWithMealsRequest request) {
        try {
            mealPlanService.createDayPlanWithMealsWithTransaction(request.getDayPlan(), request.getMeals());
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Transaction rolled back: " + e.getMessage());
        }
    }

    @Schema(description = "Payload for transaction test endpoints")
    public static class DayPlanWithMealsRequest {
        @NotNull(message = "dayPlan is required")
        @Valid
        private DayPlanDto dayPlan;

        @NotNull(message = "meals is required")
        @Valid
        private List<MealDto> meals;

        public DayPlanDto getDayPlan() { return dayPlan; }
        public void setDayPlan(DayPlanDto dayPlan) { this.dayPlan = dayPlan; }
        public List<MealDto> getMeals() { return meals; }
        public void setMeals(List<MealDto> meals) { this.meals = meals; }
    }
}