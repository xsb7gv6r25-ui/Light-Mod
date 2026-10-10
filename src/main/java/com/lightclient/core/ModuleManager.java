package com.lightclient.core;

import net.minecraft.client.MinecraftClient;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ModuleManager {
    private final List<Module> modules = new CopyOnWriteArrayList<>();

    public <T extends Module> T register(T module) {
        modules.add(module);
        return module;
    }

    public List<Module> all() {
        return List.copyOf(modules);
    }

    public void tick(MinecraftClient mc) {
        for (Module module : modules) module.safeTick(mc);
    }

    public String diagnostics() {
        StringBuilder sb = new StringBuilder("modules: ");
        for (Module module : modules) {
            sb.append(module.id()).append(module.errors() > 0 ? "!" : "+").append(' ');
        }
        return sb.toString();
    }
}
