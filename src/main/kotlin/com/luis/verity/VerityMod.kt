package com.luis.verity

import com.luis.verity.registry.ModEntities
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.slf4j.LoggerFactory

@Mod(VerityMod.MOD_ID)
class VerityMod {

    companion object {
        const val MOD_ID = "verity_mod"
        val LOGGER = LoggerFactory.getLogger(MOD_ID)
    }

    init {
        val modEventBus: IEventBus = FMLJavaModLoadingContext.get().modEventBus

        // Registrar entidades
        ModEntities.ENTITY_TYPES.register(modEventBus)

        // Evento de setup común
        modEventBus.addListener(this::commonSetup)

        // Registrar eventos de Forge
        MinecraftForge.EVENT_BUS.register(this)

        LOGGER.info("Verity Mod inicializado")
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            LOGGER.info("Setup común completado")
        }
    }
}