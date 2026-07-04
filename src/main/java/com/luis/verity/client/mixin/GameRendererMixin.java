package com.luis.verity.client.mixin;

import com.luis.verity.client.render.PostProcessor;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderStart(float partialTick, long nanoTime, boolean renderLevel, CallbackInfo ci) {
        PostProcessor.update();
    }
}