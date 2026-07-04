package com.luis.verity.client.render

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation

object SphereRenderType {

    val VERITY_SPHERE: RenderType = createVeritySphereLayer(
        ResourceLocation("verity", "textures/entity/verity_sphere.png")
    )

    private fun createVeritySphereLayer(texture: ResourceLocation): RenderType {
        val compositeState = RenderType.CompositeState.builder()
            .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
            .setTextureState(RenderStateShard.TextureStateShard(texture, false, false))
            .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
            .setCullState(RenderStateShard.NO_CULL)
            .setLightmapState(RenderStateShard.LIGHTMAP)
            .setOverlayState(RenderStateShard.OVERLAY)
            .createCompositeState(true)

        return RenderType.create(
            "verity_sphere",
            DefaultVertexFormat.NEW_ENTITY,
            VertexFormat.Mode.QUADS,
            256,
            true,
            true,
            compositeState
        )
    }

    val VERITY_SPHERE_EMISSIVE: RenderType = createEmissiveLayer(
        ResourceLocation("verity", "textures/entity/verity_sphere.png")
    )

    private fun createEmissiveLayer(texture: ResourceLocation): RenderType {
        val compositeState = RenderType.CompositeState.builder()
            .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_CUTOUT_SHADER)
            .setTextureState(RenderStateShard.TextureStateShard(texture, false, false))
            .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
            .setCullState(RenderStateShard.NO_CULL)
            .setWriteMaskState(RenderStateShard.COLOR_WRITE)
            .createCompositeState(true)

        return RenderType.create(
            "verity_sphere_emissive",
            DefaultVertexFormat.POSITION_COLOR_TEX,
            VertexFormat.Mode.QUADS,
            256,
            false,
            true,
            compositeState
        )
    }
}