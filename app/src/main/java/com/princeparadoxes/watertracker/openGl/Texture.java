package com.princeparadoxes.watertracker.openGL;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class Texture {

    private int mTextureID[] = {0};
    private int mWidth, mHeight;

    public Texture(Resources resources, int resourceId) {
        loadTexture(resources, resourceId);
    }

    public void destroy() {
        GLES20.glDeleteTextures(1, mTextureID, 0);
    }


    public int getTextureID() {
        return mTextureID[0];
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void bindTexture(int id) {
        GLES20.glActiveTexture(id);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID[0]);
    }

    private void loadTexture(Resources resources, final int resourceId) {
        GLES20.glGenTextures(1, mTextureID, 0);

        try {
            if (mTextureID[0] != 0) {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;   // No pre-scaling

                // Read in the resource
                final Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId, options);

                mWidth = bitmap.getWidth();
                mHeight = bitmap.getHeight();

                // Bind to the texture in OpenGL
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID[0]);
//                GLES20.glGenerateMipmap(mTextureID[0]);
                // Set filtering
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

                // Load the bitmap into the bound texture.
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

                // Recycle the bitmap, since its data has been loaded into OpenGL.
                bitmap.recycle();
            }

            if (mTextureID[0] == 0) {
                throw new RuntimeException("Error loading texture.");
            }
        } catch (Exception e) {
            GLES20.glDeleteTextures(1, mTextureID, 0);
            mTextureID[0] = 0;
            throw e;
        }
    }
}
