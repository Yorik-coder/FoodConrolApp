package com.example.foodcontrol.service;

import com.example.foodcontrol.dto.DayPlanDto;
import com.example.foodcontrol.dto.DayPlanSearchFilter;
import com.example.foodcontrol.entity.DayPlan;
import com.example.foodcontrol.entity.MealType;
import com.example.foodcontrol.entity.User;
import com.example.foodcontrol.mapper.DayPlanMapper;
import com.example.foodcontrol.repository.DayPlanRepository;
import com.example.foodcontrol.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class DayPlanService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DayPlanService.class);

    private final DayPlanRepository dayPlanRepository;
    private final UserRepository userRepository;
    private final DayPlanMapper dayPlanMapper;
    private final Map<DayPlanSearchCacheKey, Page<DayPlanDto>> searchCache = new HashMap<>();

    public DayPlanService(DayPlanRepository dayPlanRepository,
                          UserRepository userRepository,
                          DayPlanMapper dayPlanMapper) {

        this.dayPlanRepository = dayPlanRepository;
        this.userRepository = userRepository;
        this.dayPlanMapper = dayPlanMapper;
    }

    public DayPlanDto createDayPlan(DayPlanDto dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow();

        DayPlan plan = new DayPlan();
        plan.setDate(dto.getDate());
        plan.setUser(user);

        DayPlan saved = dayPlanRepository.save(plan);
        invalidateSearchCache();

        return dayPlanMapper.toDto(saved);
    }

    public List<DayPlanDto> getAllPlans() {

        return dayPlanRepository.findAll()
                .stream()
                .map(dayPlanMapper::toDto)
                .toList();
    }

    public DayPlanDto getPlan(Long id) {

        return dayPlanRepository.findById(id)
                .map(dayPlanMapper::toDto)
                .orElse(null);
    }

    public void deletePlan(Long id) {
        dayPlanRepository.deleteById(id);
        invalidateSearchCache();
    }

    public synchronized void invalidateSearchCache() {
        LOGGER.info("DayPlan search cache invalidated, previous size={}", searchCache.size());
        searchCache.clear();
    }

    public Page<DayPlanDto> searchPlans(DayPlanSearchFilter filter,
                                        Pageable pageable) {

        String normalizedUserName = normalizeFilter(filter.getUserName());
        String normalizedFoodName = normalizeFilter(filter.getFoodName());
        String userNamePattern = toLikePattern(normalizedUserName);
        String foodNamePattern = toLikePattern(normalizedFoodName);
        MealType mealType = filter.getMealType();
        LocalDate fromDate = filter.getFromDate();
        LocalDate toDate = filter.getToDate();
        boolean useNativeQuery = filter.isUseNative();
        String mealTypeName = null;
        if (mealType != null) {
            mealTypeName = mealType.name();
        }

        DayPlanSearchCacheKey key = new DayPlanSearchCacheKey(
                normalizedUserName,
                mealType,
                normalizedFoodName,
                fromDate,
                toDate,
                pageable,
                useNativeQuery
        );

        Page<DayPlanDto> cached = searchCache.get(key);
        if (cached != null) {
            LOGGER.info("DayPlan search cache HIT: pageable={}, useNative={}", pageable, useNativeQuery);
            return cached;
        }

        LOGGER.info("DayPlan search cache MISS: pageable={}, useNative={}", pageable, useNativeQuery);

        Page<DayPlan> searchResult = useNativeQuery
                ? dayPlanRepository.searchWithNestedFiltersNative(
                        normalizedUserName,
                mealTypeName,
                        normalizedFoodName,
                        fromDate,
                        toDate,
                        pageable
                )
                : dayPlanRepository.searchWithNestedFiltersJpql(
                    userNamePattern,
                    mealTypeName,
                    foodNamePattern,
                        fromDate,
                        toDate,
                        pageable
                );

        Page<DayPlanDto> mapped = searchResult.map(dayPlanMapper::toDto);
        searchCache.put(key, mapped);
        LOGGER.info("DayPlan search cache STORE: totalElements={}, cacheSize={}",
            mapped.getTotalElements(), searchCache.size());
        return mapped;
    }

    private String normalizeFilter(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed.toLowerCase();
    }

    private String toLikePattern(String value) {
        return value == null ? null : "%" + value + "%";
    }
}
