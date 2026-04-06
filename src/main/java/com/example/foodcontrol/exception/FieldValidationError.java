package com.example.foodcontrol.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Validation error for a specific field")
public class FieldValidationError {

    @Schema(description = "Field name", example = "userId")
    private String field;

    @Schema(description = "Validation message", example = "must be greater than 0")
    private String message;

    public FieldValidationError() {
    }

    public FieldValidationError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
