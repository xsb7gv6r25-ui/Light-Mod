package com.lightclient.module;

import com.lightclient.anticheat.AntiCheatProfiler;
import com.lightclient.config.Profile;
import com.lightclient.core.AttackGate;
import com.lightclient.core.Module;
import com.lightclient.core.SessionSeed;
import com.lightclient.math.AimModel;
import com.lightclient.math.ValueNoise;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.SplittableRandom;
import java.util.UUID;

public final class TriggerBotModule extends Module {
    private enum State { IDLE, REACTING, READY }

    private final AntiCheatProfiler profiler = new AntiCheatProfiler();
    private final AimModel model = new AimModel(0.08, 1e-4);
    private final AttackScheduler scheduler = new AttackScheduler();
    private final ValueNoise noise = new ValueNoise(SessionSeed.value() ^ 0x9E3779B97F4A7C15L);
    private final SplittableRandom rng = new SplittableRandom(SessionSeed.value());

    private State state = State.IDLE;
    private long reactionStartMs = 0;
    private double reactionDelayMs = 0;
    private boolean decided = false;
    private boolean skipSwing = false;
    private UUID lastTargetId = null;
    private double driftPhase = 0;
    private Pending pending;
    private int hits = 0;
    private int misses = 0;

    public TriggerBotModule() {
        super("triggerbot");
    }

    @Override
    protected void onTick(MinecraftClient mc) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;

        resolvePending(mc);
        profiler.update(mc);
        Profile profile = profiler.recommended();

        LivingEntity target = crosshairTarget(mc, profile);
        long now = System.currentTimeMillis();

        if (target == null || !TargetTracker.isValidTarget(mc, target)) {
            state = State.IDLE;
            return;
        }

        float cooldown = mc.player.getAttackCooldownProgress(0.5f);
        if (cooldown < profile.cooldownThreshold) return;
        if (!scheduler.canAttack(mc.player.distanceTo(target), profile)) return;

        switch (state) {
            case IDLE -> {
                boolean newTarget = lastTargetId == null || !lastTargetId.equals(target.getUuid());
                reactionDelayMs = sampleReaction(profile, newTarget);
                reactionStartMs = now;
                decided = false;
                state = State.REACTING;
            }
            case REACTING -> {
                if (now - reactionStartMs < reactionDelayMs) return;
                state = State.READY;
            }
            case READY -> {
                if (!decided) {
                    decided = true;
                    double[] f = features(mc, target, cooldown);
                    skipSwing = rng.nextDouble() < profile.missChance
                            || model.predict(f) < profile.fireThreshold;
                }
                if (skipSwing) {
                    state = State.IDLE;
                    return;
                }
                if (AttackGate.claim(mc.world.getTime())) {
                    attack(mc, target, cooldown);
                    lastTargetId = target.getUuid();
                    scheduler.mark();
                    state = State.IDLE;
                }
            }
        }
    }

    private LivingEntity crosshairTarget(MinecraftClient mc, Profile profile) {
        HitResult hit = mc.player.raycast(profile.triggerReach, 1.0f, false);
        if (hit instanceof EntityHitResult entityHit) {
            Entity entity = entityHit.getEntity();
            if (entity instanceof LivingEntity living) return living;
        }
        return null;
    }

    private double sampleReaction(Profile profile, boolean newTarget) {
        double base = newTarget ? profile.reactionMeanMs : profile.reactionFloorMs;
        double std = newTarget ? profile.reactionStdMs : profile.reactionStdMs * 0.4;
        driftPhase += 0.05;
        double drift = noise.sample(driftPhase) * profile.reactionStdMs * 0.3;
        double value = base + rng.nextGaussian() * std + drift;
        return Math.max(profile.reactionFloorMs, value);
    }

    private void attack(MinecraftClient mc, LivingEntity entity, float cooldown) {
        float health = entity.getHealth();
        double[] f = features(mc, entity, cooldown);
        mc.interactionManager.attackEntity(mc.player, entity);
        mc.player.swingHand(Hand.MAIN_HAND);
        pending = new Pending(f, entity, health, mc.world.getTime());
    }

    private void resolvePending(MinecraftClient mc) {
        if (pending == null) return;
        if (mc.world.getTime() - pending.tick < 2) return;
        boolean hit = !pending.entity.isAlive()
                || pending.entity.getHealth() < pending.health - 1e-4;
        model.train(pending.features, hit ? 1 : 0);
        if (hit) hits++; else misses++;
        pending = null;
    }

    private double[] features(MinecraftClient mc, LivingEntity entity, float cooldown) {
        return new double[]{
                mc.player.distanceTo(entity),
                entity.getVelocity().length(),
                Math.min(profiler.ping() / 200.0, 1.0),
                cooldown,
                entity.getHealth() / Math.max(entity.getMaxHealth(), 1f)
        };
    }

    public int hits() {
        return hits;
    }

    public int misses() {
        return misses;
    }

    private record Pending(double[] features, LivingEntity entity, float health, long tick) {}
}
