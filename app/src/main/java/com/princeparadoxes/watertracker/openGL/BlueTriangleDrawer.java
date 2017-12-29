package com.princeparadoxes.watertracker.openGL;

import android.opengl.GLES20;

import com.google.fpl.liquidfun.Vec2;
import com.princeparadoxes.watertracker.openGL.drawer.Drawer;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;

/**
 * A two-dimensional triangle for use as a drawn object in OpenGL ES 2.0.
 */
public class BlueTriangleDrawer extends Drawer{

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
        super(VERTEX_SHADER_CODE, FRAGMENT_SHADER_CODE);
        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  SHADERS  ////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private static final String VERTEX_SHADER_CODE =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private static final String FRAGMENT_SHADER_CODE =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  DRAW  ///////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onSurfaceChanged(int particleCount, int width, int height, float virtualWidth, float virtualHeight) {

    }

    @Override
    public void draw(Vec2[] positions) {
        GLES20.glUseProgram(mProgram);

        GLES20.glUniform4f(mColorHandle, 0.0f, 0.0f, 1.0f, 1.0f);
//
//        FloatBuffer vertexData = ByteBuffer
//                .allocateDirect(positions.length * 4)
//                .order(ByteOrder.nativeOrder())
//                .asFloatBuffer();
//        vertexData.put(points);
//
//        vertexData.position(0);
//        // Enable a handle to the triangle vertices
//        GLES20.glEnableVertexAttribArray(mPositionHandle);
//        GLES20.glVertexAttribPointer(mPositionHandle, 2, GL_FLOAT, false, 0, vertexData);
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, points.length / COORDS_PER_VERTEX);
//        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }


}
