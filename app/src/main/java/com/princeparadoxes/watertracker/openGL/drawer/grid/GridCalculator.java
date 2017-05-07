package com.princeparadoxes.watertracker.openGL.drawer.grid;

import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.List;

public class GridCalculator {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private final float mMultiplier;
    private final int mGridWidth;
    private final int mGridHeight;
    private final float mHalfParticleSize;
    private final int[][] mGrid;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CONSTRUCTOR  ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public GridCalculator(float multiplier,
                          float virtualWidth,
                          float virtualHeight,
                          float particleSize) {
        mMultiplier = multiplier;
        mGridWidth = (int) Math.ceil(virtualWidth * multiplier);
        mGridHeight = (int) Math.ceil(virtualHeight * multiplier);
        mHalfParticleSize = particleSize / 2;
        mGrid = new int[mGridHeight][mGridWidth];
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FILL GRID  //////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public GridCalculator fillGrid(Vec2[] positions) {
        for (Vec2 vec : positions) {
            int x = (int) (vec.x * mMultiplier);
            x = x > (mGridWidth - 1) ? (int) Math.floor(x) : Math.round(x);
            int y = (int) (vec.y * mMultiplier);
            y = y > (mGridHeight - 1) ? (int) Math.floor(y) : Math.round(y);
            mGrid[y][x]++;
        }
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FILL EMPTY SECTORS  /////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public GridCalculator fillEmptySectors() {
        int countFilled;
        do {
            countFilled = 0;
            for (int i = 0; i < mGrid.length; i++) {
                for (int j = 0; j < mGrid[i].length; j++) {
                    if (mGrid[i][j] != 0) continue;
                    boolean bottom = false, top = false, left = false, right = false;
                    int filled = 0;
                    for (int k = 1; k <= mMultiplier; k++) {
                        if (i - k >= 0 && mGrid[i - k][j] != 0) {
                            bottom = true;
                            filled++;
                            break;
                        }
                    }
                    for (int k = 1; k <= mMultiplier; k++) {
                        if (i + k < mGridHeight - 1 && mGrid[i + k][j] != 0) {
                            top = true;
                            filled++;
                            break;
                        }
                    }
                    for (int k = 1; k <= mMultiplier; k++) {
                        if (j - k > 0 && mGrid[i][j - k] != 0) {
                            left = true;
                            filled++;
                            break;
                        }
                    }
                    for (int k = 1; k <= mMultiplier; k++) {
                        if (j + k < mGridWidth - 1 && mGrid[i][j + k] != 0) {
                            right = true;
                            filled++;
                            break;
                        }
                    }
                    if (filled > 2 || (left && right) || (top && bottom)) {
                        mGrid[i][j]++;
                        countFilled++;
                    }
                }
            }
        } while (countFilled != 0);
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CONVERT GRID TO POINTS  /////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public float[] convertGridToPoints() {
        List<Float> floats = new ArrayList<>();
        for (int i = 0; i < mGrid.length; i++) {
            for (int j = 0; j < mGrid[i].length; j++) {
                if (mGrid[i][j] == 0) continue;
                float x = (float) (Math.floor(j) / mMultiplier);
                float y = (float) (Math.floor(i) / mMultiplier);
                x += mHalfParticleSize / mMultiplier;
                y += mHalfParticleSize / mMultiplier;
                floats.add(x - mHalfParticleSize);
                floats.add(y - mHalfParticleSize);
                floats.add(x - mHalfParticleSize);
                floats.add(y + mHalfParticleSize);
                floats.add(x + mHalfParticleSize);
                floats.add(y + mHalfParticleSize);
                floats.add(x - mHalfParticleSize);
                floats.add(y - mHalfParticleSize);
                floats.add(x + mHalfParticleSize);
                floats.add(y - mHalfParticleSize);
                floats.add(x + mHalfParticleSize);
                floats.add(y + mHalfParticleSize);
            }
        }
        float[] calculatedPoints = new float[floats.size()];
        for (int i = 0; i < floats.size(); i++) {
            calculatedPoints[i] = floats.get(i);
        }
        return calculatedPoints;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  GETTERS  ////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public int[][] getGrid() {
        return mGrid;
    }
}
