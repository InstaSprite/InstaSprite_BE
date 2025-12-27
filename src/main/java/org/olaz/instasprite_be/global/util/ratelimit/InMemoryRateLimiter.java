package org.olaz.instasprite_be.global.util.ratelimit;

import org.olaz.instasprite_be.global.error.exception.RateLimitExceededException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory token bucket rate limiter.
 *
 * Notes:
 * - Resets on restart.
 * - Best effort cleanup (removes idle buckets).
 * - For production multi-instance, replace with Redis-based limiter.
 */
@Component
public class InMemoryRateLimiter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public void consumeOrThrow(String key, long capacity, Duration refillPeriod, long tokens) {
        long nowMs = System.currentTimeMillis();
        Bucket bucket = buckets.compute(key, (k, existing) -> {
            if (existing == null) {
                Bucket b = new Bucket(capacity, capacity, refillPeriod.toMillis(), nowMs, nowMs);
                b.tryConsume(tokens, nowMs);
                return b;
            }
            existing.tryConsume(tokens, nowMs);
            existing.lastSeenMs = nowMs;
            return existing;
        });

        if (!bucket.lastConsumeOk) {
            throw new RateLimitExceededException();
        }

        // cheap cleanup: avoid unbounded growth
        cleanupOccasionally(nowMs);
    }

    private volatile long lastCleanupMs = 0;

    private void cleanupOccasionally(long nowMs) {
        if (nowMs - lastCleanupMs < 60_000) {
            return;
        }
        lastCleanupMs = nowMs;

        long idleCutoff = nowMs - 15 * 60_000; // 15 min
        buckets.entrySet().removeIf(e -> e.getValue().lastSeenMs < idleCutoff);
    }

    private static final class Bucket {
        private final long capacity;
        private final long refillPeriodMs;
        private long tokens;
        private long lastRefillMs;
        private volatile long lastSeenMs;

        // compute() needs a way to expose the result
        private boolean lastConsumeOk = true;

        private Bucket(long capacity, long tokens, long refillPeriodMs, long lastRefillMs, long lastSeenMs) {
            this.capacity = capacity;
            this.tokens = tokens;
            this.refillPeriodMs = refillPeriodMs;
            this.lastRefillMs = lastRefillMs;
            this.lastSeenMs = lastSeenMs;
        }

        private void tryConsume(long n, long nowMs) {
            refillIfNeeded(nowMs);
            if (tokens >= n) {
                tokens -= n;
                lastConsumeOk = true;
            } else {
                lastConsumeOk = false;
            }
        }

        private void refillIfNeeded(long nowMs) {
            if (nowMs <= lastRefillMs) {
                return;
            }
            long elapsed = nowMs - lastRefillMs;
            if (elapsed < refillPeriodMs) {
                return;
            }
            long periods = elapsed / refillPeriodMs;
            long refill = periods * capacity;
            tokens = Math.min(capacity, tokens + refill);
            lastRefillMs += periods * refillPeriodMs;
        }
    }
}


