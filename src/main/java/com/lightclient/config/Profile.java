package com.lightclient.config;

public final class Profile {
    public enum Curve { LINEAR, EASE_OUT_QUART, EASE_OUT_EXPO, SIGMOID }

    public final String name;
    public double baseCps = 9.0;
    public double cpsJitter = 12.0;
    public double reach = 3.0;
    public boolean visibilityCheck = true;
    public double fireThreshold = 0.5;

    public double triggerReach = 3.0;
    public double reactionMeanMs = 145.0;
    public double reactionStdMs = 40.0;
    public double reactionFloorMs = 25.0;
    public double missChance = 0.05;
    public float cooldownThreshold = 0.9f;

    public Profile(String name) {
        this.name = name;
    }
}
