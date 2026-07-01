package com.luis.verity.client

import com.luis.verity.VerityMod
import com.luis.verity.client.render.PostProcessor
import com.luis.verity.client.render.SphereRenderer
import com.luis.verity.client.render.VeritySphereEntityRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents

object VerityClient : ClientModInitializer {

    override fun onInitializeClient() {
        VerityMod.LOGGER.info("Verity Client inicializado")

        // Registrar renderer de entidad
        EntityRendererRegistry.register(VerityMod.VERITY_SPHERE) { context ->
            VeritySphereEntityRenderer(context)
        }

        // Inicializar post-procesador
        PostProcessor.init()

        // Renderizar esfera después del mundo (fallback)
        WorldRenderEvents.AFTER_ENTITIES.register { context ->
            SphereRenderer.render(context)
        }

        // Actualizar post-proceso cada tick
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            PostProcessor.update()
        }
    }
}