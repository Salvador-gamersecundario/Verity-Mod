package com.luis.verity.client.render;

import com.luis.verity.client.render.VeritySphereModel;
import com.luis.verity.entity.VeritySphereEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class VeritySphereEntityRenderer extends EntityRenderer<VeritySphereEntity> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation("verity_mod", "verity_sphere"),
            "main"
    );

    private static final ResourceLocation TEXTURE = new ResourceLocation("verity_mod", "textures/entity/verity_sphere.png");
    private final VeritySphereModel model;

    public VeritySphereEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0f;
        this.model = new VeritySphereModel(context.bakeLayer(LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(VeritySphereEntity entity) {
        return TEXTURE;
    }

    @Override
    public boolean shouldRender(VeritySphereEntity entity, Frustum frustum, double x, double y, double z) {
        return true;
    }

    @Override
    public void render(
            VeritySphereEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight
    ) {
        poseStack.pushPose();

        float scale = 0.7f;
        poseStack.scale(scale, scale, scale);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucentCull(TEXTURE));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);

        poseStack.popPose();
    }
}