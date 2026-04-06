package com.example.foodcontrol.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceExecutionTimeAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceExecutionTimeAspect.class);

    @Around("execution(* com.example.foodcontrol.service..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            LOGGER.info("Service method {} executed in {} ms", joinPoint.getSignature().toShortString(), elapsedMs);
        }
    }
}
