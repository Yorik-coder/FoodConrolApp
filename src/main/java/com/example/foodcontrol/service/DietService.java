package com.example.foodcontrol.service;

import com.example.foodcontrol.dto.DietDto;
import com.example.foodcontrol.entity.Diet;
import com.example.foodcontrol.entity.Food;
import com.example.foodcontrol.mapper.DietMapper;
import com.example.foodcontrol.repository.DietRepository;
import com.example.foodcontrol.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DietService {

    private final DietRepository dietRepository;
    private final FoodRepository foodRepository;

    public DietService(DietRepository dietRepository, FoodRepository foodRepository) {
        this.dietRepository = dietRepository;
        this.foodRepository = foodRepository;
    }

    public DietDto createDiet(DietDto dto) {

        List<Food> foods = foodRepository.findAllById(dto.getFoodIds());

        Diet diet = DietMapper.toEntity(dto, foods);

        Diet saved = dietRepository.save(diet);

        return DietMapper.toDto(saved);
    }

    public List<DietDto> getAllDiets() {
        return dietRepository.findAll()
                .stream()
                .map(DietMapper::toDto)
                .collect(Collectors.toList());
    }

    public DietDto getDietById(Long id) {
        return dietRepository.findById(id)
                .map(DietMapper::toDto)
                .orElse(null);
    }

    public void deleteDiet(Long id) {
        dietRepository.deleteById(id);
    }
}
