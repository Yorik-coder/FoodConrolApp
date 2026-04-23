package com.example.foodcontrol.service;

import com.example.foodcontrol.dto.FoodDto;
import com.example.foodcontrol.entity.Food;
import com.example.foodcontrol.mapper.FoodMapper;
import com.example.foodcontrol.repository.FoodRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FoodServiceTest {

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private FoodMapper foodMapper;

    @Mock
    private DayPlanService dayPlanService;

    @InjectMocks
    private FoodService foodService;

    @Test
    void createFoodReturnsMappedDtoAndInvalidatesCache() {
        FoodDto request = new FoodDto("Egg", 150, 12, 10, 1);
        Food entity = new Food();
        Food saved = new Food();
        FoodDto response = new FoodDto("Egg", 150, 12, 10, 1);

        when(foodMapper.toEntity(request)).thenReturn(entity);
        when(foodRepository.save(entity)).thenReturn(saved);
        when(foodMapper.toDto(saved)).thenReturn(response);

        FoodDto actual = foodService.createFood(request);

        assertEquals(response, actual);
        verify(dayPlanService).invalidateSearchCache();
    }

    @Test
    void getFoodByIdReturnsNullWhenNotFound() {
        when(foodRepository.findById(10L)).thenReturn(Optional.empty());

        FoodDto actual = foodService.getFoodById(10L);

        assertNull(actual);
    }

    @Test
    void getFoodByIdReturnsDtoWhenFound() {
        Food food = new Food();
        FoodDto dto = new FoodDto("Egg", 150, 12, 10, 1);
        when(foodRepository.findById(10L)).thenReturn(Optional.of(food));
        when(foodMapper.toDto(food)).thenReturn(dto);

        FoodDto actual = foodService.getFoodById(10L);

        assertEquals(dto, actual);
    }

    @Test
    void getAllFoodsMapsAll() {
        Food first = new Food();
        Food second = new Food();
        FoodDto firstDto = new FoodDto("A", 1, 1, 1, 1);
        FoodDto secondDto = new FoodDto("B", 2, 2, 2, 2);

        when(foodRepository.findAll()).thenReturn(List.of(first, second));
        when(foodMapper.toDto(first)).thenReturn(firstDto);
        when(foodMapper.toDto(second)).thenReturn(secondDto);

        List<FoodDto> actual = foodService.getAllFoods();

        assertEquals(List.of(firstDto, secondDto), actual);
    }

    @Test
    void getFoodsByMinCaloriesMapsAll() {
        Food first = new Food();
        FoodDto firstDto = new FoodDto("A", 100, 1, 1, 1);

        when(foodRepository.findByCaloriesGreaterThan(99)).thenReturn(List.of(first));
        when(foodMapper.toDto(first)).thenReturn(firstDto);

        List<FoodDto> actual = foodService.getFoodsByMinCalories(99);

        assertEquals(List.of(firstDto), actual);
    }

    @Test
    void updateFoodReturnsNullWhenNotFound() {
        when(foodRepository.findById(10L)).thenReturn(Optional.empty());

        FoodDto actual = foodService.updateFood(10L, new FoodDto("x", 1, 1, 1, 1));

        assertNull(actual);
        verify(foodRepository, never()).save(any(Food.class));
        verify(dayPlanService, never()).invalidateSearchCache();
    }

    @Test
    void updateFoodUpdatesFieldsAndInvalidatesCache() {
        Food existing = new Food();
        existing.setName("old");
        FoodDto request = new FoodDto("new", 10, 2, 3, 4);
        Food saved = new Food();
        FoodDto mapped = new FoodDto("new", 10, 2, 3, 4);

        when(foodRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(foodRepository.save(existing)).thenReturn(saved);
        when(foodMapper.toDto(saved)).thenReturn(mapped);

        FoodDto actual = foodService.updateFood(1L, request);

        assertEquals("new", existing.getName());
        assertEquals(10, existing.getCalories());
        assertEquals(2, existing.getProtein());
        assertEquals(3, existing.getFat());
        assertEquals(4, existing.getCarbs());
        assertEquals(mapped, actual);
        verify(dayPlanService).invalidateSearchCache();
    }

    @Test
    void deleteFoodDelegatesAndInvalidatesCache() {
        foodService.deleteFood(1L);

        verify(foodRepository).deleteById(1L);
        verify(dayPlanService).invalidateSearchCache();
    }

    @Test
    void createFoodsBulkWithoutTransactionThrowsOnEmptyInput() {
        List<FoodDto> emptyDtos = List.of();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> foodService.createFoodsBulkWithoutTransaction(emptyDtos));

        assertTrue(ex.getMessage().contains("must not be empty"));
    }

    @Test
    void createFoodsBulkWithoutTransactionCreatesAllAndInvalidatesCache() {
        FoodDto first = new FoodDto("A", 1, 1, 1, 1);
        FoodDto second = new FoodDto("B", 2, 2, 2, 2);
        Food firstEntity = new Food();
        Food secondEntity = new Food();
        Food firstSaved = new Food();
        Food secondSaved = new Food();
        FoodDto firstDto = new FoodDto("A", 1, 1, 1, 1);
        FoodDto secondDto = new FoodDto("B", 2, 2, 2, 2);

        when(foodMapper.toEntity(first)).thenReturn(firstEntity);
        when(foodMapper.toEntity(second)).thenReturn(secondEntity);
        when(foodRepository.save(firstEntity)).thenReturn(firstSaved);
        when(foodRepository.save(secondEntity)).thenReturn(secondSaved);
        when(foodMapper.toDto(firstSaved)).thenReturn(firstDto);
        when(foodMapper.toDto(secondSaved)).thenReturn(secondDto);

        List<FoodDto> actual = foodService.createFoodsBulkWithoutTransaction(List.of(first, second));

        assertEquals(List.of(firstDto, secondDto), actual);
        verify(dayPlanService).invalidateSearchCache();
    }

    @Test
    void createFoodsBulkWithoutTransactionStopsOnMarker() {
        FoodDto first = new FoodDto("A", 1, 1, 1, 1);
        FoodDto marker = new FoodDto("__FAIL__", 2, 2, 2, 2);
        List<FoodDto> bulkInput = List.of(first, marker);
        Food firstEntity = new Food();
        Food markerEntity = new Food();
        Food firstSaved = new Food();
        Food markerSaved = new Food();

        when(foodMapper.toEntity(first)).thenReturn(firstEntity);
        when(foodMapper.toEntity(marker)).thenReturn(markerEntity);
        when(foodRepository.save(firstEntity)).thenReturn(firstSaved);
        when(foodRepository.save(markerEntity)).thenReturn(markerSaved);
        when(foodMapper.toDto(firstSaved)).thenReturn(first);
        when(foodMapper.toDto(markerSaved)).thenReturn(marker);

        assertThrows(IllegalStateException.class,
            () -> foodService.createFoodsBulkWithoutTransaction(bulkInput));

        verify(foodRepository, times(2)).save(any(Food.class));
        verify(dayPlanService, never()).invalidateSearchCache();
    }

    @Test
    void createFoodsBulkWithTransactionUsesSameBehavior() {
        FoodDto first = new FoodDto("A", 1, 1, 1, 1);
        Food entity = new Food();
        Food saved = new Food();

        when(foodMapper.toEntity(first)).thenReturn(entity);
        when(foodRepository.save(entity)).thenReturn(saved);
        when(foodMapper.toDto(saved)).thenReturn(first);

        List<FoodDto> actual = foodService.createFoodsBulkWithTransaction(List.of(first));

        assertEquals(List.of(first), actual);
    }
}
