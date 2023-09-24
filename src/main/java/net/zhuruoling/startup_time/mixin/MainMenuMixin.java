package net.zhuruoling.startup_time.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.management.ManagementFactory;

@Mixin(MinecraftClient.class)
public class MainMenuMixin {

    @Unique
    private static boolean isStartup = true;
    @Shadow @Final private ToastManager toastManager;

    @Inject(method = "onFinishedLoading", at = @At("RETURN"))
    void inj(MinecraftClient.LoadingContext loadingContext, CallbackInfo ci){
        if (!isStartup) {
            return;
        }
        long timeMillis = ManagementFactory.getRuntimeMXBean().getUptime();
        Text title = Text.of(String.format("游戏启动用时: %.1f秒",timeMillis / 1000.0));
        Text content = Text.of("打败了全国99.9%的玩家");
        SystemToast.show(this.toastManager, SystemToast.Type.PERIODIC_NOTIFICATION, title, content);
        isStartup = false;
    }
}
