package com.luis.verity.client.render;

import com.luis.verity.VerityMod;
import net.minecraft.client.Minecraft;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class PostProcessor {

    private static boolean initialized = false;
    private static int fboId = 0;
    private static int colorTexture = 0;
    private static int depthTexture = 0;
    private static int shaderProgram = 0;
    private static int quadVao = 0;
    private static int quadVbo = 0;

    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public static void init() {
        if (initialized) return;
    }

    public static void update() {
        Minecraft minecraft = Minecraft.getInstance();
        int width = minecraft.getWindow().getWidth();
        int height = minecraft.getWindow().getHeight();

        if (width != screenWidth || height != screenHeight) {
            resize(width, height);
        }
    }

    private static void resize(int width, int height) {
        screenWidth = width;
        screenHeight = height;

        if (fboId != 0) {
            GL30.glDeleteFramebuffers(fboId);
            GL11.glDeleteTextures(colorTexture);
            GL11.glDeleteTextures(depthTexture);
        }

        fboId = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboId);

        colorTexture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTexture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, colorTexture, 0);

        depthTexture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTexture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_DEPTH_COMPONENT, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (java.nio.ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthTexture, 0);

        int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
        if (status != GL30.GL_FRAMEBUFFER_COMPLETE) {
            VerityMod.LOGGER.error("FBO incompleto: " + status);
        }

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

        if (quadVao == 0) createQuad();
        if (shaderProgram == 0) compileShaders();

        initialized = true;
    }

    private static void createQuad() {
        float[] vertices = {
            -1f, -1f, 0f, 0f,
             1f, -1f, 1f, 0f,
             1f,  1f, 1f, 1f,
            -1f,  1f, 0f, 1f
        };
        int[] indices = { 0, 1, 2, 0, 2, 3 };

        // En Java pasamos los arreglos primitivos a Buffers de LWJGL antes de subirlos
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();

        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
        indexBuffer.put(indices).flip();

        quadVao = GL30.glGenVertexArrays();
        quadVbo = GL15.glGenBuffers();
        int ebo = GL15.glGenBuffers();

        GL30.glBindVertexArray(quadVao);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, quadVbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);

        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 4 * 4, 0);
        GL20.glEnableVertexAttribArray(1) ;
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 4 * 4, 2 * 4);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);

        GL30.glBindVertexArray(0);
    }

    private static void compileShaders() {
        String vertexSrc = """
            #version 150
            in vec2 Position;
            in vec2 TexCoord;
            out vec2 texCoord;
            void main() {
                gl_Position = vec4(Position, 0.0, 1.0);
                texCoord = TexCoord;
            }
        """;

        String fragmentSrc = """
            #version 150
            uniform sampler2D sceneTexture;
            uniform vec2 resolution;
            in vec2 texCoord;
            out vec4 fragColor;

            void main() {
                vec4 color = texture(sceneTexture, texCoord);

                vec2 texel = 1.0 / resolution;
                vec4 bloom = vec4(0.0);
                float total = 0.0;

                for(int x = -2; x <= 2; x++) {
                    for(int y = -2; y <= 2; y++) {
                        vec2 offset = vec2(float(x), float(y)) * texel * 2.0;
                        vec4 sample = texture(sceneTexture, texCoord + offset);
                        float brightness = dot(sample.rgb, vec3(0.2126, 0.7152, 0.0722));
                        if(brightness > 0.7) {
                            bloom += sample;
                            total += 1.0;
                        }
                    }
                }

                if(total > 0.0) bloom /= total;

                fragColor = color + bloom * 0.5;
            }
        """;

        shaderProgram = createProgram(vertexSrc, fragmentSrc);
    }

    private static int createProgram(String vsSrc, String fsSrc) {
        int vs = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vs, vsSrc);
        GL20.glCompileShader(vs);
        checkShader(vs, "vertex");

        int fs = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fs, fsSrc);
        GL20.glCompileShader(fs);
        checkShader(fs, "fragment");

        int program = GL20.glCreateProgram();
        GL20.glAttachShader(program, vs);
        GL20.glAttachShader(program, fs);
        GL20.glLinkProgram(program);

        GL20.glDeleteShader(vs);
        GL20.glDeleteShader(fs);

        return program;
    }

    private static void checkShader(int shader, String type) {
        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            String log = GL20.glGetShaderInfoLog(shader);
            VerityMod.LOGGER.error("Error compilando shader " + type + ": " + log);
        }
    }

    public static void beginRender() {
        if (!initialized) return;
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboId);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public static void endRender() {
        if (!initialized) return;
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

        GL20.glUseProgram(shaderProgram);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTexture);
        GL20.glUniform1i(GL20.glGetUniformLocation(shaderProgram, "sceneTexture"), 0);
        GL20.glUniform2f(GL20.glGetUniformLocation(shaderProgram, "resolution"), (float) screenWidth, (float) screenHeight);

        GL30.glBindVertexArray(quadVao);
        GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0);
        GL30.glBindVertexArray(0);

        GL20.glUseProgram(0);
    }
}