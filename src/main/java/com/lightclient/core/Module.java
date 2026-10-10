package com.lightclient.core;

import net.minecraft.client.MinecraftClient;

public abstract class Module {
    private final String id;
    private volatile boolean enabled = true;
    private volatile int errors = 0;
    private volatile String lastError = null;

    protected Module(String id) {
        this.id = id;
    }

    public final String id() {
        return id;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(boolean value) {
        this.enabled = value;
    }

    public final int errors() {
        return errors;
    }

    public final String lastError() {
        return lastError;
    }

    public final void safeTick(MinecraftClient mc) {
        if (!enabled) return;
        try {
            onTick(mc);
            errors = 0;
        } catch (Throwable t) {
            errors++;
            lastError = t.getClass().getSimpleName() + ": " + t.getMessage();
            if (errors <= 3) com.lightclient.LightClient.LOGGER.warn("[{}] tick error", id, t);
        }
    }

    protected void onTick(MinecraftClient mc) {}
}
