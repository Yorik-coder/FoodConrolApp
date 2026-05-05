package com.example.foodcontrol.service;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class CounterService {

    private final AtomicLong counter = new AtomicLong();

    public long incrementBy(long delta) {
        return counter.addAndGet(delta);
    }

    public long getValue() {
        return counter.get();
    }
}
