package com.example.foodcontrol.service;

import com.example.foodcontrol.dto.DayPlanDto;
import com.example.foodcontrol.dto.MealDto;
import com.example.foodcontrol.entity.DayPlan;
import com.example.foodcontrol.entity.Meal;
import com.example.foodcontrol.entity.MealType;
import com.example.foodcontrol.entity.User;
import com.example.foodcontrol.repository.DayPlanRepository;
import com.example.foodcontrol.repository.MealRepository;
import com.example.foodcontrol.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MealPlanServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DayPlanRepository dayPlanRepository;

    @Mock
    private MealRepository mealRepository;

    @Mock
    private DayPlanService dayPlanService;

    @InjectMocks
    private MealPlanService mealPlanService;

    @Test
    void createDayPlanWithMealsWithoutTransactionThrowsWhenUserMissing() {
        DayPlanDto dayPlanDto = new DayPlanDto(LocalDate.now().plusDays(1), 7L, List.of());
        List<MealDto> emptyMeals = List.of();
        when(userRepository.findById(7L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
            () -> mealPlanService.createDayPlanWithMealsWithoutTransaction(dayPlanDto, emptyMeals));
    }

    @Test
    void createDayPlanWithMealsWithoutTransactionThrowsAfterSecondMeal() {
        DayPlanDto dayPlanDto = new DayPlanDto(LocalDate.now().plusDays(1), 1L, List.of());
        User user = new User();
        DayPlan savedDayPlan = new DayPlan();
        List<MealDto> meals = List.of(
                new MealDto(MealType.BREAKFAST, 1L, List.of()),
                new MealDto(MealType.LUNCH, 1L, List.of())
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(dayPlanRepository.save(any(DayPlan.class))).thenReturn(savedDayPlan);
        when(mealRepository.save(any(Meal.class))).thenReturn(new Meal());

        assertThrows(IllegalStateException.class,
                () -> mealPlanService.createDayPlanWithMealsWithoutTransaction(dayPlanDto, meals));

        verify(mealRepository, times(2)).save(any(Meal.class));
        verify(dayPlanService).invalidateSearchCache();
    }

    @Test
    void createDayPlanWithMealsWithoutTransactionCompletesForSingleMeal() {
        DayPlanDto dayPlanDto = new DayPlanDto(LocalDate.now().plusDays(1), 1L, List.of());
        User user = new User();
        DayPlan savedDayPlan = new DayPlan();
        List<MealDto> meals = List.of(new MealDto(MealType.BREAKFAST, 1L, List.of()));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(dayPlanRepository.save(any(DayPlan.class))).thenReturn(savedDayPlan);
        when(mealRepository.save(any(Meal.class))).thenReturn(new Meal());

        mealPlanService.createDayPlanWithMealsWithoutTransaction(dayPlanDto, meals);

        verify(mealRepository, times(1)).save(any(Meal.class));
        verify(dayPlanService).invalidateSearchCache();
    }

    @Test
    void createDayPlanWithMealsWithTransactionThrowsOnNonEmptyMeals() {
        DayPlanDto dayPlanDto = new DayPlanDto(LocalDate.now().plusDays(1), 1L, List.of());
        User user = new User();
        List<MealDto> meals = List.of(new MealDto(MealType.DINNER, 1L, List.of()));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(dayPlanRepository.save(any(DayPlan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertThrows(IllegalStateException.class,
                () -> mealPlanService.createDayPlanWithMealsWithTransaction(dayPlanDto, meals));

        verify(dayPlanService).invalidateSearchCache();
    }

    @Test
    void createDayPlanWithMealsWithTransactionThrowsWhenUserMissing() {
        DayPlanDto dayPlanDto = new DayPlanDto(LocalDate.now().plusDays(1), 99L, List.of());
        List<MealDto> emptyMeals = List.of();
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
            () -> mealPlanService.createDayPlanWithMealsWithTransaction(dayPlanDto, emptyMeals));
    }

    @Test
    void createDayPlanWithMealsWithTransactionCompletesForEmptyMeals() {
        DayPlanDto dayPlanDto = new DayPlanDto(LocalDate.now().plusDays(1), 1L, List.of());
        User user = new User();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(dayPlanRepository.save(any(DayPlan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mealPlanService.createDayPlanWithMealsWithTransaction(dayPlanDto, List.of());

        ArgumentCaptor<DayPlan> captor = ArgumentCaptor.forClass(DayPlan.class);
        verify(dayPlanRepository).save(captor.capture());
        assertEquals(dayPlanDto.getDate(), captor.getValue().getDate());
        verify(dayPlanService).invalidateSearchCache();
    }
}
