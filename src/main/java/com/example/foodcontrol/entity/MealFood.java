package com.example.foodcontrol.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "meal_foods")
public class MealFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double grams;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    public MealFood() {
    }

    public MealFood(Long id, double grams) {
        this.id = id;
        this.grams = grams;
    }

    //getters and setters

    public Long getId() {
        return id;
    }

    public double getGrams() {
        return grams;
    }

    public Meal getMeal() {
        return meal;
    }

    public Food getFood() {
        return food;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGrams(double grams) {
        this.grams = grams;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
