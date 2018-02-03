package com.mycardboarddreams.liquidsurface;

import com.google.fpl.liquidfunpaint.physics.actions.ParticleEraser;
import com.google.fpl.liquidfunpaint.physics.actions.ParticleGroup;
import com.google.fpl.liquidfunpaint.physics.actions.SolidShape;

/**
 * Created on 15-09-19.
 */
public interface ILiquidWorld {

    void pausePhysics();

    void resumePhysics();

    void createSolidShape(SolidShape solidShape);

    void eraseParticles(ParticleEraser eraserShape);

    void createParticles(ParticleGroup liquidShape);

    void clearAll();

}
