package org.olaz.instasprite_be.global.util.ratelimit;

import java.time.Duration;

public final class RateLimitPolicy {
    private RateLimitPolicy() {}

    // Auth
    public static final long LOGIN_CAPACITY = 5;
    public static final Duration LOGIN_REFILL = Duration.ofMinutes(5);

    public static final long GOOGLE_LOGIN_CAPACITY = 10;
    public static final Duration GOOGLE_LOGIN_REFILL = Duration.ofMinutes(5);

    // OTP / 2FA
    public static final long OTP_CAPACITY = 10;
    public static final Duration OTP_REFILL = Duration.ofMinutes(5);
}


