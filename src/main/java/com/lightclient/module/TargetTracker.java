package com.lightclient.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class TargetTracker {
    private final Map<UUID, TrackedTarget> tracked = new HashMap<>();

    public void update(MinecraftClient mc) {
        if (mc.world == null || mc.player == null) return;

        Set<UUID> seen = new HashSet<>();
        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof LivingEntity living) || !isValidTarget(mc, living)) continue;
            seen.add(living.getUuid());
            tracked.computeIfAbsent(living.getUuid(), k -> new TrackedTarget(living, 12)).record();
        }
        tracked.keySet().retainAll(seen);
    }

    public static boolean isValidTarget(MinecraftClient mc, LivingEntity living) {
        if (mc.player == null) return false;
        if (living == mc.player || !living.isAlive()) return false;
        if (living instanceof PlayerEntity player) {
            if (player.isSpectator() || player.isCreative()) return false;
            var myTeam = mc.player.getScoreboardTeam();
            var theirTeam = player.getScoreboardTeam();
            if (myTeam != null && myTeam == theirTeam) return false;
        }
        return living instanceof PlayerEntity
                || living instanceof MobEntity
                || living instanceof PassiveEntity;
    }
}
