package com.example.foodcontrol.service;

import java.util.List;

import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Service;

import com.example.foodcontrol.dto.AsyncTaskMetricsDto;
import com.example.foodcontrol.dto.AsyncTaskStatusDto;
import com.example.foodcontrol.dto.FoodDto;

@Service
public class AsyncFoodTaskService {

    private final AsyncTaskRegistry taskRegistry;
    private final AsyncFoodTaskWorker taskWorker;
    private final AsyncTaskCounterService taskCounterService;

    public AsyncFoodTaskService(AsyncTaskRegistry taskRegistry,
                                AsyncFoodTaskWorker taskWorker,
                                AsyncTaskCounterService taskCounterService) {
        this.taskRegistry = taskRegistry;
        this.taskWorker = taskWorker;
        this.taskCounterService = taskCounterService;
    }

    public AsyncTaskStatusDto startBulkCreateFoods(List<FoodDto> dtos) {
        AsyncTaskStatusDto task = taskRegistry.createTask();
        taskCounterService.incrementSubmitted();
        try {
            taskWorker.processBulkCreateFoods(task.getTaskId(), dtos);
        } catch (TaskRejectedException ex) {
            String message = "Async queue is full";
            taskRegistry.markFailed(task.getTaskId(), message);
            taskCounterService.incrementFailed();
        }
        AsyncTaskStatusDto latest = taskRegistry.getStatus(task.getTaskId());
        return latest == null ? task : latest;
    }

    public AsyncTaskStatusDto getStatus(String taskId) {
        return taskRegistry.getStatus(taskId);
    }

    public AsyncTaskMetricsDto getMetrics() {
        return taskCounterService.getMetrics();
    }
}
