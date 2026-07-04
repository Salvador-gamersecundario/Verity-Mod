package com.luis.verity.client

import com.luis.verity.VerityMod
import com.luis.verity.client.render.PostProcessor
import com.luis.verity.client.render.SphereRenderer
import com.luis.verity.client.render.VeritySphereEntityRenderer
import com.luis.verity.registry.ModEntities
import net.minecraft.client.Minecraft
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.EntityRenderersEvent
import net.minecraftforge.client.event.RenderLevelStageEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

@Mod.EventBusSubscriber(modid = VerityMod.MOD_ID, value = [Dist.CLIENT], bus = Mod.EventBusSubscriber.Bus.MOD)
object VerityClient {

    @SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent) {
        VerityMod.LOGGER.info("Verity Client inicializado")

        // Inicializar post-procesador
        PostProcessor.init()
    }

    @SubscribeEvent
    fun onRegisterRenderers(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerEntityRenderer(ModEntities.VERITY_SPHERE.get()) { context ->
            VeritySphereEntityRenderer(context)
        }
    }

    @Mod.EventBusSubscriber(modid = VerityMod.MOD_ID, value = [Dist.CLIENT])
    object ClientEvents {

        @SubscribeEvent
        fun onRenderWorld(event: RenderLevelStageEvent) {
            if (event.stage == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
                SphereRenderer.render(event)
            }
        }

        @SubscribeEvent
        fun onClientTick(event: TickEvent.ClientTickEvent) {
            if (event.phase == TickEvent.Phase.END) {
                PostProcessor.update()
            }
        }
    }
}