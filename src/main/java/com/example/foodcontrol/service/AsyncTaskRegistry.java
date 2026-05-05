package com.example.foodcontrol.service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.example.foodcontrol.dto.AsyncTaskStatus;
import com.example.foodcontrol.dto.AsyncTaskStatusDto;

@Service
public class AsyncTaskRegistry {

    private final AtomicLong taskIdSequence = new AtomicLong();
    private final Map<String, TaskInfo> tasks = new ConcurrentHashMap<>();

    public AsyncTaskStatusDto createTask() {
        String taskId = String.valueOf(taskIdSequence.incrementAndGet());
        TaskInfo info = new TaskInfo(taskId);
        tasks.put(taskId, info);
        return toDto(info);
    }

    public AsyncTaskStatusDto getStatus(String taskId) {
        TaskInfo info = tasks.get(taskId);
        if (info == null) {
            return null;
        }
        return toDto(info);
    }

    public void markRunning(String taskId) {
        update(taskId, info -> {
            info.status = AsyncTaskStatus.RUNNING;
            if (info.startedAt == null) {
                info.startedAt = Instant.now();
            }
        });
    }

    public void markSuccess(String taskId) {
        update(taskId, info -> {
            info.status = AsyncTaskStatus.SUCCESS;
            info.finishedAt = Instant.now();
        });
    }

    public void markFailed(String taskId, String errorMessage) {
        update(taskId, info -> {
            info.status = AsyncTaskStatus.FAILED;
            info.errorMessage = errorMessage;
            info.finishedAt = Instant.now();
        });
    }

    private void update(String taskId, Consumer<TaskInfo> updater) {
        TaskInfo info = tasks.get(taskId);
        if (info == null) {
            return;
        }
        synchronized (info) {
            updater.accept(info);
        }
    }

    private AsyncTaskStatusDto toDto(TaskInfo info) {
        return new AsyncTaskStatusDto(
                info.taskId,
                info.status,
                info.createdAt,
                info.startedAt,
                info.finishedAt,
                info.errorMessage
        );
    }

    private static final class TaskInfo {
        private final String taskId;
        private final Instant createdAt;
        private AsyncTaskStatus status;
        private Instant startedAt;
        private Instant finishedAt;
        private String errorMessage;

        private TaskInfo(String taskId) {
            this.taskId = taskId;
            this.createdAt = Instant.now();
            this.status = AsyncTaskStatus.PENDING;
        }
    }
}
