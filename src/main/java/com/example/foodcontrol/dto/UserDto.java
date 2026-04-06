package com.example.foodcontrol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "User payload")
public class UserDto {

    @NotBlank(message = "name is required")
    @Size(max = 100, message = "name length must be <= 100")
    @Schema(description = "User name", example = "Ivan Petrov")
    private String name;

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    @Schema(description = "User email", example = "ivan@example.com")
    private String email;

    public UserDto() {
    }

    public UserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
