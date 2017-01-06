package com.princeparadoxes.watertracker.ui.screen.main.water;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.google.fpl.liquidfun.ParticleDef;
import com.google.fpl.liquidfun.ParticleFlag;
import com.google.fpl.liquidfun.ParticleGroup;
import com.google.fpl.liquidfun.ParticleGroupDef;
import com.google.fpl.liquidfun.ParticleSystem;
import com.google.fpl.liquidfun.PolygonShape;
import com.princeparadoxes.watertracker.misc.WorldObject;


/**
 * A singleton.
 *
 * Created by mfaella on 27/02/16.
 */
public class WaterWorldObject extends WorldObject
{
    private static final float width = 2, height = 2;
    private static float screen_semi_width, screen_semi_height;

    private final Canvas canvas;
    private final Paint paint = new Paint();
    private final ParticleSystem psys;
    private final ParticleGroup group;

    public WaterWorldObject(WaterWorld gw, float x, float y)
    {
        super(gw);

        this.canvas = new Canvas(gw.getBuffer());
        this.psys = gw.particleSystem;

        paint.setARGB(255, 0, 255, 0);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        PolygonShape box = new PolygonShape();
        box.setAsBox(5, 5);
        ParticleGroupDef groupDef = new ParticleGroupDef();
//        groupDef.setFlags();
        groupDef.setFlags(ParticleFlag.waterParticle);
        groupDef.setShape(box);
        groupDef.setPosition(x, y);
        group = gw.particleSystem.createParticleGroup(groupDef);

        Log.d("DragMe", "Created " + group.getParticleCount() + " particles");

        // no body
        this.body = null;

        // clean up native objects
        groupDef.delete();
        box.delete();
    }


    // the last three parameters are irrelevant (actually, zero)
    @Override
    public void draw(Bitmap buffer, float _x, float _y, float _angle) {
        /* final int particleCount = psys.getParticleCount();
        for (int i=0; i<particleCount; i++) {
            float x = psys.getParticlePositionX(i), y = psys.getParticlePositionY(i);
            canvas.drawCircle(gw.toPixelsX(x), gw.toPixelsY(y), 6, paint);
        } */
    }
}
