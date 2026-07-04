package com.luis.verity.client.render

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.client.event.RenderLevelStageEvent
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import kotlin.math.*

object SphereRenderer {

    private val TEXTURE = ResourceLocation.fromNamespaceAndPath("verity", "textures/entity/verity_sphere.png")
    private var vaoId: Int = 0
    private var vboId: Int = 0
    private var eboId: Int = 0
    private var indexCount: Int = 0
    private var initialized = false

    private const val SPHERE_X = 100.0
    private const val SPHERE_Y = 120.0
    private const val SPHERE_Z = 100.0
    private const val RADIUS = 15.0f

    fun init() {
        if (initialized) return

        val (vertices, indices) = createIcosphere(2)
        indexCount = indices.size

        vaoId = GL30.glGenVertexArrays()
        vboId = GL15.glGenBuffers()
        eboId = GL15.glGenBuffers()

        GL30.glBindVertexArray(vaoId)

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW)

        GL20.glEnableVertexAttribArray(0)
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 8 * 4, 0)
        GL20.glEnableVertexAttribArray(1)
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 8 * 4, 3 * 4)
        GL20.glEnableVertexAttribArray(2)
        GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 8 * 4, 5 * 4)

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, eboId)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW)

        GL30.glBindVertexArray(0)

        initialized = true
    }

    fun render(event: RenderLevelStageEvent) {
        if (!initialized) init()

        val minecraft = Minecraft.getInstance()
        val camera = minecraft.gameRenderer.mainCamera

        val camPos = camera.position
        val dx = SPHERE_X - camPos.x
        val dy = SPHERE_Y - camPos.y
        val dz = SPHERE_Z - camPos.z
        val distance = sqrt(dx*dx + dy*dy + dz*dz)

        if (distance > 5000) return

        val poseStack = event.poseStack
        val projectionMatrix = event.projectionMatrix

        poseStack.pushPose()

        poseStack.translate(
            SPHERE_X - camPos.x,
            SPHERE_Y - camPos.y,
            SPHERE_Z - camPos.z
        )

        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-camera.yRot))
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(camera.xRot))

        poseStack.scale(RADIUS, RADIUS, RADIUS)

        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.disableCull()
        RenderSystem.depthMask(false)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(0, TEXTURE)

        // Usar el shader de Minecraft
        RenderSystem.setShader(GameRenderer::getPositionTexColorNormalShader)

        // Obtener el shader actual y aplicar matrices manualmente
        val shader = RenderSystem.getShader()
        if (shader != null) {
            val modelView = poseStack.last().pose()
            
            // Aplicar ModelViewMat uniform
            val modelViewUniform = shader.getUniform("ModelViewMat")
            modelViewUniform?.set(modelView)
            
            // Aplicar ProjMat uniform
            val projUniform = shader.getUniform("ProjMat")
            projUniform?.set(projectionMatrix)
        }

        GL30.glBindVertexArray(vaoId)
        GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_INT, 0)
        GL30.glBindVertexArray(0)

        RenderSystem.depthMask(true)
        RenderSystem.enableCull()
        RenderSystem.disableBlend()

        poseStack.popPose()
    }

    private fun createIcosphere(subdivisions: Int): Pair<FloatArray, IntArray> {
        val phi = (1.0f + sqrt(5.0f)) / 2.0f

        val verts = mutableListOf(
            floatArrayOf(-1f, phi, 0f), floatArrayOf(1f, phi, 0f),
            floatArrayOf(-1f, -phi, 0f), floatArrayOf(1f, -phi, 0f),
            floatArrayOf(0f, -1f, phi), floatArrayOf(0f, 1f, phi),
            floatArrayOf(0f, -1f, -phi), floatArrayOf(0f, 1f, -phi),
            floatArrayOf(phi, 0f, -1f), floatArrayOf(phi, 0f, 1f),
            floatArrayOf(-phi, 0f, -1f), floatArrayOf(-phi, 0f, 1f)
        )

        verts.replaceAll { v ->
            val len = sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2])
            floatArrayOf(v[0]/len, v[1]/len, v[2]/len)
        }

        val inds = mutableListOf(
            0,11,5, 0,5,1, 0,1,7, 0,7,10, 0,10,11,
            1,5,9, 5,11,4, 11,10,2, 10,7,6, 7,1,8,
            3,9,4, 3,4,2, 3,2,6, 3,6,8, 3,8,9,
            4,9,5, 2,4,11, 6,2,10, 8,6,7, 9,8,1
        )

        val midCache = mutableMapOf<Pair<Int,Int>, Int>()
        fun getMid(a: Int, b: Int): Int {
            val key = if (a < b) Pair(a,b) else Pair(b,a)
            return midCache.getOrPut(key) {
                val v1 = verts[a]
                val v2 = verts[b]
                val mid = floatArrayOf((v1[0]+v2[0])/2, (v1[1]+v2[1])/2, (v1[2]+v2[2])/2)
                val len = sqrt(mid[0]*mid[0] + mid[1]*mid[1] + mid[2]*mid[2])
                verts.add(floatArrayOf(mid[0]/len, mid[1]/len, mid[2]/len))
                verts.size - 1
            }
        }

        repeat(subdivisions) {
            val newInds = mutableListOf<Int>()
            for (i in inds.indices step 3) {
                val a = inds[i]
                val b = inds[i+1]
                val c = inds[i+2]
                val ab = getMid(a,b)
                val bc = getMid(b,c)
                val ca = getMid(c,a)
                newInds.addAll(listOf(a,ab,ca, ab,b,bc, ab,bc,ca, ca,bc,c))
            }
            inds.clear()
            inds.addAll(newInds)
        }

        val vertexData = FloatArray(verts.size * 8)
        for (i in verts.indices) {
            val v = verts[i]
            val idx = i * 8
            vertexData[idx] = v[0]
            vertexData[idx+1] = v[1]
            vertexData[idx+2] = v[2]
            vertexData[idx+3] = 0.5f + atan2(v[2], v[0]) / (2 * PI.toFloat())
            vertexData[idx+4] = 0.5f - asin(v[1]) / PI.toFloat()
            vertexData[idx+5] = v[0]
            vertexData[idx+6] = v[1]
            vertexData[idx+7] = v[2]
        }

        return Pair(vertexData, inds.toIntArray())
    }
}