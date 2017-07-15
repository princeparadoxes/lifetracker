package com.princeparadoxes.watertracker.ui.screen.main.water;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.opengl.GLES20;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.ParticleDef;
import com.google.fpl.liquidfun.ParticleFlag;
import com.google.fpl.liquidfun.ParticleSystem;
import com.google.fpl.liquidfun.ParticleSystemDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;
import com.princeparadoxes.watertracker.R;
import com.princeparadoxes.watertracker.misc.AccelerometerListener;
import com.princeparadoxes.watertracker.openGL.Texture;
import com.princeparadoxes.watertracker.openGL.drawer.Drawer;
import com.princeparadoxes.watertracker.openGL.drawer.particle.ParticleDrawer;

import java.util.Arrays;

import timber.log.Timber;

public class WaterWorld {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CONSTANTS  //////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private static final float BASE_UNITS = 20f;
    private static final int MAX_PARTICLE_COUNT = 10000;
    private static final float PARTICLE_RADIUS = 1f;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private final Resources mResources;

    private World mWorld;
    private ParticleSystem mParticleSystem;
    private Vec2[] mParticlePositions = new Vec2[MAX_PARTICLE_COUNT];
    private Drawer mDrawer;

    private float mVirtualWidth;
    private float mVirtualHeight;
    private boolean isObjectCreated = false;

    private float mLastAccelerometerGravityX = 0.0f;
    private float mLastAccelerometerGravityY = -9.81f;
    private boolean mIsAccelerometerGravityLock = false;

    private enum ObjectType {
        WATER_PARTICLES,
        BORDERS,
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CONSTRUCTORS  ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public WaterWorld(Context context) {
        mResources = context.getResources();
        createWorld();
        registerAccelerometerListener(context);
    }

    private void createWorld() {
        mWorld = new World(mLastAccelerometerGravityX, mLastAccelerometerGravityY);
        ParticleSystemDef def = new ParticleSystemDef();
        def.setRadius(PARTICLE_RADIUS);
        def.setMaxCount(MAX_PARTICLE_COUNT);
        mParticleSystem = mWorld.createParticleSystem(def);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  ACCELEROMETER  //////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private void registerAccelerometerListener(Context context) {
        SensorManager service = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (service.getSensorList(Sensor.TYPE_ACCELEROMETER).isEmpty()) return;
        Sensor sensor = service.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        service.registerListener(getAccelerometerListener(), sensor, SensorManager.SENSOR_DELAY_UI);
    }

    private AccelerometerListener getAccelerometerListener() {
        return new AccelerometerListener((x, y) -> {
            if (mIsAccelerometerGravityLock) return;
            mLastAccelerometerGravityX = -x;
            mLastAccelerometerGravityY = -y;
            mWorld.setGravity(-x, -y);
        });
    }

    public void restoreLastAccelerometerGravity() {
        mIsAccelerometerGravityLock = false;
        mWorld.setGravity(mLastAccelerometerGravityX, mLastAccelerometerGravityY);
    }

    public void setGravityWithLock(float gravityX, float gravityY) {
        mIsAccelerometerGravityLock = true;
        mWorld.setGravity(gravityX, gravityY);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  INIT  ///////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void init() {
        enable2DTextures();
        createSprites();
    }

    private void enable2DTextures() {
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void createSprites() {
        Texture waterTexture = new Texture(mResources, R.drawable.par_3x);
        mDrawer = new ParticleDrawer();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  SET SIZE  ///////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void setSize(int width, int height) {
        final float aspectRatio = ((float) height) / ((float) width);
        mVirtualWidth = BASE_UNITS;
        mVirtualHeight = mVirtualWidth * aspectRatio;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  ON SURFACE CHANGED  /////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void onSurfaceChanged(int width, int height) {
        mDrawer.onSurfaceChanged(MAX_PARTICLE_COUNT, width, height, mVirtualWidth, mVirtualHeight);
        createObjects();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  WATER ///////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void addWater(int ml, int dayNorm) {
        float aspect = (float) ml / dayNorm;
        int count = (int) ((mVirtualHeight * mVirtualWidth) * aspect);
        count /= PARTICLE_RADIUS;
        createWater(count);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CREATE OBJECTS  /////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private void createObjects() {
        if (isObjectCreated) return;
        createBorders();
        for (int i = 0; i < mParticlePositions.length; i++) {
            mParticlePositions[i] = new Vec2(-1, -1);
        }
        isObjectCreated = true;
    }

    private void createWater(int count) {

        float offset = PARTICLE_RADIUS / 2;
        for (int i = 0; i < count; i++) {
            ParticleDef particleDef = new ParticleDef();
            float x = (i + offset) % mVirtualWidth;
            float y = ((int) (x / mVirtualWidth)) + offset;
            particleDef.setPosition(x, y);
            particleDef.setFlags(ParticleFlag.waterParticle);

            mParticleSystem.createParticle(particleDef);
        }
    }

    private void createBorders() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.setUserData(ObjectType.BORDERS);
        bodyDef.setType(BodyType.staticBody);
        Body body = mWorld.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(mVirtualWidth + 2 * 0.05f, 0.05f, mVirtualWidth / 2, 0, 0);
        createBorderFixture(body, shape);
        shape.setAsBox(mVirtualWidth + 2 * 0.05f, 0.05f, mVirtualWidth / 2, mVirtualHeight, 0);
        createBorderFixture(body, shape);
        shape.setAsBox(0.05f, mVirtualHeight, 0, mVirtualHeight / 2, 0);
        createBorderFixture(body, shape);
        shape.setAsBox(0.05f, mVirtualHeight, mVirtualWidth, mVirtualHeight / 2, 0);
        createBorderFixture(body, shape);
    }

    private void createBorderFixture(Body body, PolygonShape shape) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(shape);
//        fixtureDef.userData = null;
        fixtureDef.setFriction(0.5f);
        fixtureDef.setRestitution(0.05f);
        fixtureDef.setDensity(1.0f);
//        fixtureDef.isSensor = false;
        body.createFixture(fixtureDef);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  UPDATE  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void update(float delta) {
        mWorld.step(1f / 8f, 3, 3, 3);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  DRAW  ///////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void onDraw() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        long startTime = System.currentTimeMillis();
        drawParticles();
        Timber.d("Draw time %d ms", System.currentTimeMillis() - startTime);
    }

//    private void drawBodies() {
//        int bodyCount = mWorld.getBodyCount();
//        if (bodyCount <= 0) return;
//        Body body = mWorld.getBodyList();
//        for (int i = 0; i < bodyCount; i++) {
//                switch ((ObjectType) body.getUserData()) {
//                }
//            body = body.getNext();
//        }
//    }

    private void drawParticles() {
        long startTime = System.currentTimeMillis();
        int particleCount = mParticleSystem.getParticleCount();
        for (int i = 0; i < particleCount; i++) {
            mParticlePositions[i].setX(mParticleSystem.getParticlePositionX(i));
            mParticlePositions[i].setY(mParticleSystem.getParticlePositionY(i));
        }
        Vec2[] vec2s = Arrays.copyOf(mParticlePositions, particleCount);
        Timber.d("create array %d ms", System.currentTimeMillis() - startTime);
        mDrawer.draw(vec2s);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  DELEGATES  //////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////


}
