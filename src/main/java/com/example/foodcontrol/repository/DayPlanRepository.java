package com.example.foodcontrol.repository;

import com.example.foodcontrol.entity.DayPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayPlanRepository extends JpaRepository<DayPlan, Long> {
}
