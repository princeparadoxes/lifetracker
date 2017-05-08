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
    private int[][] mGrid;

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
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FILL GRID  //////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public GridCalculator fillGrid(Vec2[] positions) {
        mGrid = new int[mGridHeight][mGridWidth];
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
                    if ((isFillLeft(i, j) && isFillRight(i, j)) || (isFillTop(i, j) && isFillBottom(i, j))) {
                        mGrid[i][j]++;
                        countFilled++;
                    }
                }
            }
        } while (countFilled != 0);
        return this;
    }

    private boolean isFillLeft(int y, int x) {
        for (int k = 1; k <= mMultiplier; k++) {
            if (x - k >= 0 && mGrid[y][x - k] != 0) {
                return true;
            }
        }
        return false;
    }

    private boolean isFillTop(int y, int x) {
        for (int k = 1; k <= mMultiplier; k++) {
            if (y + k < mGridHeight - 1 && mGrid[y + k][x] != 0) {
                return true;
            }
        }
        return false;
    }

    private boolean isFillRight(int y, int x) {
        for (int k = 1; k <= mMultiplier; k++) {
            if (x + k < mGridWidth - 1 && mGrid[y][x + k] != 0) {
                return true;
            }
        }
        return false;
    }

    private boolean isFillBottom(int y, int x) {
        for (int k = 1; k <= mMultiplier; k++) {
            if (y - k >= 0 && mGrid[y - k][x] != 0) {
                return true;
            }
        }
        return false;
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
