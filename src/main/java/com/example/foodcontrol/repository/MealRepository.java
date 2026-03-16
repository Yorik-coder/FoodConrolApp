package com.example.foodcontrol.repository;

import com.example.foodcontrol.entity.Meal;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
    @EntityGraph(attributePaths = {"mealFoods"})
    List<Meal> findAll();
}
