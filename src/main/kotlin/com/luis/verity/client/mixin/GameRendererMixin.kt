package com.luis.verity.client.mixin

import com.luis.verity.client.render.PostProcessor
import net.minecraft.client.render.GameRenderer
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(GameRenderer::class)
class GameRendererMixin {

    @Inject(method = ["render"], at = [At("HEAD")])
    private fun onRenderStart(tickDelta: Float, startTime: Long, tick: Boolean, ci: CallbackInfo) {
        PostProcessor.update()
    }
}