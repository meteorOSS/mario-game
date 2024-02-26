package jade;

import jade.Window;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    // 单例模式实例
    private static MouseListener instance;
    // 鼠标滚轮的x和y方向滚动量
    private double scrollX, scrollY;
    // 当前和上一帧的鼠标位置
    private double xPos, yPos, lastY, lastX;
    // 当前鼠标位置的二维向量表示
    private Vector2f position = new Vector2f();
    // 记录每个鼠标按钮是否被按下的状态数组
    private boolean mouseButtonPressed[] = new boolean[4];
    // 是否处于拖拽状态
    private boolean isDragging;

    // 初始化各项参数
    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    // 处理鼠标位置变化的回调函数
    public static void mousePosCallback(long window, double xpos, double ypos) {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xpos;
        // Y位置需要翻转，因为GLFW的Y轴和屏幕的Y轴方向相反
        get().yPos = Window.getWindow().getHeight() - ypos;
        // 如果任何鼠标按钮被按下，则认为是在拖拽
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2] || get().mouseButtonPressed[3];
    }

    // 处理鼠标按钮状态变化的回调函数
    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().mouseButtonPressed[button] = true;
        } else if (action == GLFW_RELEASE) {
            get().mouseButtonPressed[button] = false;
            // 当任何按钮释放时，结束拖拽状态
            get().isDragging = false;
        }
    }

    // 处理鼠标滚轮滚动的回调函数
    public static void mouseScrollCallback(long window, double xoffset, double yoffset) {
        get().scrollX = xoffset;
        get().scrollY = yoffset;
    }

    // 获取MouseListener实例的静态方法
    public static MouseListener get() {
        if (instance == null) {
            instance = new MouseListener();
        }
        return instance;
    }

    // 每帧结束时调用，用于重置滚轮滚动量，并更新上一帧鼠标位置
    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    // 获取鼠标当前X位置
    public static float getX() {
        return (float)get().xPos;
    }
    // 获取鼠标当前Y位置
    public static float getY() {
        return (float)get().yPos;
    }
    // 获取鼠标X方向的位移
    public static float getDx() {
        return (float)(get().lastX - get().xPos);
    }
    // 获取鼠标Y方向的位移
    public static float getDy() {
        return (float)(get().lastY - get().yPos);
    }
    // 获取鼠标滚轮X方向的滚动量
    public static float getScrollX() {
        return (float)get().scrollX;
    }
    // 获取鼠标滚轮Y方向的滚动量
    public static float getScrollY() {
        return (float)get().scrollY;
    }
    // 检查鼠标是否处于拖拽状态
    public static boolean isDragging() {
        return get().isDragging;
    }

    // 检查指定按钮是否被按下
    public static boolean mouseButtonDown(int button) {
        if (button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        } else {
            return false;
        }
    }
}
