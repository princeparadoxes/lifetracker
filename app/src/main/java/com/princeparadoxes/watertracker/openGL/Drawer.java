package com.princeparadoxes.watertracker.openGL;

import org.jbox2d.common.Vec2;

public interface Drawer {

    void onSurfaceChanged(int maxParticleCount, int width, int height);

    void draw(Vec2[] positions);
}
