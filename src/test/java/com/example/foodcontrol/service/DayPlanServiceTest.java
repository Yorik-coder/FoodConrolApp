package com.example.foodcontrol.service;

import com.example.foodcontrol.dto.DayPlanDto;
import com.example.foodcontrol.dto.DayPlanSearchFilter;
import com.example.foodcontrol.entity.DayPlan;
import com.example.foodcontrol.entity.MealType;
import com.example.foodcontrol.entity.User;
import com.example.foodcontrol.mapper.DayPlanMapper;
import com.example.foodcontrol.repository.DayPlanRepository;
import com.example.foodcontrol.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DayPlanServiceTest {

    @Mock
    private DayPlanRepository dayPlanRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DayPlanMapper dayPlanMapper;

    @InjectMocks
    private DayPlanService dayPlanService;

    @Test
    void createDayPlanUsesUserAndInvalidatesCache() {
        DayPlanDto dto = new DayPlanDto(LocalDate.now().plusDays(1), 1L, List.of());
        User user = new User();
        DayPlan saved = new DayPlan();
        DayPlanDto mapped = new DayPlanDto(LocalDate.now().plusDays(1), 1L, List.of());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(dayPlanRepository.save(any(DayPlan.class))).thenReturn(saved);
        when(dayPlanMapper.toDto(saved)).thenReturn(mapped);

        DayPlanDto actual = dayPlanService.createDayPlan(dto);

        assertEquals(mapped, actual);
    }

    @Test
    void getAllPlansMapsAll() {
        DayPlan plan = new DayPlan();
        DayPlanDto dto = new DayPlanDto(LocalDate.now().plusDays(1), 1L, List.of());

        when(dayPlanRepository.findAll()).thenReturn(List.of(plan));
        when(dayPlanMapper.toDto(plan)).thenReturn(dto);

        assertEquals(List.of(dto), dayPlanService.getAllPlans());
    }

    @Test
    void getPlanReturnsNullWhenNotFound() {
        when(dayPlanRepository.findById(99L)).thenReturn(Optional.empty());

        assertNull(dayPlanService.getPlan(99L));
    }

    @Test
    void getPlanReturnsDtoWhenFound() {
        DayPlan plan = new DayPlan();
        DayPlanDto dto = new DayPlanDto(LocalDate.now().plusDays(1), 1L, List.of());

        when(dayPlanRepository.findById(99L)).thenReturn(Optional.of(plan));
        when(dayPlanMapper.toDto(plan)).thenReturn(dto);

        assertEquals(dto, dayPlanService.getPlan(99L));
    }

    @Test
    void deletePlanDelegates() {
        dayPlanService.deletePlan(8L);

        verify(dayPlanRepository).deleteById(8L);
    }

    @Test
    void updatePlanThrowsWhenPlanMissing() {
        when(dayPlanRepository.findById(1L)).thenReturn(Optional.empty());
        DayPlanDto request = new DayPlanDto(LocalDate.now().plusDays(1), 2L, List.of());

        assertThrows(NoSuchElementException.class, () -> dayPlanService.updatePlan(1L, request));
    }

    @Test
    void updatePlanThrowsWhenUserMissing() {
        DayPlan existing = new DayPlan();
        DayPlanDto dto = new DayPlanDto(LocalDate.now().plusDays(1), 2L, List.of());

        when(dayPlanRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> dayPlanService.updatePlan(1L, dto));
    }

    @Test
    void updatePlanUpdatesAndMaps() {
        DayPlan existing = new DayPlan();
        DayPlanDto request = new DayPlanDto(LocalDate.now().plusDays(2), 2L, List.of());
        User user = new User();
        DayPlan saved = new DayPlan();
        DayPlanDto response = new DayPlanDto(LocalDate.now().plusDays(2), 2L, List.of());

        when(dayPlanRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(dayPlanRepository.save(existing)).thenReturn(saved);
        when(dayPlanMapper.toDto(saved)).thenReturn(response);

        DayPlanDto actual = dayPlanService.updatePlan(1L, request);

        assertSame(user, existing.getUser());
        assertEquals(request.getDate(), existing.getDate());
        assertEquals(response, actual);
    }

    @Test
    void searchPlansUsesJpqlAndCachesResult() {
        DayPlanSearchFilter filter = new DayPlanSearchFilter();
        filter.setUserName("ivan");
        filter.setFoodName("egg");
        filter.setMealType(MealType.BREAKFAST);
        filter.setFromDate(LocalDate.now().minusDays(1));
        filter.setToDate(LocalDate.now().plusDays(1));
        filter.setUseNative(false);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("date"));

        DayPlan entity = new DayPlan();
        DayPlanDto mappedDto = new DayPlanDto(LocalDate.now().plusDays(1), 1L, List.of());
        Page<DayPlan> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(dayPlanRepository.searchWithNestedFiltersJpql(
                eq("%ivan%"),
                eq(MealType.BREAKFAST.name()),
                eq("%egg%"),
                eq(filter.getFromDate()),
                eq(filter.getToDate()),
                any(Pageable.class)
        )).thenReturn(page);
        when(dayPlanMapper.toDto(entity)).thenReturn(mappedDto);

        Page<DayPlanDto> first = dayPlanService.searchPlans(filter, pageable);
        Page<DayPlanDto> second = dayPlanService.searchPlans(filter, pageable);

        assertNotNull(first);
        assertEquals(1, first.getTotalElements());
        assertEquals(mappedDto.getDate(), first.getContent().get(0).getDate());
        assertEquals(first.getContent().size(), second.getContent().size());
        verify(dayPlanRepository, times(1)).searchWithNestedFiltersJpql(
                any(), any(), any(), any(), any(), any(Pageable.class));
    }

    @Test
    void searchPlansUsesNativeBranch() {
        DayPlanSearchFilter filter = new DayPlanSearchFilter();
        filter.setUseNative(true);
        filter.setUserName("user");
        filter.setFoodName("food");
        filter.setMealType(MealType.LUNCH);
        filter.setFromDate(LocalDate.now().minusDays(1));
        filter.setToDate(LocalDate.now().plusDays(1));

        Pageable pageable = PageRequest.of(0, 300, Sort.by(Sort.Order.asc("date"), Sort.Order.asc("unsafe")));

        DayPlanRepository.DayPlanNativeRow row = new DayPlanRepository.DayPlanNativeRow() {
            @Override
            public Long getId() {
                return 15L;
            }

            @Override
            public LocalDate getDate() {
                return LocalDate.now();
            }

            @Override
            public Long getUserId() {
                return 2L;
            }

            @Override
            public String getMealIdsCsv() {
                return "1,2";
            }
        };

        when(dayPlanRepository.searchWithNestedFiltersNative(
                eq("user"),
                eq(MealType.LUNCH.name()),
                eq("food"),
                eq(filter.getFromDate()),
                eq(filter.getToDate()),
                any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of(row), PageRequest.of(0, 100, Sort.by("date")), 1));

        Page<DayPlanDto> result = dayPlanService.searchPlans(filter, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(15L, result.getContent().get(0).getId());
        assertEquals(List.of(1L, 2L), result.getContent().get(0).getMealIds());
        verify(dayPlanRepository, never()).searchWithNestedFiltersJpql(any(), any(), any(), any(), any(), any());
    }

    @Test
    void searchPlansHandlesBlankCsvAndUnsanitizedInput() {
        DayPlanSearchFilter filter = new DayPlanSearchFilter();
        filter.setUseNative(true);
        filter.setUserName("  ");
        filter.setFoodName("  ");

        DayPlanRepository.DayPlanNativeRow row = new DayPlanRepository.DayPlanNativeRow() {
            @Override
            public Long getId() {
                return 20L;
            }

            @Override
            public LocalDate getDate() {
                return LocalDate.now();
            }

            @Override
            public Long getUserId() {
                return 3L;
            }

            @Override
            public String getMealIdsCsv() {
                return "   ";
            }
        };

        when(dayPlanRepository.searchWithNestedFiltersNative(
                eq(null),
                eq(null),
                eq(null),
                eq(null),
                eq(null),
                any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of(row), PageRequest.of(0, 1), 1));

        Page<DayPlanDto> result = dayPlanService.searchPlans(filter, PageRequest.of(0, 1));

        assertEquals(List.of(), result.getContent().get(0).getMealIds());
    }

    @Test
    void searchPlansRejectsUnsupportedCharacters() {
        DayPlanSearchFilter filter = new DayPlanSearchFilter();
        filter.setUserName("bad!");
        PageRequest pageable = PageRequest.of(0, 10);

        assertThrows(IllegalArgumentException.class, () -> dayPlanService.searchPlans(filter, pageable));
    }

    @Test
    void searchPlansHandlesNullFiltersAndCsvWithGaps() {
        DayPlanSearchFilter filter = new DayPlanSearchFilter();
        filter.setUseNative(true);

        DayPlanRepository.DayPlanNativeRow row = new DayPlanRepository.DayPlanNativeRow() {
            @Override
            public Long getId() {
                return 30L;
            }

            @Override
            public LocalDate getDate() {
                return LocalDate.now();
            }

            @Override
            public Long getUserId() {
                return 4L;
            }

            @Override
            public String getMealIdsCsv() {
                return "1,,2";
            }
        };

        when(dayPlanRepository.searchWithNestedFiltersNative(
                eq(null),
                eq(null),
                eq(null),
                eq(null),
                eq(null),
                any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of(row), PageRequest.of(0, 1), 1));

        Page<DayPlanDto> result = dayPlanService.searchPlans(filter, PageRequest.of(0, 1));

        assertEquals(List.of(1L, 2L), result.getContent().get(0).getMealIds());
    }

    @Test
    void searchPlansHandlesNullCsv() {
        DayPlanSearchFilter filter = new DayPlanSearchFilter();
        filter.setUseNative(true);

        DayPlanRepository.DayPlanNativeRow row = new DayPlanRepository.DayPlanNativeRow() {
            @Override
            public Long getId() {
                return 31L;
            }

            @Override
            public LocalDate getDate() {
                return LocalDate.now();
            }

            @Override
            public Long getUserId() {
                return 5L;
            }

            @Override
            public String getMealIdsCsv() {
                return null;
            }
        };

        when(dayPlanRepository.searchWithNestedFiltersNative(
                eq(null),
                eq(null),
                eq(null),
                eq(null),
                eq(null),
                any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of(row), PageRequest.of(0, 1), 1));

        Page<DayPlanDto> result = dayPlanService.searchPlans(filter, PageRequest.of(0, 1));

        assertEquals(List.of(), result.getContent().get(0).getMealIds());
    }
}
