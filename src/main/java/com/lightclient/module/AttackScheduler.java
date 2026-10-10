package com.lightclient.module;

import com.lightclient.config.Profile;

import java.security.SecureRandom;

public final class AttackScheduler {
    private static final long[] COMMON_INTERVALS = {50, 100, 150, 200};
    private final SecureRandom rng = new SecureRandom();
    private long lastAttack = 0;

    public boolean canAttack(double distance, Profile profile) {
        long now = System.currentTimeMillis();
        double cps = profile.baseCps;
        if (distance > 2.5) cps *= 0.85;

        double meanMs = 1000.0 / cps;
        double jitter = rng.nextGaussian() * profile.cpsJitter;
        double delay = meanMs + jitter;

        long since = now - lastAttack;
        for (long interval : COMMON_INTERVALS) {
            if (since != 0 && since % interval == 0) {
                delay += 3;
                break;
            }
        }
        return since >= delay;
    }

    public void mark() {
        lastAttack = System.currentTimeMillis();
    }
}
