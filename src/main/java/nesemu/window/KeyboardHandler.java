package nesemu.window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyboardHandler implements GLFWKeyCallbackI {

    @Override
    public void invoke(long window, int key, int scanCode, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
            GLFW.glfwSetWindowShouldClose(window, true);
        }
    }
}
