package com.luis.verity

import com.luis.verity.entity.VeritySphereEntity
import com.luis.verity.registry.ModEntities
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory

object VerityMod : ModInitializer {
    const val MOD_ID = "verity-mod"
    val LOGGER = LoggerFactory.getLogger(MOD_ID)

    lateinit var VERITY_SPHERE: EntityType<VeritySphereEntity>

    override fun onInitialize() {
        LOGGER.info("Verity Mod inicializado")

        // Registrar entidad
        VERITY_SPHERE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier(MOD_ID, "verity_sphere"),
            net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
                .create(SpawnGroup.MISC) { entityType: EntityType<VeritySphereEntity>, world ->
                    VeritySphereEntity(entityType, world)
                }
                .dimensions(EntityDimensions.fixed(30.0f, 30.0f))
                .trackRangeChunks(32)
                .trackedUpdateRate(20)
                .build()
        )

        VeritySphereEntity.ENTITY_TYPE = VERITY_SPHERE
    }
}