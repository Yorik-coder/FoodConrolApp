package com.example.foodcontrol.service;

import com.example.foodcontrol.entity.MealType;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DayPlanSearchCacheKeyTest {

    @Test
    void equalsAndHashCodeForSameValues() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().plusDays(1);

        DayPlanSearchCacheKey first = new DayPlanSearchCacheKey(
                "ivan",
                MealType.BREAKFAST,
                "egg",
                from,
                to,
                pageable,
                true
        );
        DayPlanSearchCacheKey second = new DayPlanSearchCacheKey(
                "ivan",
                MealType.BREAKFAST,
                "egg",
                from,
                to,
                pageable,
                true
        );

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void equalsHandlesDifferentTypesAndDifferentFields() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().plusDays(1);

        DayPlanSearchCacheKey base = new DayPlanSearchCacheKey(
                "ivan",
                MealType.BREAKFAST,
                "egg",
                from,
                to,
                pageable,
                true
        );

        assertNotEquals(null, base);
        assertNotEquals("not-a-key", base);
        assertEquals(base, base);

        assertNotEquals(base, new DayPlanSearchCacheKey(
                "other",
                MealType.BREAKFAST,
                "egg",
                from,
                to,
                pageable,
                true
        ));
        assertNotEquals(base, new DayPlanSearchCacheKey(
                "ivan",
                MealType.LUNCH,
                "egg",
                from,
                to,
                pageable,
                true
        ));
        assertNotEquals(base, new DayPlanSearchCacheKey(
                "ivan",
                MealType.BREAKFAST,
                "rice",
                from,
                to,
                pageable,
                true
        ));
        assertNotEquals(base, new DayPlanSearchCacheKey(
                "ivan",
                MealType.BREAKFAST,
                "egg",
                from.minusDays(1),
                to,
                pageable,
                true
        ));
        assertNotEquals(base, new DayPlanSearchCacheKey(
                "ivan",
                MealType.BREAKFAST,
                "egg",
                from,
                to.plusDays(1),
                pageable,
                true
        ));
        assertNotEquals(base, new DayPlanSearchCacheKey(
                "ivan",
                MealType.BREAKFAST,
                "egg",
                from,
                to,
                PageRequest.of(1, 10),
                true
        ));
        assertNotEquals(base, new DayPlanSearchCacheKey(
                "ivan",
                MealType.BREAKFAST,
                "egg",
                from,
                to,
                pageable,
                false
        ));
    }
}
