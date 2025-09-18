package nesemu.window;

import com.google.inject.Inject;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_BUFFER_BIT;

public class Window {

    private long window;

    private KeyboardHandler keyboardHandler;

    @Inject
    public Window(KeyboardHandler keyboardHandler) {
        this.keyboardHandler = keyboardHandler;
    }

    public void init() {
        initGlfw();
        initWindow();

        GL.createCapabilities();
        GL11.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
    }

    public void cycle() {
        GL11C.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    public void destroy() {
        Callbacks.glfwFreeCallbacks(window);
        GLFW.glfwDestroyWindow(window);

        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    private void initWindow() {
        window = GLFW.glfwCreateWindow(300, 300, "NESEMU", MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        GLFW.glfwSetKeyCallback(window, keyboardHandler);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            // Get the window size passed to glfwCreateWindow
            GLFW.glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            // Center the window
            GLFW.glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        GLFW.glfwMakeContextCurrent(window);
        // v-sync
        GLFW.glfwSwapInterval(1);

        GLFW.glfwShowWindow(window);
    }

    private void initGlfw() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
    }
}
