package jade;

import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL20.*;

public class LevelEditorScene extends Scene{

    private String vertexShaderSrc = "#version 330 core\n" +
            "\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main(){\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos,1.0);\n" +
            "}";
    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main(){\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexId,fragmentID,shaderProgram;

    private float[] vertexArray = {
            // position            // color
            0.5f,-0.5f,0.0f,       1.0f,0.0f,0.0f,1.0f,
            -0.5f,0.5f,0.0f,       0.0f,1.0f,0.0f,1.0f,
            0.5f,0.5f,0.0f,        0.0f,0.0f,1.0f,1.0f,
            -0.5f,-0.5f,0.0f,      1.0f,1.0f,0.0f,1.0f
    };

    private int[] elementArray = {
            2,1,0,
            0,1,3
    };

    private int vaoID,vboID,eboID;

    @Override
    public void init() {
        // 编译，链接着色器

        // 载入顶点着色器
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        // 将着色器源码传递至GPU
        glShaderSource(vertexId,vertexShaderSrc);
        glCompileShader(vertexId);
        // 检查错误
        if(glGetShaderi(vertexId,GL_COMPILE_STATUS) == GL_FALSE){
            int len = glGetShaderi(vertexId,GL_INFO_LOG_LENGTH);
            System.out.println("error: defaultSaderh.glsl");
            System.out.println(glGetShaderInfoLog(vertexId,len));
            assert false : "";
        }

        // 对片段着色做同样的事情
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID,fragmentShaderSrc);
        glCompileShader(fragmentID);
        if(glGetShaderi(fragmentID,GL_COMPILE_STATUS) == GL_FALSE){
            int len = glGetShaderi(vertexId,GL_INFO_LOG_LENGTH);
            System.out.println("error: defaultSaderh.glsl");
            System.out.println(glGetShaderInfoLog(vertexId,len));
            assert false : "";
        }

        // 链接着色器
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram,vertexId);
        glAttachShader(shaderProgram,fragmentID);
        glLinkProgram(shaderProgram);

        if(glGetProgrami(shaderProgram,GL_LINK_STATUS) == GL_FALSE){
            int len = glGetProgrami(shaderProgram,GL_INFO_LOG_LENGTH);
            System.out.println(glGetProgramInfoLog(shaderProgram,len));
        }

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        glBufferData(GL_ARRAY_BUFFER,vertexBuffer,GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,elementBuffer,GL_STATIC_DRAW);

        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize)*floatSizeBytes;

        glVertexAttribPointer(0,positionsSize,GL_FLOAT,false,vertexSizeBytes,0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1,colorSize,GL_FLOAT,false,vertexSizeBytes,positionsSize*floatSizeBytes);
        glEnableVertexAttribArray(1);



    }

    public LevelEditorScene(){
    }

    @Override
    public void update(float dt) {
        // 绑定着色器
        glUseProgram(shaderProgram);
        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES,elementArray.length,GL_UNSIGNED_INT,0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        glBindVertexArray(1);
    }
}
