package com.example.foodcontrol.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Async task status")
public enum AsyncTaskStatus {
    PENDING,
    RUNNING,
    SUCCESS,
    FAILED
}
