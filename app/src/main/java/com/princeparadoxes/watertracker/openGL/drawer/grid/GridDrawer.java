package com.princeparadoxes.watertracker.openGL.drawer.grid;

import android.opengl.GLES20;

import com.princeparadoxes.watertracker.openGL.drawer.Drawer;

import org.jbox2d.common.Vec2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import timber.log.Timber;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINK_STATUS;

/**
 * A two-dimensional triangle for use as a drawn object in OpenGL ES 2.0.
 */
public class GridDrawer implements Drawer {
    public static final int FLOAT_BYTES = Float.SIZE / Byte.SIZE;
    public static final int POINTS_ON_PARTICLE = 12;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CONSTANTS  //////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private static final int COORDS_PER_VERTEX = 2;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
    private static final int VERTEX_COUNT = 4;
    private static final float PARTICLE_SIZE = 1f;
    private static final float HALF_PARTICLE_SIZE = PARTICLE_SIZE / 2f;
    private static final float MULTIPLIER = 2f;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private float mAspectRatio;
    private GridCalculator mGridCalculator;

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

    public GridDrawer() {

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // creates OpenGL ES program executables
        checkLinkStatus();
        initAttributes();
    }

    private void checkLinkStatus() {
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(mProgram, GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            Timber.e("Program not linked");
        }
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

    private final String vertexShaderCode =
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

    private final String fragmentShaderCode =
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

    @Override
    public void onSurfaceChanged(int particleCount, int width, int height, float virtualWidth, float virtualHeight) {
        mGridCalculator = new GridCalculator(MULTIPLIER, virtualWidth, virtualHeight, PARTICLE_SIZE);
        mAspectRatio = (float) (virtualWidth / Math.ceil(virtualHeight));

        int countPoints = (int) (virtualWidth * MULTIPLIER * virtualHeight * MULTIPLIER * POINTS_ON_PARTICLE);
        int bufferSize = countPoints * FLOAT_BYTES;

        createVertexBuffer(bufferSize);
        createUniformMatrix();
        createTextureBuffer(countPoints, bufferSize);
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

        float[] calculatedPositions = mGridCalculator
                .fillGrid(positions)
                .fillEmptySectors()
                .convertGridToPoints();
        mVertexData.put(calculatedPositions);
        mVertexData.position(0);

        GLES20.glVertexAttribPointer(mPositionHandle, 2, GL_FLOAT, false, 0, mVertexData);
        GLES20.glVertexAttribPointer(aTexCoord, 2, GL_FLOAT, false, 0, mTextureData);
        GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mUniformMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, calculatedPositions.length / 2);
    }


}
