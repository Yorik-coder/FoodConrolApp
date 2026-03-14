package com.example.foodcontrol.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodcontrol.dto.DayPlanDto;
import com.example.foodcontrol.service.DayPlanService;

@RestController
@RequestMapping("/dayplans")
public class DayPlanController {

    private final DayPlanService dayPlanService;

    public DayPlanController(DayPlanService dayPlanService) {
        this.dayPlanService = dayPlanService;
    }

    @PostMapping
    public DayPlanDto createPlan(@RequestBody DayPlanDto dto) {
        return dayPlanService.createDayPlan(dto);
    }

    @GetMapping
    public List<DayPlanDto> getPlans() {
        return dayPlanService.getAllPlans();
    }

    @GetMapping("/{id}")
    public DayPlanDto getPlan(@PathVariable Long id) {
        return dayPlanService.getPlan(id);
    }

    @DeleteMapping("/{id}")
    public void deletePlan(@PathVariable Long id) {
        dayPlanService.deletePlan(id);
    }
}
