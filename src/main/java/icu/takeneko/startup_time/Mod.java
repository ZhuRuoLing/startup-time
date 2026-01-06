package icu.takeneko.startup_time;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.toast.SystemToast;

public class Mod implements ClientModInitializer {
    public static final SystemToast.Type TYPE = new SystemToast.Type(10000);
    @Override
    public void onInitializeClient() {

    }
}