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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class DayPlanService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DayPlanService.class);
    private static final int MAX_PAGE_SIZE = 100;
    private static final Pattern SAFE_SEARCH_TEXT = Pattern.compile("[\\p{L}\\p{N}\\s'\\-]*");
    private static final Set<String> ALLOWED_SORT_PROPERTIES = Set.of("id", "date");

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

    public DayPlanDto updatePlan(Long id, DayPlanDto dto) {
        DayPlan existing = dayPlanRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("DayPlan not found with id: " + id));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + dto.getUserId()));

        existing.setDate(dto.getDate());
        existing.setUser(user);

        DayPlan updated = dayPlanRepository.save(existing);
        invalidateSearchCache();
        return dayPlanMapper.toDto(updated);
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
        Pageable safePageable = sanitizePageable(pageable);
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
                safePageable,
                useNativeQuery
        );

        Page<DayPlanDto> cached = searchCache.get(key);
        if (cached != null) {
            LOGGER.info("DayPlan search cache HIT");
            return cached;
        }

        LOGGER.info("DayPlan search cache MISS");

        Page<DayPlanDto> mapped;
        if (useNativeQuery) {
            Page<DayPlanRepository.DayPlanNativeRow> nativeResult = dayPlanRepository.searchWithNestedFiltersNative(
                normalizedUserName,
                mealTypeName,
                normalizedFoodName,
                fromDate,
                toDate,
                safePageable
            );
            mapped = nativeResult.map(this::toDtoForNative);
        } else {
            Page<DayPlan> jpqlResult = dayPlanRepository.searchWithNestedFiltersJpql(
                userNamePattern,
                mealTypeName,
                foodNamePattern,
                fromDate,
                toDate,
                safePageable
            );
            mapped = jpqlResult.map(dayPlanMapper::toDto);
        }

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
        if (trimmed.isEmpty()) {
            return null;
        }
        if (!SAFE_SEARCH_TEXT.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("Search filter contains unsupported characters");
        }
        return trimmed.toLowerCase();
    }

    private String toLikePattern(String value) {
        return value == null ? null : "%" + value + "%";
    }

    private Pageable sanitizePageable(Pageable pageable) {
        int pageNumber = Math.clamp(pageable.getPageNumber(), 0, Integer.MAX_VALUE);
        int pageSize = Math.clamp(pageable.getPageSize(), 1, MAX_PAGE_SIZE);

        List<Sort.Order> safeOrders = pageable.getSort().stream()
                .filter(order -> ALLOWED_SORT_PROPERTIES.contains(order.getProperty()))
                .map(order -> new Sort.Order(order.getDirection(), order.getProperty()))
                .toList();

        Sort safeSort = safeOrders.isEmpty()
                ? Sort.unsorted()
            : Sort.by(safeOrders);

        return PageRequest.of(pageNumber, pageSize, safeSort);
    }

    private DayPlanDto toDtoForNative(DayPlanRepository.DayPlanNativeRow row) {
        DayPlanDto dto = new DayPlanDto();
        dto.setId(row.getId());
        dto.setDate(row.getDate());
        dto.setUserId(row.getUserId());
        dto.setMealIds(parseMealIdsCsv(row.getMealIdsCsv()));
        return dto;
    }

    private List<Long> parseMealIdsCsv(String mealIdsCsv) {
        if (mealIdsCsv == null || mealIdsCsv.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(mealIdsCsv.split(","))
                .filter(part -> !part.isBlank())
                .map(Long::valueOf)
                .toList();
    }
}
