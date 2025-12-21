package org.olaz.instasprite_be.global.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.olaz.instasprite_be.global.annotation.Timed;

import lombok.extern.slf4j.Slf4j;

/**
 * AOP handler for @Timed annotation
 * Measures and logs method execution time
 * Only active in 'dev' and 'local' profiles to avoid production overhead
 */
@Aspect
@Component
@Slf4j
@Profile({"dev", "local"})
public class TimedAOPHandler {

    @Around("@annotation(org.olaz.instasprite_be.global.annotation.Timed)")
    public Object calculateTime(ProceedingJoinPoint pjp) throws Throwable {
        final long startTime = System.nanoTime();
        final Object result = pjp.proceed();
        final long endTime = System.nanoTime();
        final long takenTime = endTime - startTime;
        
        log.info("@Timed Method '{}' takes {}ns ({}ms)", 
            pjp.getSignature().toShortString(), 
            takenTime,
            takenTime / 1_000_000);
        
        return result;
    }
}

