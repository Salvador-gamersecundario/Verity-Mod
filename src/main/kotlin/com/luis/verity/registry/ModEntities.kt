package com.luis.verity.registry

import com.luis.verity.VerityMod
import com.luis.verity.entity.VeritySphereEntity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModEntities {

    val ENTITY_TYPES: DeferredRegister<EntityType<*>> = DeferredRegister.create(
        ForgeRegistries.ENTITY_TYPES,
        VerityMod.MOD_ID
    )

    val VERITY_SPHERE: RegistryObject<EntityType<VeritySphereEntity>> = ENTITY_TYPES.register("verity_sphere") {
        EntityType.Builder.of(
            { entityType: EntityType<VeritySphereEntity>, world ->
                VeritySphereEntity(entityType, world)
            },
            MobCategory.MISC
        )
            .sized(30.0f, 30.0f)
            .clientTrackingRange(32)
            .updateInterval(20)
            .build("verity_sphere")
    }
}