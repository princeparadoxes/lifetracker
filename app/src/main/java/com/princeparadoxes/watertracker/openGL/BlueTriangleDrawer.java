package com.princeparadoxes.watertracker.openGL;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;

/**
 * A two-dimensional triangle for use as a drawn object in OpenGL ES 2.0.
 */
public class BlueTriangleDrawer {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CONSTANTS  //////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private static final int COORDS_PER_VERTEX = 2;
    private static final int VERTEX_STRIDE = 0;
    private static final int VERTEX_COUNT = 3;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private FloatBuffer vertexBuffer;

    private int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMatrixHandle;
    private int mTextureHandle;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  INSTANCE  ///////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private static BlueTriangleDrawer instance = null;

    public static BlueTriangleDrawer getInstance() {
        if (instance == null) {
            instance = new BlueTriangleDrawer();
        }
        return instance;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CONSTRUCTORS  ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public BlueTriangleDrawer() {

        int vertexShader = loadShader(GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // creates OpenGL ES program executables

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  SHADERS  ////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  DRAW  ///////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void draw(float[] points) {
        GLES20.glUseProgram(mProgram);

        GLES20.glUniform4f(mColorHandle, 0.0f, 0.0f, 1.0f, 1.0f);

        FloatBuffer vertexData = ByteBuffer
                .allocateDirect(points.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(points);

        vertexData.position(0);
        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 2, GL_FLOAT, false, 0, vertexData);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, points.length / COORDS_PER_VERTEX);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

}
