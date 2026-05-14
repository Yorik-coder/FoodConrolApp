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

        public DietDto updateDiet(Long id, DietDto dto) {

        Diet existing = dietRepository.findById(id)
            .orElseThrow(() -> new java.util.NoSuchElementException("Diet not found with id: " + id));

        List<Long> foodIds = java.util.Optional.ofNullable(dto.getFoodIds())
            .orElseGet(List::of);
        List<Food> foods = foodRepository.findAllById(foodIds);

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setFoods(foods);

        Diet saved = dietRepository.save(existing);

        return dietMapper.toDto(saved);
        }

    public void deleteDiet(Long id) {
        dietRepository.deleteById(id);
    }
}
