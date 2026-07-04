package com.luis.verity.client.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class SphereRenderType {

    private SphereRenderType() {}

    // Usamos un RenderType existente de Minecraft como base
    public static RenderType veritySphere(ResourceLocation texture) {
        return RenderType.entityTranslucentCull(texture);
    }

    // Versión emissive (brilla en la oscuridad)
    public static RenderType veritySphereEmissive(ResourceLocation texture) {
        return RenderType.entityTranslucentEmissive(texture);
    }
}