package com.example.foodcontrol.service;

import com.example.foodcontrol.dto.UserDto;
import com.example.foodcontrol.entity.User;
import com.example.foodcontrol.mapper.UserMapper;
import com.example.foodcontrol.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final DayPlanService dayPlanService;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       DayPlanService dayPlanService) {

        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.dayPlanService = dayPlanService;
    }

    public UserDto createUser(UserDto dto) {

        if (userRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "User with email already exists: " + dto.getEmail());
        }

        User user = userMapper.toEntity(dto);

        User saved = userRepository.save(user);
        dayPlanService.invalidateSearchCache();

        return userMapper.toDto(saved);
    }

    public List<UserDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto getUserById(Long id) {

        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    }

    public UserDto updateUser(Long id, UserDto dto) {

        User existing = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));

        String newEmail = dto.getEmail();
        if (newEmail != null && !newEmail.equalsIgnoreCase(existing.getEmail())
                && userRepository.existsByEmailIgnoreCase(newEmail)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "User with email already exists: " + newEmail);
        }

        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());

        User saved = userRepository.save(existing);
        dayPlanService.invalidateSearchCache();

        return userMapper.toDto(saved);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        dayPlanService.invalidateSearchCache();
    }
}
