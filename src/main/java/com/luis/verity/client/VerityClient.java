package com.luis.verity.client;

import com.luis.verity.VerityMod;
import com.luis.verity.client.render.PostProcessor;
import com.luis.verity.client.render.SphereRenderer;
import com.luis.verity.client.render.VeritySphereEntityRenderer;
import com.luis.verity.registry.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

// Eventos que corren en el bus de MOD (Inicialización y registro)
@Mod.EventBusSubscriber(modid = VerityMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VerityClient {

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        VerityMod.LOGGER.info("Verity Client inicializado");

        // Inicializar post-procesador
        PostProcessor.init();
    }

    @SubscribeEvent
    public static void onRegisterRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        // En Java pasamos la referencia del constructor usando "VeritySphereEntityRenderer::new"
        event.registerEntityRenderer(ModEntities.VERITY_SPHERE.get(), VeritySphereEntityRenderer::new);
    }

    // Eventos del juego que corren en el bus de FORGE (Ticks y Renders del mundo)
    @Mod.EventBusSubscriber(modid = VerityMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientEvents {

        @SubscribeEvent
        public static void onRenderWorld(final RenderLevelStageEvent event) {
            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
                SphereRenderer.render(event);
            }
        }

        @SubscribeEvent
        public static void onClientTick(final TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                PostProcessor.update();
            }
        }
    }
}