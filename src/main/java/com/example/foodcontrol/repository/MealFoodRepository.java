package com.example.foodcontrol.repository;

import java.util.List;

import com.example.foodcontrol.entity.MealFood;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealFoodRepository extends JpaRepository<MealFood, Long> {
	List<MealFood> findAllByMealId(Long mealId);
}
