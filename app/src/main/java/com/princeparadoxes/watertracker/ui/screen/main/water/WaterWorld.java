package com.princeparadoxes.watertracker.ui.screen.main.water;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;

import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.ParticleSystem;
import com.google.fpl.liquidfun.ParticleSystemDef;
import com.google.fpl.liquidfun.World;
import com.princeparadoxes.watertracker.misc.Box;
import com.princeparadoxes.watertracker.misc.WorldObject;
import com.princeparadoxes.watertracker.misc.MyContactListener;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * The game objects and the viewport.
 * <p>
 * Created by mfaella on 27/02/16.
 */
public class WaterWorld {
    // Rendering
    private final static int bufferWidth = 400, bufferHeight = 600;    // actual pixels
    private Bitmap buffer;
    private Canvas canvas;
    private Paint particlePaint;
    private final boolean isLittleEndian;

    // Simulation
    private List<WorldObject> objects;
    private World world;
    private final Box physicalSize, screenSize;
    private ContactListener contactListener; // kept to prevent GC
    private final int bufferOffset; // an architecture-dependent parameter

    // Particles
    ParticleSystem particleSystem;
    private byte[] particlePositions;
    private ByteBuffer particlePositionsBuffer;
    private static final int BYTESPERPARTICLE = 8;
    private static final int MAXPARTICLECOUNT = 1000;
    private static final float PARTICLE_RADIUS = 0.2f;

    // Parameters for world simulation
    private static final float TIME_STEP = 1 / 24f; // 60 fps
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;

    // Arguments are in physical simulation units.
    public WaterWorld(Box physicalSize, Box screenSize) {
        this.physicalSize = physicalSize;
        this.screenSize = screenSize;

        this.buffer = Bitmap.createBitmap(bufferWidth, bufferHeight, Bitmap.Config.ARGB_8888);
        this.world = new World(0, 0);  // gravity vector

        // The particle system
        ParticleSystemDef psysdef = new ParticleSystemDef();
        this.particleSystem = world.createParticleSystem(psysdef);
        particleSystem.setRadius(PARTICLE_RADIUS);
        particleSystem.setMaxParticleCount(MAXPARTICLECOUNT);
        psysdef.delete();
        particlePositionsBuffer = ByteBuffer.allocateDirect(MAXPARTICLECOUNT * BYTESPERPARTICLE);
        particlePositions = particlePositionsBuffer.array();

        particlePaint = new Paint();
        particlePaint.setARGB(255, 0, 0, 255);
        particlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        // stored to prevent GC
        contactListener = new MyContactListener();
        world.setContactListener(contactListener);

        this.objects = new ArrayList<>();
        this.canvas = new Canvas(buffer);

        isLittleEndian = (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN);
        // An ugly trick, can we do better?
        Log.d("DEBUG", "Build.FINGERPRINT=" + Build.FINGERPRINT);
        Log.d("DEBUG", "Build.PRODUCT=" + Build.PRODUCT);
        if (Build.FINGERPRINT.contains("generic") ||
                Build.FINGERPRINT.contains("unknown") ||
                Build.PRODUCT.contains("sdk"))
            bufferOffset = 0; // emulator
        else
            bufferOffset = 4; // real device
    }

    public synchronized WorldObject addGameObject(WorldObject obj) {
        objects.add(obj);
        return obj;
    }

    public synchronized void addParticleGroup(WorldObject obj) {
        objects.add(obj);
    }

    private void drawParticles() {
        final int particleCount = particleSystem.getParticleCount();

        // Log.d("GameWorld", "about to draw " + particleCount + " particles");

        particleSystem.copyPositionBuffer(0, particleCount, particlePositionsBuffer);

        for (int i = 0; i < particleCount; i++) {
            int xint, yint;
            if (isLittleEndian) {
                xint = (particlePositions[i * 8 + bufferOffset] & 0xFF) | (particlePositions[i * 8 + bufferOffset + 1] & 0xFF) << 8 |
                        (particlePositions[i * 8 + bufferOffset + 2] & 0xFF) << 16 | (particlePositions[i * 8 + bufferOffset + 3] & 0xFF) << 24;
                yint = (particlePositions[i * 8 + bufferOffset + 4] & 0xFF) | (particlePositions[i * 8 + bufferOffset + 5] & 0xFF) << 8 |
                        (particlePositions[i * 8 + bufferOffset + 6] & 0xFF) << 16 | (particlePositions[i * 8 + bufferOffset + 7] & 0xFF) << 24;
            } else {
                xint = (particlePositions[i * 8] & 0xFF) << 24 | (particlePositions[i * 8 + 1] & 0xFF) << 16 |
                        (particlePositions[i * 8 + 2] & 0xFF) << 8 | (particlePositions[i * 8 + 3] & 0xFF);
                yint = (particlePositions[i * 8 + 4] & 0xFF) << 24 | (particlePositions[i * 8 + 5] & 0xFF) << 16 |
                        (particlePositions[i * 8 + 6] & 0xFF) << 8 | (particlePositions[i * 8 + 7] & 0xFF);
            }

            float x = Float.intBitsToFloat(xint), y = Float.intBitsToFloat(yint);
            canvas.drawCircle(toPixelsX(x), toPixelsY(y), 6, particlePaint);
        }
    }

    public synchronized void update() {
        // advance the physics simulation
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
        // clear the screen (with white)
        canvas.drawARGB(255, 255, 255, 255);
        for (WorldObject obj : objects)
            obj.draw(buffer);
        drawParticles();
        // handle touch events
    }

    public float toPixelsX(float x) {
        return (x - physicalSize.xmin) / physicalSize.width * bufferWidth;
    }

    public float toPixelsY(float y) {
        return (y - physicalSize.ymin) / physicalSize.height * bufferHeight;
    }

    public float toPixelsXLength(float x) {
        return x / physicalSize.width * bufferWidth;
    }

    public float toPixelsYLength(float y) {
        return y / physicalSize.height * bufferHeight;
    }

    public synchronized void setGravity(float x, float y) {
        world.setGravity(x, y);
    }

    public Box getScreenSize() {
        return screenSize;
    }

    public Box getPhysicalSize() {
        return physicalSize;
    }

    public Bitmap getBuffer() {
        return buffer;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void finalize() {
        world.delete();
    }

}
