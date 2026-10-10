package com.lightclient.core;

public final class AttackGate {
    private static long lastTick = Long.MIN_VALUE;

    private AttackGate() {}

    public static boolean claim(long tick) {
        if (tick == lastTick) return false;
        lastTick = tick;
        return true;
    }
}
