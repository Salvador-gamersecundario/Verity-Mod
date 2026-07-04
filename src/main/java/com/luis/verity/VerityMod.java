package com.luis.verity;

import com.luis.verity.registry.ModEntities;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(VerityMod.MOD_ID)
public class VerityMod {
    public static final String MOD_ID = "verity_mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public VerityMod() {
        // Conseguimos el bus nativo de Forge sin usar KotlinForForge
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        // Registrar entidades
        ModEntities.ENTITY_TYPES.register(bus);

        // Evento de setup común
        bus.addListener(this::commonSetup);

        // Registrar eventos de Forge
        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("Verity Mod inicializado");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.info("Setup común completado");
        });
    }
}