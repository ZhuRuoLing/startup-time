package icu.takeneko.startup_time.mixin;

import icu.takeneko.startup_time.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.management.ManagementFactory;

@Mixin(Minecraft.class)
public class MainMenuMixin {

    @Unique
    private static boolean isStartup = true;
    @Shadow @Final private ToastManager toastManager;

    @Inject(method = "onResourceLoadFinished", at = @At("RETURN"))
    void inj(Minecraft.GameLoadCookie loadCookie, CallbackInfo ci) {
        if (!isStartup) {
            return;
        }
        long timeMillis = ManagementFactory.getRuntimeMXBean().getUptime();
        Component title = Component.translatable("startup_time.time", timeMillis/1000.0);
        Component content = Component.literal("");
        SystemToast.add(this.toastManager, Mod.TYPE, title, content);
        isStartup = false;
    }
}
