package com.example.foodcontrol.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodcontrol.dto.AsyncTaskMetricsDto;
import com.example.foodcontrol.dto.AsyncTaskStatusDto;
import com.example.foodcontrol.dto.FoodDto;
import com.example.foodcontrol.service.AsyncFoodTaskService;

@RestController
@RequestMapping("/foods/async")
@Validated
@Tag(name = "Async Foods", description = "Async operations for foods")
public class AsyncFoodTaskController {

    private final AsyncFoodTaskService asyncFoodTaskService;

    public AsyncFoodTaskController(AsyncFoodTaskService asyncFoodTaskService) {
        this.asyncFoodTaskService = asyncFoodTaskService;
    }

    @PostMapping("/bulk")
    @Operation(summary = "Start async bulk food creation")
    public AsyncTaskStatusDto startBulkCreate(@Valid @RequestBody List<@Valid FoodDto> dtos) {
        return asyncFoodTaskService.startBulkCreateFoods(dtos);
    }

    @GetMapping("/tasks/{taskId}")
    @Operation(summary = "Get async task status")
    public ResponseEntity<AsyncTaskStatusDto> getStatus(@PathVariable String taskId) {
        AsyncTaskStatusDto status = asyncFoodTaskService.getStatus(taskId);
        if (status == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(status);
    }

    @GetMapping("/metrics")
    @Operation(summary = "Get async task metrics")
    public AsyncTaskMetricsDto getMetrics() {
        return asyncFoodTaskService.getMetrics();
    }
}
