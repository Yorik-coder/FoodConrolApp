package com.example.foodcontrol.service;

import com.example.foodcontrol.dto.DayPlanDto;
import com.example.foodcontrol.entity.DayPlan;
import com.example.foodcontrol.entity.User;
import com.example.foodcontrol.mapper.DayPlanMapper;
import com.example.foodcontrol.repository.DayPlanRepository;
import com.example.foodcontrol.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DayPlanService {

    private final DayPlanRepository dayPlanRepository;
    private final UserRepository userRepository;

    public DayPlanService(DayPlanRepository dayPlanRepository, UserRepository userRepository) {
        this.dayPlanRepository = dayPlanRepository;
        this.userRepository = userRepository;
    }

    public DayPlanDto createDayPlan(DayPlanDto dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow();

        DayPlan plan = new DayPlan();
        plan.setDate(dto.getDate());
        plan.setUser(user);

        DayPlan saved = dayPlanRepository.save(plan);

        return DayPlanMapper.toDto(saved);
    }

    public List<DayPlanDto> getAllPlans() {
        return dayPlanRepository.findAll()
                .stream()
                .map(DayPlanMapper::toDto)
                .collect(Collectors.toList());
    }

    public DayPlanDto getPlan(Long id) {
        return dayPlanRepository.findById(id)
                .map(DayPlanMapper::toDto)
                .orElse(null);
    }

    public void deletePlan(Long id) {
        dayPlanRepository.deleteById(id);
    }
}
