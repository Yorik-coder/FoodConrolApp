package com.example.foodcontrol.service;

import com.example.foodcontrol.dto.UserDto;
import com.example.foodcontrol.entity.User;
import com.example.foodcontrol.mapper.UserMapper;
import com.example.foodcontrol.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private DayPlanService dayPlanService;

    @InjectMocks
    private UserService userService;

    @Test
    void createUserThrowsConflictWhenEmailExists() {
        UserDto dto = new UserDto("Ivan", "ivan@mail.com");
        when(userRepository.existsByEmailIgnoreCase("ivan@mail.com")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userService.createUser(dto));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void createUserSavesAndInvalidatesCache() {
        UserDto dto = new UserDto("Ivan", "ivan@mail.com");
        User entity = new User();
        User saved = new User();
        UserDto mapped = new UserDto("Ivan", "ivan@mail.com");

        when(userRepository.existsByEmailIgnoreCase("ivan@mail.com")).thenReturn(false);
        when(userMapper.toEntity(dto)).thenReturn(entity);
        when(userRepository.save(entity)).thenReturn(saved);
        when(userMapper.toDto(saved)).thenReturn(mapped);

        UserDto actual = userService.createUser(dto);

        assertEquals(mapped, actual);
        verify(dayPlanService).invalidateSearchCache();
    }

    @Test
    void getAllUsersMapsAll() {
        User user = new User();
        UserDto dto = new UserDto("Ivan", "ivan@mail.com");

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);

        assertEquals(List.of(dto), userService.getAllUsers());
    }

    @Test
    void getUserByIdThrowsWhenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.getUserById(1L));
    }

    @Test
    void getUserByIdReturnsDtoWhenFound() {
        User user = new User();
        UserDto dto = new UserDto("Ivan", "ivan@mail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);

        assertEquals(dto, userService.getUserById(1L));
    }

    @Test
    void deleteUserDelegatesAndInvalidatesCache() {
        userService.deleteUser(7L);

        verify(userRepository).deleteById(7L);
        verify(dayPlanService).invalidateSearchCache();
    }
}
