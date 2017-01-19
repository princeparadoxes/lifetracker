package com.princeparadoxes.watertracker.openGL;

import android.opengl.Matrix;
import android.support.annotation.NonNull;

import org.jbox2d.common.Vec2;
import org.joml.Matrix4f;

public class Sprite {
    public static final float ONE_RADIAN = (float) (180.0 / Math.PI);

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private Texture mDrawTexture;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CONSTRUCTORS  ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public Sprite(Texture texture) {
        mDrawTexture = texture;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  DRAW  ///////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void draw(Vec2 position, float rotation, float scale, Matrix4f view) {
        draw(position, rotation, new Vec2(scale, scale), view);
    }

    public void draw(Vec2 position, float rotation, Vec2 scale, Matrix4f view) {
        mDrawTexture.bindTexture(0);
        Square.getInstance().draw(transformMatrix(position, rotation, scale, view));
    }

    @NonNull
    private Matrix4f transformMatrix(Vec2 position, float rotation, Vec2 scale, Matrix4f view) {
        Matrix4f newSprite = new Matrix4f();
        newSprite.mul(view);
        translateMatrix(newSprite, position);
        scaleMatrix(newSprite, scale);
        rotateMatrix(newSprite, rotation);
        return newSprite;
    }

    /**
     * slowly
     *
     * @see #translateMatrix(Matrix4f, Vec2)
     */
    private float[] transformMatrixGL(Vec2 position, float rotation, Vec2 scale, Matrix4f view) {
        float[] newMatrix = new float[16];
        Matrix.multiplyMM(newMatrix, 0, createIdentityFloatArray(), 0, mapToFloatArray(view), 0);
        Matrix.translateM(newMatrix, 0, position.x, position.y, 0.0f);
        float x = (float) mDrawTexture.getWidth() * scale.x;
        float y = (float) mDrawTexture.getHeight() * scale.y;
        Matrix.scaleM(newMatrix, 0, x, y, 1.0f);
        Matrix.rotateM(newMatrix, 0, rotation * ONE_RADIAN, 0.0f, 0.0f, -1.0f);
        return newMatrix;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CALCULATE TRANSFORMATION MATRIX  ////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private void translateMatrix(Matrix4f newSprite, Vec2 position) {
        newSprite.translate(position.x, position.y, 0.0f);
    }

    private void rotateMatrix(Matrix4f newSprite, float rotation) {
        newSprite.rotate(rotation * ONE_RADIAN, 0.0f, 0.0f, -1.0f);
    }

    private void scaleMatrix(Matrix4f newSprite, Vec2 scale) {
        float x = (float) mDrawTexture.getWidth() * scale.x;
        float y = (float) mDrawTexture.getHeight() * scale.y;
        newSprite.scale(x, y, 1.0f);
    }

    private float[] mapToFloatArray(Matrix4f matrix4f) {
        float[] floats = new float[16];
        floats[0] = matrix4f.m00;
        floats[1] = matrix4f.m01;
        floats[2] = matrix4f.m02;
        floats[3] = matrix4f.m03;
        floats[4] = matrix4f.m10;
        floats[5] = matrix4f.m11;
        floats[6] = matrix4f.m12;
        floats[7] = matrix4f.m13;
        floats[8] = matrix4f.m20;
        floats[9] = matrix4f.m21;
        floats[10] = matrix4f.m22;
        floats[11] = matrix4f.m23;
        floats[12] = matrix4f.m30;
        floats[13] = matrix4f.m31;
        floats[14] = matrix4f.m32;
        floats[15] = matrix4f.m33;
        return floats;
    }

    private float[] createIdentityFloatArray() {
        float[] floats = new float[16];
        floats[0] = 1;
        floats[1] = 0;
        floats[2] = 0;
        floats[3] = 0;
        floats[4] = 0;
        floats[5] = 1;
        floats[6] = 0;
        floats[7] = 0;
        floats[8] = 0;
        floats[9] = 0;
        floats[10] = 1;
        floats[11] = 0;
        floats[12] = 0;
        floats[13] = 0;
        floats[14] = 0;
        floats[15] = 1;
        return floats;
    }
}
