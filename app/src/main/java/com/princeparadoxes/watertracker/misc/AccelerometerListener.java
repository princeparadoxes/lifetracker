package com.princeparadoxes.watertracker.misc;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import timber.log.Timber;

public class AccelerometerListener implements SensorEventListener {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CONSTANTS  //////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private static final float DELTA = 0.2f;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private final OnSensorChangeWithDeltaListener mListener;
    private float mTempX = 0;
    private float mTempY = 0;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CONSTRUCTORS  ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public AccelerometerListener(OnSensorChangeWithDeltaListener listener) {
        mListener = listener;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  ON SENSOR CHANGED  //////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0], y = event.values[1], z = event.values[2];
//        Timber.d("AccelerometerListener event x: %f, y %f, z %f", x, y, z);
        if (Math.abs(mTempX - x) < DELTA || Math.abs(mTempY - y) < DELTA) return;
        mTempX = x;
        mTempY = y;
//        Timber.d("AccelerometerListener set gravity x: %f, y %f, z %f", x, y, z);
        mListener.onSensorChangeWithDeltaListener(x, y);
    }

    public interface OnSensorChangeWithDeltaListener{
        void onSensorChangeWithDeltaListener(float x, float y);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  REDUNDANT  //////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
