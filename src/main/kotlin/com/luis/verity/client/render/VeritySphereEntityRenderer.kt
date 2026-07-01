package com.luis.verity.client.render

import com.luis.verity.entity.VeritySphereEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.Frustum
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.RotationAxis

class VeritySphereEntityRenderer(
    context: EntityRendererFactory.Context
) : EntityRenderer<VeritySphereEntity>(context) {

    init {
        shadowRadius = 0f
    }

    override fun getTexture(entity: VeritySphereEntity): Identifier {
        return Identifier("verity", "textures/entity/verity_sphere.png")
    }

    override fun shouldRender(entity: VeritySphereEntity, frustum: Frustum, x: Double, y: Double, z: Double): Boolean {
        return true
    }

    override fun render(
        entity: VeritySphereEntity,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int
    ) {
        matrices.push()

        val camera = MinecraftClient.getInstance().gameRenderer.camera

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.yaw))
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.pitch))

        val scale = 15.0f
        matrices.scale(scale, scale, scale)

        renderSphereQuad(matrices, vertexConsumers, light)

        matrices.pop()

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)
    }

    private fun renderSphereQuad(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int
    ) {
        val buffer = vertexConsumers.getBuffer(SphereRenderType.VERITY_SPHERE)
        val matrix = matrices.peek().positionMatrix
        val normal = matrices.peek().normalMatrix

        val size = 1.0f

        buffer.vertex(matrix, -size, -size, 0f)
            .texture(0f, 1f)
            .color(255, 255, 255, 255)
            .light(light)
            .normal(normal, 0f, 0f, 1f)
            .next()

        buffer.vertex(matrix, size, -size, 0f)
            .texture(1f, 1f)
            .color(255, 255, 255, 255)
            .light(light)
            .normal(normal, 0f, 0f, 1f)
            .next()

        buffer.vertex(matrix, size, size, 0f)
            .texture(1f, 0f)
            .color(255, 255, 255, 255)
            .light(light)
            .normal(normal, 0f, 0f, 1f)
            .next()

        buffer.vertex(matrix, -size, size, 0f)
            .texture(0f, 0f)
            .color(255, 255, 255, 255)
            .light(light)
            .normal(normal, 0f, 0f, 1f)
            .next()
    }
}