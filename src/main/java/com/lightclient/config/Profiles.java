package com.lightclient.config;

public final class Profiles {
    private Profiles() {}

    public static Profile legit() {
        Profile p = new Profile("LEGIT");
        p.cooldownThreshold = 0.96f;
        p.missChance = 0.06;
        p.reactionMeanMs = 150;
        p.reactionStdMs = 42;
        p.triggerReach = 2.98;
        return p;
    }

    public static Profile semi() {
        Profile p = new Profile("SEMI");
        p.baseCps = 11;
        p.reach = 3.1;
        p.cooldownThreshold = 0.85f;
        p.missChance = 0.04;
        p.reactionMeanMs = 120;
        p.reactionStdMs = 30;
        p.triggerReach = 3.05;
        return p;
    }

    public static Profile hypixel() {
        Profile p = new Profile("HYPIXEL");
        p.baseCps = 9.4;
        p.cpsJitter = 14;
        p.reach = 3.05;
        p.cooldownThreshold = 0.9f;
        p.missChance = 0.05;
        p.reactionMeanMs = 132;
        p.reactionStdMs = 34;
        p.triggerReach = 3.0;
        return p;
    }

    public static Profile mmc() {
        Profile p = new Profile("MMC");
        p.baseCps = 8.4;
        p.cpsJitter = 10;
        p.reach = 3.0;
        p.cooldownThreshold = 0.97f;
        p.missChance = 0.03;
        p.reactionMeanMs = 112;
        p.reactionStdMs = 26;
        p.triggerReach = 3.0;
        return p;
    }

    public static Profile blatant() {
        Profile p = new Profile("BLATANT");
        p.baseCps = 14;
        p.cpsJitter = 20;
        p.reach = 3.4;
        p.visibilityCheck = false;
        p.fireThreshold = 0.3;
        p.cooldownThreshold = 0.6f;
        p.missChance = 0.01;
        p.reactionMeanMs = 72;
        p.reactionStdMs = 18;
        p.triggerReach = 3.2;
        return p;
    }
}
