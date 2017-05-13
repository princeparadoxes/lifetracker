package com.princeparadoxes.watertracker.openGL.drawer.grid;

import android.opengl.GLES20;

import com.google.fpl.liquidfun.Vec2;
import com.princeparadoxes.watertracker.openGL.drawer.Drawer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import timber.log.Timber;

import static android.opengl.GLES20.GL_FLOAT;

/**
 * A two-dimensional triangle for use as a drawn object in OpenGL ES 2.0.
 */
public class GridDrawer extends Drawer {

    public static final int POINTS_ON_PARTICLE = 12;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CONSTANTS  //////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private static final int COORDS_PER_VERTEX = 2;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
    private static final int VERTEX_COUNT = 4;
    private static final float PARTICLE_SIZE = 1f;
    private static final float HALF_PARTICLE_SIZE = PARTICLE_SIZE / 2f;
    private static final float MULTIPLIER = 3f;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private float mAspectRatio;
    private GridCalculator mGridCalculator;

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

    public GridDrawer() {
        super(VERTEX_SHADER_CODE, FRAGMENT_SHADER_CODE);
        initAttributes();
    }

    private void initAttributes() {
        // get handle to vertex shader's vPosition member
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

    private final static String VERTEX_SHADER_CODE =
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

    private final static String FRAGMENT_SHADER_CODE =
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

//    private static final String VERTEX_SHADER_CODE =
//            "attribute vec2 vPosition;" +
//                    "uniform mat4 vMatrix;" +
//                    "void main() {" +
//                    "  gl_Position = vec4(vPosition,0,1) * vMatrix;" +
////                    "  gl_Position = vPosition;" +
//                    "}";
//
//    private static final String FRAGMENT_SHADER_CODE =
//            "precision mediump float;" +
//                    "uniform vec4 vColor;" +
//                    "void main() {" +
//                                        "  gl_FragColor = vec4(1,0,0,1);" +
//
////                    "  gl_FragColor = vColor;" +
//                    "}";


    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  ON SURFACE CHANGED  /////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onSurfaceChanged(int particleCount, int width, int height, float virtualWidth, float virtualHeight) {
        mGridCalculator = new GridCalculator(MULTIPLIER, virtualWidth, virtualHeight, PARTICLE_SIZE);
        mAspectRatio = (float) (virtualWidth / Math.ceil(virtualHeight));

        int countPoints = (int) (virtualWidth * MULTIPLIER * virtualHeight * MULTIPLIER * POINTS_ON_PARTICLE);
        int bufferSize = countPoints * FLOAT_BYTES;

        createVertexBuffer(bufferSize);
        createUniformMatrix();
        createTextureBuffer(countPoints, bufferSize);
        GLES20.glUniform4f(mColorHandle, 0.0f, 0.0f, 1.0f, 1.0f);
    }

    private void createTextureBuffer(int countPoints, int bufferSize) {

        mTextureData = ByteBuffer.allocateDirect(bufferSize)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        mTextureMatrix = new float[countPoints];
        for (int i = 0, end = countPoints / POINTS_ON_PARTICLE; i < end; i++) {
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

    private void createVertexBuffer(int bufferSize) {
        mVertexData = ByteBuffer.allocateDirect(bufferSize)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
    }

    private void createUniformMatrix() {
        mUniformMatrix = new float[]{
                .1f, 0, 0, -1,
                0, .1f * mAspectRatio, 0, -1,
                0, 0, 1, 0,
                0, 0, 0, 1,
        };
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  ON DRAW  ////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void draw(Vec2[] positions) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        long startTime = System.currentTimeMillis();
        mGridCalculator.fillGrid(positions);
        Timber.d("fillGrid %d", System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        mGridCalculator.fillEmptySectors();
        Timber.d("fillEmptySectors %d", System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        float[] calculatedPositions = mGridCalculator.convertGridToPoints();
        Timber.d("convertGridToPoints %d", System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        mVertexData.put(calculatedPositions);
        mVertexData.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, 2, GL_FLOAT, false, 0, mVertexData);
        GLES20.glVertexAttribPointer(aTexCoord, 2, GL_FLOAT, false, 0, mTextureData);
        GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mUniformMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, calculatedPositions.length / 2);
        Timber.d("glDrawArrays %d", System.currentTimeMillis() - startTime);

    }


}
