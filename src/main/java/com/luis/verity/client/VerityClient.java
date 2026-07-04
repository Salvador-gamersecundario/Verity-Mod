package com.luis.verity.client;

import com.luis.verity.VerityMod;
import com.luis.verity.client.render.VeritySphereEntityRenderer;
import com.luis.verity.client.render.VeritySphereModel;
import com.luis.verity.registry.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

// Eventos que corren en el bus de MOD (Inicialización y registro)
@Mod.EventBusSubscriber(modid = VerityMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VerityClient {

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        VerityMod.LOGGER.info("Verity Client inicializado");
    }

    @SubscribeEvent
    public static void onRegisterRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.VERITY_SPHERE.get(), VeritySphereEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(VeritySphereEntityRenderer.LAYER_LOCATION, VeritySphereModel::createBodyLayer);
    }
}