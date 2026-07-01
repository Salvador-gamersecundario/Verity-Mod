package com.luis.verity.client.mixin

import com.luis.verity.client.render.PostProcessor
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.WorldRenderer
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(WorldRenderer::class)
class LevelRendererMixin {

    @Inject(method = ["render"], at = [At("HEAD")])
    private fun onRenderStart(
        tickDelta: Float,
        limitTime: Long,
        renderBlockOutline: Boolean,
        camera: net.minecraft.client.render.Camera,
        gameRenderer: GameRenderer,
        lightmapTextureManager: net.minecraft.client.render.LightmapTextureManager,
        matrix4f: org.joml.Matrix4f,
        ci: CallbackInfo
    ) {
        PostProcessor.beginRender()
    }

    @Inject(method = ["render"], at = [At("RETURN")])
    private fun onRenderEnd(
        tickDelta: Float,
        limitTime: Long,
        renderBlockOutline: Boolean,
        camera: net.minecraft.client.render.Camera,
        gameRenderer: GameRenderer,
        lightmapTextureManager: net.minecraft.client.render.LightmapTextureManager,
        matrix4f: org.joml.Matrix4f,
        ci: CallbackInfo
    ) {
        PostProcessor.endRender()
    }
}