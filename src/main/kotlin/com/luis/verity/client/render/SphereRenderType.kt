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
            .setShaderState(RenderStateShard.ShaderStateShard(RenderType::entityTranslucentShader))
            .setTextureState(RenderStateShard.TextureStateShard(texture, false, false))
            .setTransparencyState(RenderStateShard.TransparencyStateShard("translucent", {
                net.minecraft.client.renderer.RenderSystem.enableBlend()
                net.minecraft.client.renderer.RenderSystem.defaultBlendFunc()
            }, {
                net.minecraft.client.renderer.RenderSystem.disableBlend()
            }))
            .setCullState(RenderStateShard.CullStateShard(false))
            .setLightmapState(RenderStateShard.LightmapStateShard(true))
            .setOverlayState(RenderStateShard.OverlayStateShard(true))
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
            .setShaderState(RenderStateShard.ShaderStateShard(RenderType::entityCutoutShader))
            .setTextureState(RenderStateShard.TextureStateShard(texture, false, false))
            .setTransparencyState(RenderStateShard.TransparencyStateShard("translucent", {
                net.minecraft.client.renderer.RenderSystem.enableBlend()
                net.minecraft.client.renderer.RenderSystem.defaultBlendFunc()
            }, {
                net.minecraft.client.renderer.RenderSystem.disableBlend()
            }))
            .setCullState(RenderStateShard.CullStateShard(false))
            .setWriteMaskState(RenderStateShard.WriteMaskStateShard(true, false))
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