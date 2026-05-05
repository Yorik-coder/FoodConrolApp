package com.example.foodcontrol.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Counter value response")
public class CounterValueDto {

    @Schema(description = "Current counter value", example = "42")
    private long value;

    public CounterValueDto() {
    }

    public CounterValueDto(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
