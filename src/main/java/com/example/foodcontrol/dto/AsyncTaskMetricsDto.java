package com.example.foodcontrol.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Async task metrics response")
public class AsyncTaskMetricsDto {

    @Schema(description = "Total tasks submitted", example = "120")
    private long submitted;

    @Schema(description = "Tasks currently running", example = "4")
    private long running;

    @Schema(description = "Tasks completed successfully", example = "110")
    private long succeeded;

    @Schema(description = "Tasks failed", example = "6")
    private long failed;

    public AsyncTaskMetricsDto() {
    }

    public AsyncTaskMetricsDto(long submitted, long running, long succeeded, long failed) {
        this.submitted = submitted;
        this.running = running;
        this.succeeded = succeeded;
        this.failed = failed;
    }

    public long getSubmitted() {
        return submitted;
    }

    public void setSubmitted(long submitted) {
        this.submitted = submitted;
    }

    public long getRunning() {
        return running;
    }

    public void setRunning(long running) {
        this.running = running;
    }

    public long getSucceeded() {
        return succeeded;
    }

    public void setSucceeded(long succeeded) {
        this.succeeded = succeeded;
    }

    public long getFailed() {
        return failed;
    }

    public void setFailed(long failed) {
        this.failed = failed;
    }
}
