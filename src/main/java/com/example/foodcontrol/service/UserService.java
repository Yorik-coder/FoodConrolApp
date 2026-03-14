package com.example.foodcontrol.service;

import com.example.foodcontrol.dto.UserDto;
import com.example.foodcontrol.entity.User;
import com.example.foodcontrol.mapper.UserMapper;
import com.example.foodcontrol.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto createUser(UserDto dto) {

        User user = UserMapper.toEntity(dto);

        User saved = userRepository.save(user);

        return UserMapper.toDto(saved);
    }

    public List<UserDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {

        return userRepository.findById(id)
                .map(UserMapper::toDto)
                .orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
