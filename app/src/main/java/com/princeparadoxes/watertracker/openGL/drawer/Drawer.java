package com.princeparadoxes.watertracker.openGL.drawer;

import android.opengl.GLES20;


import com.google.fpl.liquidfun.Vec2;

import timber.log.Timber;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_LINK_STATUS;

/**
 * A two-dimensional triangle for use as a drawn object in OpenGL ES 2.0.
 */
public abstract class Drawer {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CONSTANTS  //////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public static final int FLOAT_BYTES = Float.SIZE / Byte.SIZE;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    protected int mProgram;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CONSTRUCTORS  ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public Drawer(String vertexShaderCode, String fragmentShaderCode) {

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // creates OpenGL ES program executables
        checkLinkStatus();
    }

    private void checkLinkStatus() {
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(mProgram, GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            Timber.e("Program not linked");
        }
    }

    private int loadShader(int type, String shaderCode) {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == 0) {
            Timber.e("Shader not compile");
            return 0;
        }
        return shader;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  ON SURFACE CHANGED  /////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public abstract void onSurfaceChanged(int particleCount,
                                          int width,
                                          int height,
                                          float virtualWidth,
                                          float virtualHeight);


    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  ON DRAW  ////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public abstract void draw(Vec2[] positions);


}
