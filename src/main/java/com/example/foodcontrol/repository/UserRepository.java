package com.example.foodcontrol.repository;

import com.example.foodcontrol.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
