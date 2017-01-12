package com.princeparadoxes.watertracker.ui.screen.main.water;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.util.Calendar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class WaterRenderer implements GLSurfaceView.Renderer {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private final WaterWorld mWaterWorld;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CONSTRUCTORS  ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public WaterRenderer(Context context) {
        mWaterWorld = new WaterWorld(context);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  RENDERER  ///////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1f, 1f, 1f, 1f);
        mWaterWorld.init();
    }

//    float Accumulator = 0.0f;
//    long lastTicks = -1;

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        mWaterWorld.setSize(width, height);
        mWaterWorld.createObjects();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        mWaterWorld.update(1.0f / 24.0f);
        mWaterWorld.draw();
//        if(lastTicks == -1) lastTicks = Calendar.getInstance().getTime().getTime();
//        final float min_timestep = 1.0f / 100.0f;
//        long nowticks = Calendar.getInstance().getTime().getTime();
//        Accumulator += (float) (nowticks - lastTicks) / 1000.0f;
//        lastTicks = nowticks;
//        while (Accumulator > min_timestep) {
//            mWaterWorld.update(min_timestep);
//            Accumulator -= min_timestep;
//        }
//        mWaterWorld.draw();
    }
}
