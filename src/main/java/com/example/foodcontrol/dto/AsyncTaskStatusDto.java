package com.example.foodcontrol.dto;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Async task status response")
public class AsyncTaskStatusDto {

    @Schema(description = "Task id", example = "1")
    private String taskId;

    @Schema(description = "Task status")
    private AsyncTaskStatus status;

    @Schema(description = "Creation time")
    private Instant createdAt;

    @Schema(description = "Start time")
    private Instant startedAt;

    @Schema(description = "Finish time")
    private Instant finishedAt;

    @Schema(description = "Error message if task failed")
    private String errorMessage;

    public AsyncTaskStatusDto() {
    }

    public AsyncTaskStatusDto(String taskId,
                              AsyncTaskStatus status,
                              Instant createdAt,
                              Instant startedAt,
                              Instant finishedAt,
                              String errorMessage) {
        this.taskId = taskId;
        this.status = status;
        this.createdAt = createdAt;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.errorMessage = errorMessage;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public AsyncTaskStatus getStatus() {
        return status;
    }

    public void setStatus(AsyncTaskStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
