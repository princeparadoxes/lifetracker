package com.princeparadoxes.watertracker.openGL.drawer.grid;


import com.google.fpl.liquidfun.Vec2;

import java.util.Arrays;

import timber.log.Timber;

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
            int x = (int) (vec.getX() * mMultiplier);
            x = x < 0 ? 0 : x;
            x = x < mGridWidth - 1 ? x : mGridWidth - 1;
            int y = (int) (vec.getY() * mMultiplier);
            y = y < 0 ? 0 : y;
            y = y < mGridHeight - 1 ? y : mGridHeight - 1;
            if (mGrid[y][x] > 0) continue;
            fillSector(x, y);
        }
        return this;
    }

    private void fillSector(int x, int y) {
        mGrid[y][x]++;
//        for (int k = 1; k < mMultiplier; k++) {
//            boolean left = false, top = false, right = false, bottom = false;
//            if (x - k >= 0) left = true;
//            if (y + k < mGridHeight) top = true;
//            if (x + k < mGridWidth) right = true;
//            if (y - k >= 0) bottom = true;
//            if (left) mGrid[y][x - k]++;
//            if (top) mGrid[y + k][x]++;
//            if (right) mGrid[y][x + k]++;
//            if (bottom) mGrid[y - k][x]++;
//            if (left && top) mGrid[y + k][x - k]++;
//            if (top && right) mGrid[y + k][x + k]++;
//            if (right && bottom) mGrid[y - k][x + k]++;
//            if (bottom && left) mGrid[y - k][x - k]++;
//        }
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
                    boolean left = isFillLeft(i, j);
                    boolean top = isFillTop(i, j);
                    boolean right = isFillRight(i, j);
                    boolean bottom = isFillBottom(i, j);

                    if ((left && right) || (top && bottom)) {
                        mGrid[i][j]++;
                        countFilled++;
                        for (int k = 1; k < mMultiplier; k++) {
                            if (left && j - k >= 0) mGrid[i][j - k]++;
                            if (top && i + k < mGridHeight) mGrid[i + k][j]++;
                            if (right && j + k < mGridWidth) mGrid[i][j + k]++;
                            if (bottom && i - k >= 0) mGrid[i - k][j]++;
                        }
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
            if (y + k < mGridHeight && mGrid[y + k][x] != 0) {
                return true;
            }
        }
        return false;
    }

    private boolean isFillRight(int y, int x) {
        for (int k = 1; k <= mMultiplier; k++) {
            if (x + k < mGridWidth && mGrid[y][x + k] != 0) {
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
        long startTime = System.currentTimeMillis();
        float[] floats2 = new float[mGrid.length * mGrid[0].length * 12];
        int count = 0;
        for (int i = 0; i < mGrid.length; i++) {
            for (int j = 0; j < mGrid[i].length; j++) {
                if (mGrid[i][j] == 0) continue;
                float x = (float) (Math.floor(j) / mMultiplier);
                float y = (float) (Math.floor(i) / mMultiplier);
                x += mHalfParticleSize / mMultiplier;
                y += mHalfParticleSize / mMultiplier;
                floats2[count + 0] = x - mHalfParticleSize;
                floats2[count + 1] = y - mHalfParticleSize;
                floats2[count + 2] = x - mHalfParticleSize;
                floats2[count + 3] = y + mHalfParticleSize;
                floats2[count + 4] = x + mHalfParticleSize;
                floats2[count + 5] = y + mHalfParticleSize;
                floats2[count + 6] = x - mHalfParticleSize;
                floats2[count + 7] = y - mHalfParticleSize;
                floats2[count + 8] = x + mHalfParticleSize;
                floats2[count + 9] = y - mHalfParticleSize;
                floats2[count + 10] = x + mHalfParticleSize;
                floats2[count + 11] = y + mHalfParticleSize;
                count += 12;
            }
        }
        Timber.d("convertGridToPoints fill %d", System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        float[] calculatedPoints = Arrays.copyOf(floats2, count);
        Timber.d("convertGridToPoints convert %d", System.currentTimeMillis() - startTime);

        return calculatedPoints;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  GETTERS  ////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public int[][] getGrid() {
        return mGrid;
    }
}
