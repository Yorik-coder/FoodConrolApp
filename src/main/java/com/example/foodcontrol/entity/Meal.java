package com.example.foodcontrol.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "day_plan_id")
    private DayPlan dayPlan;

    @OneToMany(
            mappedBy = "meal",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<MealFood> mealFoods = new ArrayList<>();

    public Meal() {
    }

    public Meal(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DayPlan getDayPlan() {
        return dayPlan;
    }

    public List<MealFood> getMealFoods() {
        return mealFoods;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDayPlan(DayPlan dayPlan) {
        this.dayPlan = dayPlan;
    }

    public void setMealFoods(List<MealFood> mealFoods) {
        this.mealFoods = mealFoods;
    }

    // helper methods

    public void addMealFood(MealFood mealFood) {
        mealFoods.add(mealFood);
        mealFood.setMeal(this);
    }

    public void removeMealFood(MealFood mealFood) {
        mealFoods.remove(mealFood);
        mealFood.setMeal(null);
    }
}
