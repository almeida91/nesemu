package nesemu.window;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;

public class Texture {
    private static final int BORDER = 0;
    private static final int LEVEL = 0;

    private int id;
    private int width;
    private int height;

    public Texture(int width, int height, ByteBuffer buffer) {
        this.width = width;
        this.height = height;

        id = GL11.glGenTextures();

        bind();

        GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        loadBuffer(buffer);
    }

    public void bind() {
        GL11.glBindTexture(GL_TEXTURE_2D, id);
    }

    public void cleanup() {
        GL11.glDeleteTextures(id);
    }

    public void unbind() {
        GL11.glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void load(ByteBuffer buffer) {
        bind();
        loadBuffer(buffer);
    }

    private void loadBuffer(ByteBuffer buffer) {
        GL11.glTexImage2D(GL_TEXTURE_2D, LEVEL, GL_RGBA, width, height, BORDER, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
    }

}
