package com.example.foodcontrol.service;

import com.example.foodcontrol.dto.MealDto;
import com.example.foodcontrol.entity.DayPlan;
import com.example.foodcontrol.entity.Meal;
import com.example.foodcontrol.entity.MealType;
import com.example.foodcontrol.mapper.MealMapper;
import com.example.foodcontrol.repository.DayPlanRepository;
import com.example.foodcontrol.repository.MealRepository;
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
class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private DayPlanRepository dayPlanRepository;

    @Mock
    private MealMapper mealMapper;

    @Mock
    private DayPlanService dayPlanService;

    @InjectMocks
    private MealService mealService;

    @Test
    void createMealSavesAndInvalidatesCache() {
        MealDto dto = new MealDto(MealType.BREAKFAST, 1L, List.of());
        DayPlan dayPlan = new DayPlan();
        Meal saved = new Meal();
        MealDto mapped = new MealDto(MealType.BREAKFAST, 1L, List.of());

        when(dayPlanRepository.findById(1L)).thenReturn(Optional.of(dayPlan));
        when(mealRepository.save(org.mockito.ArgumentMatchers.any(Meal.class))).thenReturn(saved);
        when(mealMapper.toDto(saved)).thenReturn(mapped);

        MealDto actual = mealService.createMeal(dto);

        assertEquals(mapped, actual);
        verify(dayPlanService).invalidateSearchCache();
    }

    @Test
    void getMealsMapsAll() {
        Meal meal = new Meal();
        MealDto dto = new MealDto(MealType.DINNER, 1L, List.of(1L));

        when(mealRepository.findAll()).thenReturn(List.of(meal));
        when(mealMapper.toDto(meal)).thenReturn(dto);

        assertEquals(List.of(dto), mealService.getMeals());
    }

    @Test
    void getMealReturnsNullWhenNotFound() {
        when(mealRepository.findById(9L)).thenReturn(Optional.empty());

        assertNull(mealService.getMeal(9L));
    }

    @Test
    void getMealReturnsDtoWhenFound() {
        Meal meal = new Meal();
        MealDto dto = new MealDto(MealType.LUNCH, 1L, List.of(1L));

        when(mealRepository.findById(9L)).thenReturn(Optional.of(meal));
        when(mealMapper.toDto(meal)).thenReturn(dto);

        assertEquals(dto, mealService.getMeal(9L));
    }

    @Test
    void deleteMealDelegatesAndInvalidatesCache() {
        mealService.deleteMeal(5L);

        verify(mealRepository).deleteById(5L);
        verify(dayPlanService).invalidateSearchCache();
    }
}
