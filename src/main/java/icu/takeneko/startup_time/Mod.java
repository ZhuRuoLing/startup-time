package icu.takeneko.startup_time;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.components.toasts.SystemToast;

public class Mod implements ClientModInitializer {
    public static final SystemToast.SystemToastId TYPE = new SystemToast.SystemToastId(10000);
    @Override
    public void onInitializeClient() {

    }
}