package com.example.foodcontrol.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(description = "Unified API error response")
public class ApiErrorResponse {

    @Schema(description = "Timestamp when error happened", example = "2026-04-07T10:15:30Z")
    private Instant timestamp;

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "HTTP error", example = "Bad Request")
    private String error;

    @Schema(description = "Business/validation message", example = "Validation failed")
    private String message;

    @Schema(description = "Request path", example = "/dayplans/search")
    private String path;

    @Schema(description = "Detailed field validation errors")
    private List<FieldValidationError> fieldErrors;

    public ApiErrorResponse() {
    }

    public ApiErrorResponse(Instant timestamp,
                            int status,
                            String error,
                            String message,
                            String path,
                            List<FieldValidationError> fieldErrors) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.fieldErrors = fieldErrors;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public List<FieldValidationError> getFieldErrors() {
        return fieldErrors;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setFieldErrors(List<FieldValidationError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
