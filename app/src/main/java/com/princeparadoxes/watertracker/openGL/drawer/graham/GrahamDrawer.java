package com.princeparadoxes.watertracker.openGL.drawer.graham;

import android.opengl.GLES20;

import com.google.fpl.liquidfun.Vec2;
import com.princeparadoxes.watertracker.openGL.drawer.Drawer;
import com.princeparadoxes.watertracker.utils.ConvexHull;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;

/**
 * A two-dimensional triangle for use as a drawn object in OpenGL ES 2.0.
 */
public class GrahamDrawer extends Drawer {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CONSTANTS  //////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public static final float PARTICLE_SIZE = 2;
    public static final int POINTS_ON_PARTICLE = 12;
    private static final int COORDS_PER_VERTEX = 2;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
    private static final int VERTEX_COUNT = 4;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private int mProgram;
    private int mPositionHandle;
    private int aTexCoord;
    private int mColorHandle;
    private int mMatrixHandle;
    private int mTextureHandle;

    private FloatBuffer mVertexData;
    private FloatBuffer mTextureData;
    private float[] mUniformMatrix;
    private float[] mTextureMatrix;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  INSTANCE  ///////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CONSTRUCTORS  ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public GrahamDrawer() {
        super(vertexShaderCode, fragmentShaderCode);
        initAttributes();
    }

    private void initAttributes() {
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        aTexCoord = GLES20.glGetAttribLocation(mProgram, "aTexCoord");
        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        // get handle to fragment shader's vMatrix member
        mMatrixHandle = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        // get handle to fragment shader's vTexture member
        mTextureHandle = GLES20.glGetUniformLocation(mProgram, "vTexture");
        GLES20.glUniform1i(mTextureHandle, 0);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(aTexCoord);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  SHADERS  ////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private static final String vertexShaderCode =
            "attribute vec2 vPosition;" +
                    "attribute vec2 aTexCoord;" +
                    "varying vec2 TexCoord;" +
                    "uniform mat4 vMatrix;" +
                    "void main() {" +
                    "  gl_Position = vec4(vPosition,0,1) * vMatrix;" +
                    "  TexCoord = aTexCoord;" +
//                    "  TexCoord = vPosition.st + 0.5;" +
//                    "  TexCoord.t = 1.0 - TexCoord.t;" +
                    "}";

    private static final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform sampler2D vTexture;" +
                    "varying vec2 TexCoord;" +
//                    "uniform vec4 vColor;" +
                    "void main() {" +
//                    "  gl_FragColor = vec4(1,0,0,1);" +
//                    "  gl_FragColor = texture2D(vTexture,TexCoord.st).rgba * vColor;" +
                    "  gl_FragColor = texture2D(vTexture, TexCoord).rgba;" +
//                    "  gl_FragColor *= gl_FragColor.a;" +
                    "}";

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  ON SURFACE CHANGED  /////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onSurfaceChanged(int particleCount, int width, int height, float virtualWidth, float virtualHeight) {
        int bufferSize = width * POINTS_ON_PARTICLE * FLOAT_BYTES;
        mVertexData = ByteBuffer.allocateDirect(bufferSize)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mTextureData = ByteBuffer.allocateDirect(bufferSize)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        final float aspectRatio = ((float) width) / ((float) height);
        mUniformMatrix = new float[]{
                .1f, 0, 0, -1,
                0, .1f * aspectRatio, 0, -1,
                0, 0, 1, 0,
                0, 0, 0, 1,
        };

        mTextureMatrix = new float[width * POINTS_ON_PARTICLE];
        for (int i = 0; i < width; i++) {
            mTextureMatrix[POINTS_ON_PARTICLE * i] = 0;
            mTextureMatrix[POINTS_ON_PARTICLE * i + 1] = 0;
            mTextureMatrix[POINTS_ON_PARTICLE * i + 2] = 0;
            mTextureMatrix[POINTS_ON_PARTICLE * i + 3] = 1;
            mTextureMatrix[POINTS_ON_PARTICLE * i + 4] = 1;
            mTextureMatrix[POINTS_ON_PARTICLE * i + 5] = 1;
            mTextureMatrix[POINTS_ON_PARTICLE * i + 6] = 0;
            mTextureMatrix[POINTS_ON_PARTICLE * i + 7] = 0;
            mTextureMatrix[POINTS_ON_PARTICLE * i + 8] = 1;
            mTextureMatrix[POINTS_ON_PARTICLE * i + 9] = 0;
            mTextureMatrix[POINTS_ON_PARTICLE * i + 10] = 1;
            mTextureMatrix[POINTS_ON_PARTICLE * i + 11] = 1;
        }
        mTextureData.put(mTextureMatrix);
        mTextureData.position(0);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  ON DRAW  ////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void draw(Vec2[] positions) {
        // Add program to OpenGL ES environment
        positions = new ConvexHull().convexHabr(positions);
        GLES20.glUseProgram(mProgram);

        float[] calculatedPositions = calculateAdditionalPoints(positions);
        mVertexData.put(calculatedPositions);
        mVertexData.position(0);

        GLES20.glVertexAttribPointer(mPositionHandle, 2, GL_FLOAT, false, 0, mVertexData);
        GLES20.glVertexAttribPointer(aTexCoord, 2, GL_FLOAT, false, 0, mTextureData);
        GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mUniformMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, calculatedPositions.length / 2);
    }

    private float[] calculateAdditionalPoints(Vec2[] positions) {
        float[] calculatedPoints = new float[positions.length * POINTS_ON_PARTICLE];
        for (int i = 0; i < positions.length; i++) {
            calculatedPoints[POINTS_ON_PARTICLE * i + 0] = positions[i].getX() - PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 1] = positions[i].getY() - PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 2] = positions[i].getX() - PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 3] = positions[i].getY() + PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 4] = positions[i].getX() + PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 5] = positions[i].getY() + PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 6] = positions[i].getX() - PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 7] = positions[i].getY() - PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 8] = positions[i].getX() + PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 9] = positions[i].getY() - PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 10] = positions[i].getX() + PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 11] = positions[i].getY() + PARTICLE_SIZE / 2;
        }
        return calculatedPoints;
    }
}
