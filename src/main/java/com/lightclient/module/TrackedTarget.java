package com.lightclient.module;

import com.lightclient.math.RingBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public final class TrackedTarget {
    public final LivingEntity entity;
    public final RingBuffer<Vec3d> positions;

    public TrackedTarget(LivingEntity entity, int historySize) {
        this.entity = entity;
        this.positions = new RingBuffer<>(historySize);
    }

    public void record() {
        positions.add(entity.getPos());
    }

    public Vec3d velocity() {
        if (positions.size() < 2) return entity.getVelocity();
        return positions.get(0).subtract(positions.get(1));
    }

    public Vec3d acceleration() {
        if (positions.size() < 3) return Vec3d.ZERO;
        Vec3d v0 = positions.get(0).subtract(positions.get(1));
        Vec3d v1 = positions.get(1).subtract(positions.get(2));
        return v0.subtract(v1);
    }

    public Vec3d predict(int ticks) {
        Vec3d p = entity.getPos();
        Vec3d v = velocity();
        Vec3d a = acceleration();
        double t = ticks;
        double gravity = 0.08 * t * t;
        return new Vec3d(
                p.x + v.x * t + 0.5 * a.x * t * t,
                p.y + v.y * t + 0.5 * a.y * t * t - gravity,
                p.z + v.z * t + 0.5 * a.z * t * t
        );
    }
}
