package com.example.foodcontrol.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodcontrol.dto.CounterValueDto;
import com.example.foodcontrol.dto.RaceConditionResultDto;
import com.example.foodcontrol.service.ConcurrencyDemoService;
import com.example.foodcontrol.service.CounterService;

@RestController
@RequestMapping("/concurrency")
@Validated
@Tag(name = "Concurrency", description = "Race condition and counter demos")
public class ConcurrencyController {

    private final ConcurrencyDemoService concurrencyDemoService;
    private final CounterService counterService;

    public ConcurrencyController(ConcurrencyDemoService concurrencyDemoService, CounterService counterService) {
        this.concurrencyDemoService = concurrencyDemoService;
        this.counterService = counterService;
    }

    @GetMapping("/race-demo")
    @Operation(summary = "Run race condition demo with 50+ threads")
    public RaceConditionResultDto runRaceDemo(
            @RequestParam(defaultValue = "64") @Min(50) int threads,
            @RequestParam(defaultValue = "10000") @Min(1) int iterationsPerThread
    ) {
        return concurrencyDemoService.runRaceDemo(threads, iterationsPerThread);
    }

    @GetMapping("/counter")
    @Operation(summary = "Get counter value")
    public CounterValueDto getCounterValue() {
        return new CounterValueDto(counterService.getValue());
    }

    @PostMapping("/counter/increment")
    @Operation(summary = "Increment counter")
    public CounterValueDto incrementCounter(
            @RequestParam(defaultValue = "1") @Min(1) long delta
    ) {
        return new CounterValueDto(counterService.incrementBy(delta));
    }
}
