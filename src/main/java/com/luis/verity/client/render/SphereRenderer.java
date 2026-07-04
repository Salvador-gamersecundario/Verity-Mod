package com.luis.verity.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SphereRenderer {

    private static final ResourceLocation TEXTURE = new ResourceLocation("verity_mod", "textures/entity/verity_sphere.png");
    private static int vaoId = 0;
    private static int vboId = 0;
    private static int eboId = 0;
    private static int indexCount = 0;
    private static boolean initialized = false;

    private static final double SPHERE_X = 100.0;
    private static final double SPHERE_Y = 120.0;
    private static final double SPHERE_Z = 100.0;
    private static final float RADIUS = 15.0f;

    public static void init() {
        if (initialized) return;

        IcosphereData data = createIcosphere(2);
        indexCount = data.indices.length;

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(data.vertices.length);
        vertexBuffer.put(data.vertices).flip();

        IntBuffer indexBuffer = BufferUtils.createIntBuffer(data.indices.length);
        indexBuffer.put(data.indices).flip();

        vaoId = GL30.glGenVertexArrays();
        vboId = GL15.glGenBuffers();
        eboId = GL15.glGenBuffers();

        GL30.glBindVertexArray(vaoId);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);

        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 8 * 4, 0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 8 * 4, 3 * 4);
        GL20.glEnableVertexAttribArray(2);
        GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 8 * 4, 5 * 4);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, eboId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);

        GL30.glBindVertexArray(0);

        initialized = true;
    }

    public static void render(RenderLevelStageEvent event) {
        if (!initialized) init();

        Minecraft minecraft = Minecraft.getInstance();
        Camera camera = minecraft.gameRenderer.getMainCamera();

        Vec3 camPos = camera.getPosition();
        double dx = SPHERE_X - camPos.x;
        double dy = SPHERE_Y - camPos.y;
        double dz = SPHERE_Z - camPos.z;
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (distance > 5000) return;

        PoseStack poseStack = event.getPoseStack();
        Matrix4f projectionMatrix = event.getProjectionMatrix();

        poseStack.pushPose();

        poseStack.translate(
                SPHERE_X - camPos.x,
                SPHERE_Y - camPos.y,
                SPHERE_Z - camPos.z
        );

        poseStack.mulPose(Axis.YP.rotationDegrees(-camera.getYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));

        poseStack.scale(RADIUS, RADIUS, RADIUS);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        RenderSystem.setShader(GameRenderer::getPositionTexColorNormalShader);

        ShaderInstance shader = RenderSystem.getShader();
        if (shader != null) {
            Matrix4f modelView = poseStack.last().pose();

            if (shader.getUniform("ModelViewMat") != null) {
                shader.getUniform("ModelViewMat").set(modelView);
            }
            if (shader.getUniform("ProjMat") != null) {
                shader.getUniform("ProjMat").set(projectionMatrix);
            }
        }

        GL30.glBindVertexArray(vaoId);
        GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_INT, 0);
        GL30.glBindVertexArray(0);

        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();

        poseStack.popPose();
    }

    private static IcosphereData createIcosphere(int subdivisions) {
        float phi = (1.0f + (float) Math.sqrt(5.0f)) / 2.0f;

        List<float[]> verts = new ArrayList<>();
        verts.add(new float[]{-1f, phi, 0f}); verts.add(new float[]{1f, phi, 0f});
        verts.add(new float[]{-1f, -phi, 0f}); verts.add(new float[]{1f, -phi, 0f});
        verts.add(new float[]{0f, -1f, phi}); verts.add(new float[]{0f, 1f, phi});
        verts.add(new float[]{0f, -1f, -phi}); verts.add(new float[]{0f, 1f, -phi});
        verts.add(new float[]{phi, 0f, -1f}); verts.add(new float[]{phi, 0f, 1f});
        verts.add(new float[]{-phi, 0f, -1f}); verts.add(new float[]{-phi, 0f, 1f});

        for (int i = 0; i < verts.size(); i++) {
            float[] v = verts.get(i);
            float len = (float) Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
            verts.set(i, new float[]{v[0] / len, v[1] / len, v[2] / len});
        }

        List<Integer> inds = new ArrayList<>();
        int[] initialIndices = {
            0,11,5, 0,5,1, 0,1,7, 0,7,10, 0,10,11,
            1,5,9, 5,11,4, 11,10,2, 10,7,6, 7,1,8,
            3,9,4, 3,4,2, 3,2,6, 3,6,8, 3,8,9,
            4,9,5, 2,4,11, 6,2,10, 8,6,7, 9,8,1
        };
        for (int idx : initialIndices) inds.add(idx);

        Map<String, Integer> midCache = new HashMap<>();

        for (int s = 0; s < subdivisions; s++) {
            List<Integer> newInds = new ArrayList<>();
            for (int i = 0; i < inds.size(); i += 3) {
                int a = inds.get(i);
                int b = inds.get(i + 1);
                int c = inds.get(i + 2);

                int ab = getMid(a, b, verts, midCache);
                int bc = getMid(b, c, verts, midCache);
                int ca = getMid(c, a, verts, midCache);

                newInds.add(a);  newInds.add(ab); newInds.add(ca);
                newInds.add(ab); newInds.add(b);  newInds.add(bc);
                newInds.add(ab); newInds.add(bc); newInds.add(ca);
                newInds.add(ca); newInds.add(bc); newInds.add(c);
            }
            inds.clear();
            inds.addAll(newInds);
        }

        float[] vertexData = new float[verts.size() * 8];
        for (int i = 0; i < verts.size(); i++) {
            float[] v = verts.get(i);
            int idx = i * 8;
            vertexData[idx] = v[0];
            vertexData[idx + 1] = v[1];
            vertexData[idx + 2] = v[2];
            vertexData[idx + 3] = 0.5f + (float) Math.atan2(v[2], v[0]) / (2 * (float) Math.PI);
            vertexData[idx + 4] = 0.5f - (float) Math.asin(v[1]) / (float) Math.PI;
            vertexData[idx + 5] = v[0];
            vertexData[idx + 6] = v[1];
            vertexData[idx + 7] = v[2];
        }

        int[] indexData = new int[inds.size()];
        for (int i = 0; i < inds.size(); i++) {
            indexData[i] = inds.get(i);
        }

        return new IcosphereData(vertexData, indexData);
    }

    private static int getMid(int a, int b, List<float[]> verts, Map<String, Integer> midCache) {
        String key = a < b ? a + "_" + b : b + "_" + a;
        if (midCache.containsKey(key)) {
            return midCache.get(key);
        }

        float[] v1 = verts.get(a);
        float[] v2 = verts.get(b);
        float[] mid = {(v1[0] + v2[0]) / 2f, (v1[1] + v2[1]) / 2f, (v1[2] + v2[2]) / 2f};
        float len = (float) Math.sqrt(mid[0] * mid[0] + mid[1] * mid[1] + mid[2] * mid[2]);
        
        verts.add(new float[]{mid[0] / len, mid[1] / len, mid[2] / len});
        int index = verts.size() - 1;
        midCache.put(key, index);
        return index;
    }

    private static class IcosphereData {
        float[] vertices;
        int[] indices;

        IcosphereData(float[] vertices, int[] indices) {
            this.vertices = vertices;
            this.indices = indices;
        }
    }
}