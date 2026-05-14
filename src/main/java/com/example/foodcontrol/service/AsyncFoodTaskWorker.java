package com.example.foodcontrol.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.foodcontrol.dto.FoodDto;

@Service
public class AsyncFoodTaskWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncFoodTaskWorker.class);
    private static final long DEMO_DELAY_MS = 15000;

    private final AsyncTaskRegistry taskRegistry;
    private final FoodService foodService;
    private final AsyncTaskCounterService taskCounterService;

    public AsyncFoodTaskWorker(AsyncTaskRegistry taskRegistry,
                               FoodService foodService,
                               AsyncTaskCounterService taskCounterService) {
        this.taskRegistry = taskRegistry;
        this.foodService = foodService;
        this.taskCounterService = taskCounterService;
    }

    @Async("asyncTaskExecutor")
    public CompletableFuture<Void> processBulkCreateFoods(String taskId, List<FoodDto> dtos) {
        taskRegistry.markRunning(taskId);
        taskCounterService.incrementRunning();
        try {
            Thread.sleep(DEMO_DELAY_MS);
            foodService.createFoodsBulkWithTransaction(dtos);
            taskRegistry.markSuccess(taskId);
            taskCounterService.incrementSucceeded();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            String message = "Async task interrupted";
            LOGGER.warn("Async bulk food task {} interrupted", taskId);
            taskRegistry.markFailed(taskId, message);
            taskCounterService.incrementFailed();
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null || message.isBlank()) {
                message = e.getClass().getSimpleName();
            }
            LOGGER.warn("Async bulk food task {} failed: {}", taskId, message);
            taskRegistry.markFailed(taskId, message);
            taskCounterService.incrementFailed();
        } finally {
            taskCounterService.decrementRunning();
        }
        return CompletableFuture.completedFuture(null);
    }
}
