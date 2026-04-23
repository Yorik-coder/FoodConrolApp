package com.example.foodcontrol.service;

import com.example.foodcontrol.dto.DietDto;
import com.example.foodcontrol.entity.Diet;
import com.example.foodcontrol.entity.Food;
import com.example.foodcontrol.mapper.DietMapper;
import com.example.foodcontrol.repository.DietRepository;
import com.example.foodcontrol.repository.FoodRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DietServiceTest {

    @Mock
    private DietRepository dietRepository;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private DietMapper dietMapper;

    @InjectMocks
    private DietService dietService;

    @Test
    void createDietLoadsFoodsAndSaves() {
        DietDto dto = new DietDto("Keto", "desc", List.of(1L));
        Food food = new Food();
        Diet entity = new Diet();
        Diet saved = new Diet();
        DietDto mapped = new DietDto("Keto", "desc", List.of(1L));

        when(foodRepository.findAllById(List.of(1L))).thenReturn(List.of(food));
        when(dietMapper.toEntity(dto, List.of(food))).thenReturn(entity);
        when(dietRepository.save(entity)).thenReturn(saved);
        when(dietMapper.toDto(saved)).thenReturn(mapped);

        DietDto actual = dietService.createDiet(dto);

        assertEquals(mapped, actual);
    }

    @Test
    void getAllDietsMapsAll() {
        Diet first = new Diet();
        DietDto dto = new DietDto("Keto", "desc", List.of(1L));

        when(dietRepository.findAll()).thenReturn(List.of(first));
        when(dietMapper.toDto(first)).thenReturn(dto);

        assertEquals(List.of(dto), dietService.getAllDiets());
    }

    @Test
    void getDietByIdReturnsNullWhenNotFound() {
        when(dietRepository.findById(1L)).thenReturn(Optional.empty());

        assertNull(dietService.getDietById(1L));
    }

    @Test
    void getDietByIdReturnsDtoWhenFound() {
        Diet diet = new Diet();
        DietDto dto = new DietDto("Keto", "desc", List.of(1L));

        when(dietRepository.findById(1L)).thenReturn(Optional.of(diet));
        when(dietMapper.toDto(diet)).thenReturn(dto);

        assertEquals(dto, dietService.getDietById(1L));
    }

    @Test
    void deleteDietDelegates() {
        dietService.deleteDiet(11L);

        verify(dietRepository).deleteById(11L);
    }
}
