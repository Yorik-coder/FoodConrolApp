package com.example.foodcontrol.service;

import com.example.foodcontrol.dto.UserDto;
import com.example.foodcontrol.entity.User;
import com.example.foodcontrol.mapper.UserMapper;
import com.example.foodcontrol.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper) {

        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDto createUser(UserDto dto) {

        User user = userMapper.toEntity(dto);

        User saved = userRepository.save(user);

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
                .orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
