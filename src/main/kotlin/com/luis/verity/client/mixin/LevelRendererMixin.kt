package com.luis.verity.client.mixin

import com.luis.verity.client.render.PostProcessor
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.LevelRenderer
import org.joml.Matrix4f
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(LevelRenderer::class)
class LevelRendererMixin {

    @Inject(method = ["renderLevel"], at = [At("HEAD")])
    private fun onRenderStart(
        partialTick: Float,
        finishTimeNano: Long,
        renderBlockOutline: Boolean,
        camera: net.minecraft.client.Camera,
        gameRenderer: GameRenderer,
        lightmapTextureManager: net.minecraft.client.renderer.LightTexture,
        matrix4f: Matrix4f,
        ci: CallbackInfo
    ) {
        PostProcessor.beginRender()
    }

    @Inject(method = ["renderLevel"], at = [At("RETURN")])
    private fun onRenderEnd(
        partialTick: Float,
        finishTimeNano: Long,
        renderBlockOutline: Boolean,
        camera: net.minecraft.client.Camera,
        gameRenderer: GameRenderer,
        lightmapTextureManager: net.minecraft.client.renderer.LightTexture,
        matrix4f: Matrix4f,
        ci: CallbackInfo
    ) {
        PostProcessor.endRender()
    }
}