package com.mycardboarddreams.liquidsurface;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.google.fpl.liquidfunpaint.physics.WorldLock;
import com.google.fpl.liquidfunpaint.physics.actions.ParticleEraser;
import com.google.fpl.liquidfunpaint.physics.actions.ParticleGroup;
import com.google.fpl.liquidfunpaint.physics.actions.SolidShape;
import com.google.fpl.liquidfunpaint.renderer.PhysicsLoop;

public class LiquidSurfaceView extends GLSurfaceView implements ILiquidWorld {

    private PhysicsLoop physicsLoop;
    private WorldLock worldLock;
    private RotatableController rotatableController;

    public LiquidSurfaceView(Context context) {
        super(context);
        initialize(context);
    }

    public LiquidSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    protected void initialize(Context context) {

        if (isInEditMode())
            return;

        physicsLoop = PhysicsLoop.getInstance();
        physicsLoop.init(context);
        worldLock = WorldLock.getInstance();

        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        if (BuildConfig.DEBUG) {
//            setDebugFlags(GLSurfaceView.DEBUG_LOG_GL_CALLS | GLSurfaceView.DEBUG_CHECK_GL_ERROR);
        }

        setRenderer(physicsLoop);

        rotatableController = new RotatableController((Activity) context);
    }

    @Override
    public void resumePhysics() {
        rotatableController.updateDownDirection((Activity) getContext());
        physicsLoop.startSimulation();
        rotatableController.onResume();
    }

    @Override
    public void createSolidShape(SolidShape solidShape) {
        worldLock.addPhysicsCommand(solidShape);
    }

    @Override
    public void eraseParticles(ParticleEraser eraserShape) {
        worldLock.addPhysicsCommand(eraserShape);
    }

    @Override
    public void createParticles(ParticleGroup liquidShape) {
        worldLock.addPhysicsCommand(liquidShape);
    }

    @Override
    public void pausePhysics() {
        physicsLoop.pauseSimulation();
        rotatableController.onPause();
    }

    @Override
    public void clearAll() {
        worldLock.clearPhysicsCommands();
        physicsLoop.reset();
    }
}
