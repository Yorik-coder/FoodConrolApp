package com.example.foodcontrol.repository;

import com.example.foodcontrol.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
}
