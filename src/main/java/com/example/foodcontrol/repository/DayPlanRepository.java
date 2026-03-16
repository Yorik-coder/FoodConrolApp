package com.example.foodcontrol.repository;

import com.example.foodcontrol.entity.DayPlan;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DayPlanRepository extends JpaRepository<DayPlan, Long> {
    @EntityGraph(attributePaths = {"meals"})
    List<DayPlan> findAll();
}
