package com.mycardboarddreams.liquidsurface;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Surface;

import com.google.fpl.liquidfunpaint.physics.WorldLock;

public class RotatableController implements SensorEventListener {

    private SensorManager mManager;
    private Sensor mAccelerometer;
    private final float[] mGravityVec = new float[2];

    private static final float GRAVITY = 10f;

    public RotatableController(Activity activity) {
        // Get rotation and set the vector
        updateDownDirection(activity);

        mManager = (SensorManager) activity.getSystemService(Activity.SENSOR_SERVICE);
        mAccelerometer = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void updateDownDirection(Activity activity) {
        mGravityVec[0] = 0;
        mGravityVec[1] = 0;

        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
                mGravityVec[0] = -GRAVITY;
                break;
            case Surface.ROTATION_90:
                mGravityVec[1] = -GRAVITY;
                break;
            case Surface.ROTATION_180:
                mGravityVec[0] = GRAVITY;
                break;
            case Surface.ROTATION_270:
                mGravityVec[1] = GRAVITY;
                break;
        }
    }

    public void onResume() {
        mManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void onPause() {
        mManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];

            float gravityX = mGravityVec[0] * x - mGravityVec[1] * y;
            float gravityY = mGravityVec[1] * x + mGravityVec[0] * y;
            WorldLock.getInstance().setGravity(gravityX, gravityY, true);
        }
    }

}
