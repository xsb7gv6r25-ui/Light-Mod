package com.lightclient.xray;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

import java.util.Set;

public final class XRayState {
    private static volatile boolean enabled = false;

    private static final Set<Block> VISIBLE = Set.of(
            Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE,
            Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE,
            Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE,
            Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE,
            Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE,
            Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE,
            Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE,
            Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE,
            Blocks.NETHER_GOLD_ORE, Blocks.NETHER_QUARTZ_ORE,
            Blocks.ANCIENT_DEBRIS,
            Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.ENDER_CHEST,
            Blocks.SPAWNER,
            Blocks.WATER, Blocks.LAVA
    );

    private XRayState() {}

    public static boolean isEnabled() {
        return enabled;
    }

    public static void toggle() {
        enabled = !enabled;
    }

    public static void setEnabled(boolean value) {
        enabled = value;
    }

    public static boolean shouldRender(BlockState state) {
        return !enabled || VISIBLE.contains(state.getBlock());
    }
}
