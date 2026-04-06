package com.example.foodcontrol.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI foodControlOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("FoodControl API")
                        .description("API for managing users, foods, diets, day plans, meals and meal-food links")
                        .version("v1")
                        .contact(new Contact().name("FoodControl Team")));
    }
}