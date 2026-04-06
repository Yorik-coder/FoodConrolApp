package com.example.foodcontrol.repository;

import com.example.foodcontrol.entity.DayPlan;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface DayPlanRepository extends JpaRepository<DayPlan, Long> {
    @EntityGraph(attributePaths = {"meals"})
    List<DayPlan> findAll();

    @Query(
        value = """
            select distinct dp
            from DayPlan dp
            join dp.user u
            left join dp.meals m
            left join m.mealFoods mf
            left join mf.food f
            where (:userNamePattern is null or lower(u.name) like :userNamePattern)
              and (:mealType is null or cast(m.mealType as string) = :mealType)
              and (:foodNamePattern is null or lower(f.name) like :foodNamePattern)
              and (:fromDate is null or dp.date >= :fromDate)
              and (:toDate is null or dp.date <= :toDate)
            """,
        countQuery = """
            select count(distinct dp)
            from DayPlan dp
            join dp.user u
            left join dp.meals m
            left join m.mealFoods mf
            left join mf.food f
            where (:userNamePattern is null or lower(u.name) like :userNamePattern)
              and (:mealType is null or cast(m.mealType as string) = :mealType)
              and (:foodNamePattern is null or lower(f.name) like :foodNamePattern)
              and (:fromDate is null or dp.date >= :fromDate)
              and (:toDate is null or dp.date <= :toDate)
            """
    )
    Page<DayPlan> searchWithNestedFiltersJpql(
        @Param("userNamePattern") String userNamePattern,
      @Param("mealType") String mealType,
        @Param("foodNamePattern") String foodNamePattern,
        @Param("fromDate") LocalDate fromDate,
        @Param("toDate") LocalDate toDate,
        Pageable pageable
    );

    @Query(
        value = """
            select distinct dp.*
            from day_plans dp
            join users u on u.id = dp.user_id
            left join meals m on m.day_plan_id = dp.id
            left join meal_foods mf on mf.meal_id = m.id
            left join foods f on f.id = mf.food_id
            where (:userName is null or lower(u.name) like lower(concat('%', :userName, '%')))
              and (:mealType is null or cast(m.meal_type as text) = :mealType)
              and (:foodName is null or lower(f.name) like lower(concat('%', :foodName, '%')))
              and (:fromDate is null or dp.date >= :fromDate)
              and (:toDate is null or dp.date <= :toDate)
            """,
        countQuery = """
            select count(distinct dp.id)
            from day_plans dp
            join users u on u.id = dp.user_id
            left join meals m on m.day_plan_id = dp.id
            left join meal_foods mf on mf.meal_id = m.id
            left join foods f on f.id = mf.food_id
            where (:userName is null or lower(u.name) like lower(concat('%', :userName, '%')))
              and (:mealType is null or cast(m.meal_type as text) = :mealType)
              and (:foodName is null or lower(f.name) like lower(concat('%', :foodName, '%')))
              and (:fromDate is null or dp.date >= :fromDate)
              and (:toDate is null or dp.date <= :toDate)
            """,
        nativeQuery = true
    )
    Page<DayPlan> searchWithNestedFiltersNative(
        @Param("userName") String userName,
        @Param("mealType") String mealType,
        @Param("foodName") String foodName,
        @Param("fromDate") LocalDate fromDate,
        @Param("toDate") LocalDate toDate,
        Pageable pageable
    );
}
