package com.princeparadoxes.watertracker.ui.screen.main.water;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.opengl.GLES20;

import com.princeparadoxes.watertracker.R;
import com.princeparadoxes.watertracker.misc.AccelerometerListener;
import com.princeparadoxes.watertracker.openGL.Texture;
import com.princeparadoxes.watertracker.openGL.TextureDrawer;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.particle.ParticleGroup;
import org.jbox2d.particle.ParticleGroupDef;
import org.jbox2d.particle.ParticleType;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import timber.log.Timber;

public class WaterWorld {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CONSTANTS  //////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private static final float BASE_UNITS = 20f;
    private static final int MAX_PARTICLE_COUNT = 10000;
    private static final float PARTICLE_RADIUS = 0.5f;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private final Resources mResources;

    private World mWorld;
    private Vector4f mDrawWhite = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    private TextureDrawer mWaterTextureDrawer;

    private float mVirtualWidth;
    private float mVirtualHeight;
    private boolean isObjectCreated = false;

    private Boolean mIsSlow = false;

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
        mWorld = new World(new Vec2(0.0f, -9.81f));
        mWorld.setParticleRadius(PARTICLE_RADIUS);
        mWorld.setParticleMaxCount(MAX_PARTICLE_COUNT);
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
            mWorld.setGravity(new Vec2(-x, -y));
        });
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
        Texture waterTexture = new Texture(mResources, R.drawable.ic_water_particle_8dp);
        mWaterTextureDrawer = new TextureDrawer();
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
        mWaterTextureDrawer.onSurfaceChanged(MAX_PARTICLE_COUNT, width, height);
        createObjects();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CREATE OBJECTS  /////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private void createObjects() {
        if (isObjectCreated) return;
        createBorders();
        createWater();
        isObjectCreated = true;
    }

    private void createWater() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(10f, 10f, new Vec2(10f, 10f), 0);

        ParticleGroupDef particleGroupDef = new ParticleGroupDef();
        particleGroupDef.flags = ParticleType.b2_waterParticle;
        particleGroupDef.shape = shape;

        ParticleGroup particleGroup = mWorld.createParticleGroup(particleGroupDef);
        particleGroup.setUserData(ObjectType.WATER_PARTICLES);
    }

    private void createBorders() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.userData = ObjectType.BORDERS;
        bodyDef.type = BodyType.KINEMATIC;
        Body body = mWorld.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(mVirtualWidth + 2 * 0.05f, 0.05f, new Vec2(mVirtualWidth / 2, 0), 0);
        createBorderFixture(body, shape);
        shape.setAsBox(mVirtualWidth + 2 * 0.05f, 0.05f, new Vec2(mVirtualWidth / 2, mVirtualHeight), 0);
        createBorderFixture(body, shape);
        shape.setAsBox(0.05f, mVirtualHeight, new Vec2(0, mVirtualHeight / 2), 0);
        createBorderFixture(body, shape);
        shape.setAsBox(0.05f, mVirtualHeight, new Vec2(mVirtualWidth, mVirtualHeight / 2), 0);
        createBorderFixture(body, shape);
    }

    private void createBorderFixture(Body body, PolygonShape shape) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.userData = null;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.05f;
        fixtureDef.density = 1.0f;
        fixtureDef.isSensor = false;
        body.createFixture(fixtureDef);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  UPDATE  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void update(float delta) {
        if (mIsSlow) mWorld.step(delta * 0.2f, 8, 3);
        else mWorld.step(delta, 1, 1);

        for (Body b = mWorld.getBodyList(); b != null; b = b.getNext()) {
            Vec2 position = b.getPosition();
            if (position.y < -5 || position.x < -5 || position.x > 18) mWorld.destroyBody(b);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  DRAW  ///////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void onDraw() {
        long startTime = System.currentTimeMillis();
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        drawParticles();
        Timber.d("Draw time %d", System.currentTimeMillis() - startTime);
    }

    private void drawBodies() {
        int bodyCount = mWorld.getBodyCount();
        if (bodyCount <= 0) return;
        Body body = mWorld.getBodyList();
        for (int i = 0; i < bodyCount; i++) {
            TextureDrawer.color = mDrawWhite;
//                switch ((ObjectType) body.getUserData()) {
//                }
            body = body.getNext();
        }
    }

    private void drawParticles() {
        int particleCount = mWorld.getParticleCount();
        if (particleCount <= 0) return;
        List<Vec2> list = new ArrayList<>(Arrays.asList(mWorld.getParticlePositionBuffer()));
        Iterator<Vec2> iterator;
        for (iterator = list.iterator(); iterator.hasNext();) {
            Vec2 vec2 = iterator.next();
            if(vec2.x <= 0 || vec2.y <= 0){
                iterator.remove();
            }
        }
        Vec2[] vec2s = new Vec2[list.size()];
        mWaterTextureDrawer.draw(list.toArray(vec2s));
    }
}
