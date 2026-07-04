package com.luis.verity.client.render

import com.luis.verity.entity.VeritySphereEntity
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Axis

class VeritySphereEntityRenderer(
    context: EntityRendererProvider.Context
) : EntityRenderer<VeritySphereEntity>(context) {

    init {
        shadowRadius = 0f
    }

    override fun getTextureLocation(entity: VeritySphereEntity): ResourceLocation {
        return ResourceLocation("verity", "textures/entity/verity_sphere.png")
    }

    override fun shouldRender(entity: VeritySphereEntity, frustum: net.minecraft.client.renderer.culling.Frustum, x: Double, y: Double, z: Double): Boolean {
        return true
    }

    override fun render(
        entity: VeritySphereEntity,
        entityYaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int
    ) {
        poseStack.pushPose()

        val camera = Minecraft.getInstance().gameRenderer.mainCamera

        poseStack.mulPose(Axis.YP.rotationDegrees(-camera.yRot))
        poseStack.mulPose(Axis.XP.rotationDegrees(camera.xRot))

        val scale = 15.0f
        poseStack.scale(scale, scale, scale)

        renderSphereQuad(poseStack, buffer, packedLight)

        poseStack.popPose()

        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight)
    }

    private fun renderSphereQuad(
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int
    ) {
        val vertexConsumer = buffer.getBuffer(SphereRenderType.VERITY_SPHERE)
        val matrix = poseStack.last().pose()
        val normal = poseStack.last().normal()

        val size = 1.0f

        vertexConsumer.vertex(matrix, -size, -size, 0f)
            .uv(0f, 1f)
            .color(255, 255, 255, 255)
            .uv2(packedLight)
            .normal(normal, 0f, 0f, 1f)
            .endVertex()

        vertexConsumer.vertex(matrix, size, -size, 0f)
            .uv(1f, 1f)
            .color(255, 255, 255, 255)
            .uv2(packedLight)
            .normal(normal, 0f, 0f, 1f)
            .endVertex()

        vertexConsumer.vertex(matrix, size, size, 0f)
            .uv(1f, 0f)
            .color(255, 255, 255, 255)
            .uv2(packedLight)
            .normal(normal, 0f, 0f, 1f)
            .endVertex()

        vertexConsumer.vertex(matrix, -size, size, 0f)
            .uv(0f, 0f)
            .color(255, 255, 255, 255)
            .uv2(packedLight)
            .normal(normal, 0f, 0f, 1f)
            .endVertex()
    }
}