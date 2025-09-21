package nesemu.io;


import org.lwjgl.glfw.GLFW;

public class GlfwKeyboardController implements Controller {

    @Override
    public boolean checkPressed(ControllerKey key) {
        switch (key) {
            case A:
                return GLFW.glfwGetKey(GLFW.glfwGetCurrentContext(), GLFW.GLFW_KEY_X) == GLFW.GLFW_PRESS;
            case B:
                return GLFW.glfwGetKey(GLFW.glfwGetCurrentContext(), GLFW.GLFW_KEY_Z) == GLFW.GLFW_PRESS;
            case SELECT:
                return GLFW.glfwGetKey(GLFW.glfwGetCurrentContext(), GLFW.GLFW_KEY_BACKSPACE) == GLFW.GLFW_PRESS;
            case START:
                return GLFW.glfwGetKey(GLFW.glfwGetCurrentContext(), GLFW.GLFW_KEY_ENTER) == GLFW.GLFW_PRESS;
            case UP:
                return GLFW.glfwGetKey(GLFW.glfwGetCurrentContext(), GLFW.GLFW_KEY_UP) == GLFW.GLFW_PRESS;
            case DOWN:
                return GLFW.glfwGetKey(GLFW.glfwGetCurrentContext(), GLFW.GLFW_KEY_DOWN) == GLFW.GLFW_PRESS;
            case LEFT:
                return GLFW.glfwGetKey(GLFW.glfwGetCurrentContext(), GLFW.GLFW_KEY_LEFT) == GLFW.GLFW_PRESS;
            case RIGHT:
                return GLFW.glfwGetKey(GLFW.glfwGetCurrentContext(), GLFW.GLFW_KEY_RIGHT) == GLFW.GLFW_PRESS;
        }

        return false;
    }
}
