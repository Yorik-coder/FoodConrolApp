package com.example.foodcontrol.service;

import com.example.foodcontrol.dto.MealFoodDto;
import com.example.foodcontrol.entity.Food;
import com.example.foodcontrol.entity.Meal;
import com.example.foodcontrol.entity.MealFood;
import com.example.foodcontrol.mapper.MealFoodMapper;
import com.example.foodcontrol.repository.FoodRepository;
import com.example.foodcontrol.repository.MealFoodRepository;
import com.example.foodcontrol.repository.MealRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MealFoodServiceTest {

    @Mock
    private MealFoodRepository mealFoodRepository;

    @Mock
    private MealRepository mealRepository;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private MealFoodMapper mealFoodMapper;

    @Mock
    private DayPlanService dayPlanService;

    @InjectMocks
    private MealFoodService mealFoodService;

    @Test
    void addFoodToMealSavesAndInvalidatesCache() {
        MealFoodDto dto = new MealFoodDto(1L, 2L, 100);
        Meal meal = new Meal();
        Food food = new Food();
        MealFood saved = new MealFood();

        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal));
        when(foodRepository.findById(2L)).thenReturn(Optional.of(food));
        when(mealFoodRepository.save(any(MealFood.class))).thenReturn(saved);
        when(mealFoodMapper.toDto(saved)).thenReturn(dto);

        MealFoodDto actual = mealFoodService.addFoodToMeal(dto);

        assertEquals(dto, actual);
        verify(dayPlanService).invalidateSearchCache();
    }

    @Test
    void updateGramsThrowsWhenMissing() {
        when(mealFoodRepository.findById(3L)).thenReturn(Optional.empty());
        MealFoodDto updateDto = new MealFoodDto(1L, 2L, 80);

        assertThrows(RuntimeException.class,
            () -> mealFoodService.updateGrams(3L, updateDto));
    }

    @Test
    void updateGramsSavesAndInvalidatesCache() {
        MealFood mealFood = new MealFood();
        MealFoodDto dto = new MealFoodDto(1L, 2L, 150);

        when(mealFoodRepository.findById(3L)).thenReturn(Optional.of(mealFood));
        when(mealFoodRepository.save(mealFood)).thenReturn(mealFood);
        when(mealFoodMapper.toDto(mealFood)).thenReturn(dto);

        MealFoodDto actual = mealFoodService.updateGrams(3L, dto);

        assertEquals(150, mealFood.getGrams());
        assertEquals(dto, actual);
        verify(dayPlanService).invalidateSearchCache();
    }

    @Test
    void deleteMealFoodThrowsNotFound() {
        when(mealFoodRepository.findById(3L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> mealFoodService.deleteMealFood(3L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void deleteMealFoodDeletesAndInvalidatesCache() {
        MealFood mealFood = new MealFood();
        when(mealFoodRepository.findById(3L)).thenReturn(Optional.of(mealFood));

        mealFoodService.deleteMealFood(3L);

        verify(mealFoodRepository).delete(mealFood);
        verify(dayPlanService).invalidateSearchCache();
    }
}
