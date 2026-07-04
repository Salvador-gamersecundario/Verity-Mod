package com.luis.verity.registry;

import com.luis.verity.VerityMod;
import com.luis.verity.entity.VeritySphereEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    // Constructor privado para que no se pueda instanciar esta clase
    private ModEntities() {}

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = 
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, VerityMod.MOD_ID);

    public static final RegistryObject<EntityType<VeritySphereEntity>> VERITY_SPHERE = 
            ENTITY_TYPES.register("verity_sphere", () -> EntityType.Builder.<VeritySphereEntity>of(
                    VeritySphereEntity::new,
                    MobCategory.MISC
            )
            .sized(0.4f, 0.7f)
            .clientTrackingRange(32)
            .updateInterval(20)
            .build("verity_sphere"));
}