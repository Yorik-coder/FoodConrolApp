package com.example.foodcontrol.controller;

import java.util.List;

import com.example.foodcontrol.dto.DayPlanSearchFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodcontrol.dto.DayPlanDto;
import com.example.foodcontrol.service.DayPlanService;

@RestController
@RequestMapping("/dayplans")
@Validated
@Tag(name = "Day Plans", description = "Operations for day plans")
public class DayPlanController {

    private final DayPlanService dayPlanService;

    public DayPlanController(DayPlanService dayPlanService) {
        this.dayPlanService = dayPlanService;
    }

    @PostMapping
    @Operation(summary = "Create day plan")
    public DayPlanDto createPlan(@Valid @RequestBody DayPlanDto dto) {
        return dayPlanService.createDayPlan(dto);
    }

    @GetMapping
    @Operation(summary = "Get all day plans")
    public List<DayPlanDto> getPlans() {
        return dayPlanService.getAllPlans();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get day plan by id")
    public DayPlanDto getPlan(@PathVariable @Positive Long id) {
        return dayPlanService.getPlan(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search day plans with filters and pagination")
    public Page<DayPlanDto> searchPlans(
            @Valid @ModelAttribute @ParameterObject DayPlanSearchFilter filter,
            @ParameterObject Pageable pageable
    ) {
        return dayPlanService.searchPlans(filter, pageable);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete day plan by id")
    public void deletePlan(@PathVariable @Positive Long id) {
        dayPlanService.deletePlan(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update day plan by id")
    public DayPlanDto updatePlan(@PathVariable @Positive Long id,
                                 @Valid @RequestBody DayPlanDto dto) {
        return dayPlanService.updatePlan(id, dto);
    }
}
