package com.luis.verity.client.render

import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderPhase
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.util.Identifier

object SphereRenderType {

    val VERITY_SPHERE: RenderLayer = createVeritySphereLayer(
        Identifier("verity", "textures/entity/verity_sphere.png")
    )

    private fun createVeritySphereLayer(texture: Identifier): RenderLayer {
        val parameters = RenderLayer.MultiPhaseParameters.builder()
            .shader(RenderPhase.POSITION_COLOR_TEXTURE_LIGHT_NORMAL_SHADER)
            .texture(RenderPhase.Texture(texture, false, false))
            .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
            .cull(RenderPhase.DISABLE_CULLING)
            .lightmap(RenderPhase.ENABLE_LIGHTMAP)
            .overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
            .build(true)

        return RenderLayer.of(
            "verity_sphere",
            VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL,
            VertexFormat.DrawMode.QUADS,
            256,
            false,
            true,
            parameters
        )
    }

    val VERITY_SPHERE_EMISSIVE: RenderLayer = createEmissiveLayer(
        Identifier("verity", "textures/entity/verity_sphere.png")
    )

    private fun createEmissiveLayer(texture: Identifier): RenderLayer {
        val parameters = RenderLayer.MultiPhaseParameters.builder()
            .shader(RenderPhase.POSITION_COLOR_TEXTURE_SHADER)
            .texture(RenderPhase.Texture(texture, false, false))
            .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
            .cull(RenderPhase.DISABLE_CULLING)
            .writeMaskState(RenderPhase.COLOR_MASK)
            .build(true)

        return RenderLayer.of(
            "verity_sphere_emissive",
            VertexFormats.POSITION_COLOR_TEXTURE,
            VertexFormat.DrawMode.QUADS,
            256,
            false,
            true,
            parameters
        )
    }
}