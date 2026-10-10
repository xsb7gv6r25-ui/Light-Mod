package com.lightclient.module;

import com.lightclient.core.Module;
import com.lightclient.xray.XRayState;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public final class XRayModule extends Module {
    private final KeyBinding toggleKey;

    public XRayModule() {
        super("xray");
        this.toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.lightclient.xray",
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_X,
                "category.lightclient"));
    }

    @Override
    protected void onTick(MinecraftClient mc) {
        boolean changed = false;
        while (toggleKey.wasPressed()) {
            XRayState.toggle();
            changed = true;
        }
        if (changed && mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }
}
