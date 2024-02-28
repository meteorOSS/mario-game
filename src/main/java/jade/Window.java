package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width;
    private int height;
    private String title;

    private static Window window;
    private long glfwWindow;

    public static float r,g,b,a;

    private static Scene currentScene = null;

    /**
     * 更改场景
     */
    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                break;
            default:
                assert false : "未知场景: "+newScene;
                break;

        }
    }

    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
        this.r = this.g = this.b = this.a = 1;
    }

    public static Window getWindow() {
        return window;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }

    public long getGlfwWindow() {
        return glfwWindow;
    }

    public static Window get(){
        if (Window.window == null) {
            Window.window = new Window();
        }
        return window;
    }


    public void run(){
        System.out.println("hello lwjgl"+ Version.getVersion());
        this.init();
        this.loop();

        // 释放内存
        glfwFreeCallbacks(this.glfwWindow);
        glfwDestroyWindow(this.glfwWindow);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init(){
        // 设置错误回调
        GLFWErrorCallback.createPrint(System.err).set();
        if(!glfwInit()){
            throw new IllegalStateException("无法初始化glfw");
        }

        // setup glfw
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE); // 设置为不可见 (等待准备工作完成)
        glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED,GLFW_TRUE);

        // create the window
        glfwWindow = glfwCreateWindow(this.width,this.height,this.title,NULL,NULL);

        if(glfwWindow == NULL){
            throw new IllegalStateException("创建GLFW窗口时发生异常");
        }

        // 设置回调
        glfwSetCursorPosCallback(this.glfwWindow, MouseListener::mousePosCallback); // 鼠标移动回调
        glfwSetMouseButtonCallback(this.glfwWindow, MouseListener::mouseButtonCallback); // 鼠标按键回调
        glfwSetScrollCallback(this.glfwWindow, MouseListener::mouseScrollCallback); // 滚轮滑动回调
        glfwSetKeyCallback(this.glfwWindow,KeyListener::keyCallback); // 按键回调

        // 设置opengl当前上下文
        glfwMakeContextCurrent(this.glfwWindow);
        // 启用垂直同步
        glfwSwapInterval(1);

        glfwShowWindow(this.glfwWindow);

        GL.createCapabilities(); //创建OpenGL的能力对象，基于当前的上下文

        Window.changeScene(0);

    }

    private boolean fadeToBlack = false;

    private void loop(){

        float beginTime = Time.getTime();
        float endTime;
        float dt = 0.0f;
        while (!glfwWindowShouldClose(this.glfwWindow)){
            // 拉取键盘，鼠标操作等事件
            glfwPollEvents();
            // 设置清屏颜色为白色，并清除颜色缓冲区
            glClearColor(r,g,b,a);
            glClear(GL_COLOR_BUFFER_BIT);

            if(dt>=0)
                currentScene.update(dt);

            // 现在，按下空格会在黑色淡入和白色之间切换!

            glfwSwapBuffers(this.glfwWindow); // 交换缓冲区

            endTime = Time.getTime();
            dt = endTime - beginTime; // 增量时间
            beginTime = endTime;
        }
    }
}
