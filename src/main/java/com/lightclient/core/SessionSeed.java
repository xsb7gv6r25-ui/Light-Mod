package com.lightclient.core;

import java.security.SecureRandom;

public final class SessionSeed {
    private static final long SEED = new SecureRandom().nextLong();

    private SessionSeed() {}

    public static long value() {
        return SEED;
    }
}
