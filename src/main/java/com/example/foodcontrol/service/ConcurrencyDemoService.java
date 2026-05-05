package com.example.foodcontrol.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.example.foodcontrol.dto.RaceConditionResultDto;

@Service
public class ConcurrencyDemoService {

    public RaceConditionResultDto runRaceDemo(int threads, int iterationsPerThread) {
        if (threads < 50) {
            throw new IllegalArgumentException("Threads must be >= 50 for race condition demo");
        }
        if (iterationsPerThread <= 0) {
            throw new IllegalArgumentException("iterationsPerThread must be > 0");
        }

        UnsafeCounter unsafeCounter = new UnsafeCounter();
        SynchronizedCounter synchronizedCounter = new SynchronizedCounter();
        AtomicInteger atomicCounter = new AtomicInteger();

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch startedLatch = new CountDownLatch(1);
        CountDownLatch completedLatch = new CountDownLatch(threads);

        try {
            for (int i = 0; i < threads; i++) {
                executor.execute(() -> {
                    try {
                        startedLatch.await();
                        for (int j = 0; j < iterationsPerThread; j++) {
                            unsafeCounter.increment();
                            synchronizedCounter.increment();
                            atomicCounter.incrementAndGet();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        completedLatch.countDown();
                    }
                });
            }

            startedLatch.countDown();
            try {
                boolean completed = completedLatch.await(60, TimeUnit.SECONDS);
                if (!completed) {
                    throw new IllegalStateException("Race condition demo timed out");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Race condition demo interrupted", e);
            }
        } finally {
            shutdownExecutor(executor);
        }

        int expected = threads * iterationsPerThread;
        int unsafe = unsafeCounter.get();
        int synchronizedValue = synchronizedCounter.get();
        int atomic = atomicCounter.get();
        boolean raceConditionDetected = unsafe < expected
                && synchronizedValue == expected
                && atomic == expected;

        return new RaceConditionResultDto(
                threads,
                iterationsPerThread,
                expected,
                unsafe,
                synchronizedValue,
                atomic,
                raceConditionDetected
        );
    }

    private void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            executor.shutdownNow();
        }
    }

    private static final class UnsafeCounter {
        private int value;

        private void increment() {
            value++;
        }

        private int get() {
            return value;
        }
    }

    private static final class SynchronizedCounter {
        private int value;

        private synchronized void increment() {
            value++;
        }

        private synchronized int get() {
            return value;
        }
    }
}
