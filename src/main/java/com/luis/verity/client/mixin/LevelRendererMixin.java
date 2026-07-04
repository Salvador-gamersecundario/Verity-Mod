package com.luis.verity.client.mixin;

import com.luis.verity.client.render.PostProcessor;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.LevelRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void onRenderStart(
            float partialTick,
            long finishTimeNano,
            boolean renderBlockOutline,
            Camera camera,
            GameRenderer gameRenderer,
            LightTexture lightmapTextureManager,
            Matrix4f matrix4f,
            CallbackInfo ci
    ) {
        PostProcessor.beginRender();
    }

    @Inject(method = "renderLevel", at = @At("RETURN"))
    private void onRenderEnd(
            float partialTick,
            long finishTimeNano,
            boolean renderBlockOutline,
            Camera camera,
            GameRenderer gameRenderer,
            LightTexture lightmapTextureManager,
            Matrix4f matrix4f,
            CallbackInfo ci
    ) {
        PostProcessor.endRender();
    }
}