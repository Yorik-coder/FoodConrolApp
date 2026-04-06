package com.example.foodcontrol.entity;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.SQLInsert;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@SQLInsert(sql = "INSERT INTO meals (day_plan_id, meal_type) VALUES (?, ?::meal_type_enum)")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "meal_type_enum", nullable = false)
    private MealType mealType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "day_plan_id", nullable = false)
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

    public Meal(Long id, MealType mealType) {
        this.id = id;
        this.mealType = mealType;
    }

    public Long getId() {
        return id;
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
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

    

    public void setDayPlan(DayPlan dayPlan) {
        this.dayPlan = dayPlan;
    }

    public void setMealFoods(List<MealFood> mealFoods) {
        this.mealFoods = mealFoods;
    }


    public void addMealFood(MealFood mealFood) {
        mealFoods.add(mealFood);
        mealFood.setMeal(this);
    }

    public void removeMealFood(MealFood mealFood) {
        mealFoods.remove(mealFood);
        mealFood.setMeal(null);
    }
}
