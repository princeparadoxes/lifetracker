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
}
