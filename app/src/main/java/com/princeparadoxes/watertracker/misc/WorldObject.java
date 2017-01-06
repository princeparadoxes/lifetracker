package com.princeparadoxes.watertracker.misc;

import android.graphics.Bitmap;

import com.google.fpl.liquidfun.Body;
import com.princeparadoxes.watertracker.ui.screen.main.water.WaterWorld;

/**
 * Created by mfaella on 27/02/16.
 */
public abstract class WorldObject {
    protected Body body;
    protected String name;
    protected WaterWorld gw;

    public WorldObject(WaterWorld gw)
    {
        this.gw = gw;
    }
    public boolean draw(Bitmap buffer)
    {
        if (body!=null) {
            float x = body.getPositionX(),
                  y = body.getPositionY(),
                  angle = body.getAngle();
            // Log.d("GameObject", "x=" + x + "\t y=" + y);
            if (x > gw.getPhysicalSize().xmin && x < gw.getPhysicalSize().xmax &&
                y > gw.getPhysicalSize().ymin && y < gw.getPhysicalSize().ymax) {
                float screen_x = gw.toPixelsX(x),
                        screen_y = gw.toPixelsY(y);
                this.draw(buffer, screen_x, screen_y, angle);
                return true;
            } else
                return false;
        } else {
            this.draw(buffer, 0, 0, 0);
            return true;
        }
    }

    public abstract void draw(Bitmap buf, float x, float y, float angle);
}
