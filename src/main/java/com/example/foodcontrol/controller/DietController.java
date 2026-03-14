package com.example.foodcontrol.controller;

import java.util.List;

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
public class DietController {

    private final DietService dietService;

    public DietController(DietService dietService) {
        this.dietService = dietService;
    }

    @PostMapping
    public DietDto createDiet(@RequestBody DietDto dto) {
        return dietService.createDiet(dto);
    }

    @GetMapping
    public List<DietDto> getAllDiets() {
        return dietService.getAllDiets();
    }

    @GetMapping("/{id}")
    public DietDto getDiet(@PathVariable Long id) {
        return dietService.getDietById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteDiet(@PathVariable Long id) {
        dietService.deleteDiet(id);
    }
}
