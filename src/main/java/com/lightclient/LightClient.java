package com.lightclient;

import com.lightclient.core.ModuleManager;
import com.lightclient.module.TriggerBotModule;
import com.lightclient.module.XRayModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LightClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("lightclient");
    public static final ModuleManager MODULES = new ModuleManager();

    @Override
    public void onInitializeClient() {
        MODULES.register(new TriggerBotModule());
        MODULES.register(new XRayModule());
        ClientTickEvents.END_CLIENT_TICK.register(MODULES::tick);
        LOGGER.info("Light Client ready: {}", MODULES.diagnostics());
    }
}
