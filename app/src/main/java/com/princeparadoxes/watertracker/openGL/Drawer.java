package com.princeparadoxes.watertracker.openGL;

import org.jbox2d.common.Vec2;

public interface Drawer {

    void onSurfaceChanged(int particleCount, int width, int height, float virtualWidth, float virtualHeight);

    void draw(Vec2[] positions);
}
