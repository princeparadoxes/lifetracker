package com.google.fpl.liquidfunpaint.util;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created on 8/16/2015.
 */
public interface DrawableLayer extends GLSurfaceView.Renderer {
    void reset();

    void init(Context activity);
}
