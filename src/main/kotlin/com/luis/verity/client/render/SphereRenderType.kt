package com.luis.verity.client.render

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import com.mojang.blaze3d.systems.RenderSystem

object SphereRenderType {

    // Usamos un RenderType existente de Minecraft como base
    fun veritySphere(texture: ResourceLocation): RenderType {
        return RenderType.entityTranslucentCull(texture)
    }

    // Versión emissive (brilla en la oscuridad)
    fun veritySphereEmissive(texture: ResourceLocation): RenderType {
        return RenderType.entityTranslucentEmissive(texture)
    }
}