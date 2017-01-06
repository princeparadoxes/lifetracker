package com.princeparadoxes.watertracker.misc;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.princeparadoxes.watertracker.ui.screen.main.water.WaterWorld;

/**
 * Created by mfaella on 28/02/16.
 */
public class AccelerometerListener implements SensorEventListener {

    private final WaterWorld gw;

    public AccelerometerListener(WaterWorld gw) {
        this.gw = gw;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0], y = event.values[1], z = event.values[2];
        gw.setGravity(-x, y);
        // Log.i("AccelerometerListener", "new gravity x= " + -x + "\t y= " + y);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // NOP
    }
}
