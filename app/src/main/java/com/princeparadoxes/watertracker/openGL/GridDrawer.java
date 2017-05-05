package com.princeparadoxes.watertracker.openGL;

import android.opengl.GLES20;

import org.jbox2d.common.Vec2;
import org.joml.Vector4f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

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

    public static Vector4f color = new Vector4f();
    private static float squareCoords[] = {
            -0.5f, -0.5f,     // bottom left
            0.5f, -0.5f,      // bottom right
            0.5f, 0.5f,      // top right
            -0.5f, 0.5f};    // top left

    private static final int COORDS_PER_VERTEX = 2;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
    private static final int VERTEX_COUNT = 4;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private FloatBuffer mVertexBuffer;
    private ShortBuffer mDrawListBuffer;
    private float mHeight;
    private float mWidth;
    private float mAspectRatio;
    private float mGridSize;

    private short drawOrder[] = {0, 1, 2, 0, 2, 3}; // order to onDraw vertices

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

    public static final float PARTICLE_SIZE = 1;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CONSTRUCTORS  ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public GridDrawer() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4); // (# of coordinate values * 4 bytes per float)
        bb.order(ByteOrder.nativeOrder());
        mVertexBuffer = bb.asFloatBuffer();
        mVertexBuffer.put(squareCoords);
        mVertexBuffer.position(0);

        // initialize byte buffer for the onDraw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2); // (# of coordinate values * 2 bytes per short)
        dlb.order(ByteOrder.nativeOrder());
        mDrawListBuffer = dlb.asShortBuffer();
        mDrawListBuffer.put(drawOrder);
        mDrawListBuffer.position(0);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // creates OpenGL ES program executables
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(mProgram, GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            Timber.e("Program not linked");
        }

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
        int bufferSize = width * POINTS_ON_PARTICLE * FLOAT_BYTES;
        mWidth = virtualWidth;
        mHeight = virtualHeight;
        mGridSize = width / virtualWidth;
        mVertexData = ByteBuffer.allocateDirect(bufferSize)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mTextureData = ByteBuffer.allocateDirect(bufferSize)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        mAspectRatio = (float) (virtualWidth / Math.ceil(virtualHeight));
        mUniformMatrix = new float[]{
                .1f, 0, 0, -1,
                0, .1f * mAspectRatio, 0, -1,
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
        GLES20.glUseProgram(mProgram);

        float[] calculatedPositions = convertGridToPoints(fillGrid(positions, mWidth, mHeight));
        mVertexData.put(calculatedPositions);
        mVertexData.position(0);

        GLES20.glVertexAttribPointer(mPositionHandle, 2, GL_FLOAT, false, 0, mVertexData);
        GLES20.glVertexAttribPointer(aTexCoord, 2, GL_FLOAT, false, 0, mTextureData);
        GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mUniformMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, calculatedPositions.length / 2);
    }

    private int[] fillGrid(Vec2[] positions, float width, float height) {
        int[] grid = new int[(int) (width * (Math.ceil(height)))];

        for (Vec2 vec : positions) {
            int x = vec.x > (width - 1) ? (int) Math.floor(vec.x) : Math.round(vec.x);
            int y = vec.y > (height - 1) ? (int) Math.floor(vec.y) : Math.round(vec.y);
            grid[(int) (y * width + x)] += 1;
        }

        return grid;
    }

    private float[] convertGridToPoints(int[] grid) {
        List<Float> floats = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            int pixel = grid[i];
            if (pixel == 0) continue;
            int x = (int) Math.floor(i % mWidth);
            int y = (int) Math.floor(i / mWidth);
            Vec2 pos = new Vec2(x + 0.5f, y + 0.5f);
            floats.add(pos.x - PARTICLE_SIZE / 2);
            floats.add(pos.y - PARTICLE_SIZE / 2);
            floats.add(pos.x - PARTICLE_SIZE / 2);
            floats.add(pos.y + PARTICLE_SIZE / 2);
            floats.add(pos.x + PARTICLE_SIZE / 2);
            floats.add(pos.y + PARTICLE_SIZE / 2);
            floats.add(pos.x - PARTICLE_SIZE / 2);
            floats.add(pos.y - PARTICLE_SIZE / 2);
            floats.add(pos.x + PARTICLE_SIZE / 2);
            floats.add(pos.y - PARTICLE_SIZE / 2);
            floats.add(pos.x + PARTICLE_SIZE / 2);
            floats.add(pos.y + PARTICLE_SIZE / 2);
        }
        float[] calculatedPoints = new float[floats.size()];
        for (int i = 0; i < floats.size(); i++) {
            calculatedPoints[i] = floats.get(i);
        }
        return calculatedPoints;
    }

    private float[] calculateAdditionalPoints(Vec2[] positions) {
        float[] calculatedPoints = new float[positions.length * POINTS_ON_PARTICLE];
        for (int i = 0; i < positions.length; i++) {
            calculatedPoints[POINTS_ON_PARTICLE * i + 0] = positions[i].x - PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 1] = positions[i].y - PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 2] = positions[i].x - PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 3] = positions[i].y + PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 4] = positions[i].x + PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 5] = positions[i].y + PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 6] = positions[i].x - PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 7] = positions[i].y - PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 8] = positions[i].x + PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 9] = positions[i].y - PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 10] = positions[i].x + PARTICLE_SIZE / 2;
            calculatedPoints[POINTS_ON_PARTICLE * i + 11] = positions[i].y + PARTICLE_SIZE / 2;
        }
        return calculatedPoints;
    }
}
