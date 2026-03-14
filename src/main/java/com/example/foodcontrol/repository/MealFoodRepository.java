package com.example.foodcontrol.repository;

import com.example.foodcontrol.entity.MealFood;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealFoodRepository extends JpaRepository<MealFood, Long> {
}
