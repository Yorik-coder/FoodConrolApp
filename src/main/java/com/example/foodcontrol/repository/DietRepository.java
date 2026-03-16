package com.example.foodcontrol.repository;

import com.example.foodcontrol.entity.Diet;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DietRepository extends JpaRepository<Diet, Long> {
    @EntityGraph(attributePaths = {"foods"})
    List<Diet> findAll();
}
