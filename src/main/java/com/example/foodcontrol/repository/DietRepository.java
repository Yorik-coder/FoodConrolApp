package com.example.foodcontrol.repository;

import com.example.foodcontrol.entity.Diet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DietRepository extends JpaRepository<Diet, Long> {
}
