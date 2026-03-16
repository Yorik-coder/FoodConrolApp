package com.example.foodcontrol.service;

import com.example.foodcontrol.dto.DietDto;
import com.example.foodcontrol.entity.Diet;
import com.example.foodcontrol.entity.Food;
import com.example.foodcontrol.mapper.DietMapper;
import com.example.foodcontrol.repository.DietRepository;
import com.example.foodcontrol.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DietService {

    private final DietRepository dietRepository;
    private final FoodRepository foodRepository;
    private final DietMapper dietMapper;

    public DietService(DietRepository dietRepository,
                       FoodRepository foodRepository,
                       DietMapper dietMapper) {

        this.dietRepository = dietRepository;
        this.foodRepository = foodRepository;
        this.dietMapper = dietMapper;
    }

    public DietDto createDiet(DietDto dto) {

        List<Food> foods = foodRepository.findAllById(dto.getFoodIds());

        Diet diet = dietMapper.toEntity(dto, foods);

        Diet saved = dietRepository.save(diet);

        return dietMapper.toDto(saved);
    }

    public List<DietDto> getAllDiets() {

        return dietRepository.findAll()
                .stream()
                .map(dietMapper::toDto)
                .toList();
    }

    public DietDto getDietById(Long id) {

        return dietRepository.findById(id)
                .map(dietMapper::toDto)
                .orElse(null);
    }

    public void deleteDiet(Long id) {
        dietRepository.deleteById(id);
    }
}
