package com.example.foodcontrol.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodcontrol.dto.DietDto;
import com.example.foodcontrol.service.DietService;

@RestController
@RequestMapping("/diets")
@Validated
@Tag(name = "Diets", description = "Operations for diets")
public class DietController {

    private final DietService dietService;

    public DietController(DietService dietService) {
        this.dietService = dietService;
    }

    @PostMapping
    @Operation(summary = "Create diet")
    public DietDto createDiet(@Valid @RequestBody DietDto dto) {
        return dietService.createDiet(dto);
    }

    @GetMapping
    @Operation(summary = "Get all diets")
    public List<DietDto> getAllDiets() {
        return dietService.getAllDiets();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get diet by id")
    public DietDto getDiet(@PathVariable @Positive Long id) {
        return dietService.getDietById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete diet")
    public void deleteDiet(@PathVariable @Positive Long id) {
        dietService.deleteDiet(id);
    }
}
