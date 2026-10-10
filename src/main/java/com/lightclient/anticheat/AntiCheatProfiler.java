package com.lightclient.anticheat;

import com.lightclient.config.Profile;
import com.lightclient.config.Profiles;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.ServerInfo;

public final class AntiCheatProfiler {
    private long lastTickNanos = 0;
    private double smoothedPing = 0;
    private double smoothedTickMs = 50;
    private String serverKey = "unknown";
    private Profile current = Profiles.legit();

    public void update(MinecraftClient mc) {
        if (mc.getNetworkHandler() != null && mc.player != null) {
            PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
            if (entry != null) smoothedPing += (entry.getLatency() - smoothedPing) * 0.2;
        }

        long now = System.nanoTime();
        if (lastTickNanos != 0) {
            double dtMs = (now - lastTickNanos) / 1_000_000.0;
            if (dtMs > 0 && dtMs < 500) smoothedTickMs += (dtMs - smoothedTickMs) * 0.1;
        }
        lastTickNanos = now;

        ServerInfo info = mc.getCurrentServerEntry();
        String addr = info != null ? info.address.toLowerCase() : "singleplayer";
        serverKey = classify(addr);
        current = select();
    }

    private String classify(String addr) {
        if (addr.contains("hypixel")) return "hypixel";
        if (addr.contains("minemen") || addr.contains("mmc")) return "mmc";
        if (addr.contains("cubecraft")) return "cubecraft";
        if (addr.contains("singleplayer")) return "sp";
        return "generic";
    }

    private Profile select() {
        if (smoothedPing > 200 || smoothedTickMs > 80) return Profiles.legit();
        return switch (serverKey) {
            case "hypixel" -> Profiles.hypixel();
            case "mmc" -> Profiles.mmc();
            case "sp" -> Profiles.blatant();
            default -> smoothedPing < 80 ? Profiles.semi() : Profiles.legit();
        };
    }

    public Profile recommended() {
        return current;
    }

    public double ping() {
        return smoothedPing;
    }
}
