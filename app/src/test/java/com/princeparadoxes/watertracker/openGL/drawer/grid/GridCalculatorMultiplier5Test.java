package com.princeparadoxes.watertracker.openGL.drawer.grid;

import com.google.fpl.liquidfun.Vec2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(MockitoJUnitRunner.class)
public class GridCalculatorMultiplier5Test {

    public static final int MULTIPLIER = 5;
    GridCalculator mCalculator = new GridCalculator(MULTIPLIER, 20f, 30f, 1);

    ///////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////   fillGrid(Vec2[] positions)   //////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void testFillGridCellBottomLeftCorner() {
        Vec2[] test = new Vec2[1];
        test[0] = new Vec2(0.1f, 0.1f);
        mCalculator.fillGrid(test);
        assertThat(mCalculator.getGrid()[0][0], is(not(0)));
    }

    @Test
    public void testFillGridCellBottomRightCorner() {
        Vec2[] test = new Vec2[1];
        test[0] = new Vec2(19f, 1.1f);
        mCalculator.fillGrid(test);
        assertThat(mCalculator.getGrid()[(int) (1.1 * MULTIPLIER)][19 * MULTIPLIER], is(not(0)));
    }

    @Test
    public void testFillGridCellTopLeftCorner() {
        Vec2[] test = new Vec2[1];
        test[0] = new Vec2(19.2f, 0.1f);
        mCalculator.fillGrid(test);
        assertThat(mCalculator.getGrid()[(int) (0.1 * MULTIPLIER)][(int) (19.2 * MULTIPLIER)], is(not(0)));
    }

    @Test
    public void testFillGridCellTopRightCorner() {
        Vec2[] test = new Vec2[1];
        test[0] = new Vec2(19.2f, 29.6f);
        mCalculator.fillGrid(test);
        assertThat(mCalculator.getGrid()[(int) (29.6 * MULTIPLIER)][(int) (19.2 * MULTIPLIER)], is(not(0)));
    }

    @Test
    public void testFillGridEmptyCellAroundFilled() {
        Vec2[] test = new Vec2[4];
        test[0] = new Vec2(0, 1);
        test[1] = new Vec2(1, 0);
        test[2] = new Vec2(1, 2);
        test[3] = new Vec2(2, 1);
        mCalculator.fillGrid(test).fillEmptySectors();
        assertThat(mCalculator.getGrid()[MULTIPLIER][MULTIPLIER], is(not(0)));
    }

    @Test
    public void testFillGridEmptyCellLeftTopRightFilled() {
        Vec2[] test = new Vec2[3];
        test[0] = new Vec2(0, 1);
        test[1] = new Vec2(1, 2);
        test[2] = new Vec2(2, 1);
        mCalculator.fillGrid(test).fillEmptySectors();
        assertThat(mCalculator.getGrid()[MULTIPLIER][MULTIPLIER], is(not(0)));
    }

    @Test
    public void testFillGridEmptyCellTopRightBottomFilled() {
        Vec2[] test = new Vec2[3];
        test[0] = new Vec2(1, 0);
        test[1] = new Vec2(1, 2);
        test[2] = new Vec2(2, 1);
        mCalculator.fillGrid(test).fillEmptySectors();
        assertThat(mCalculator.getGrid()[MULTIPLIER][MULTIPLIER], is(not(0)));
    }

    @Test
    public void testFillGridEmptyCellRightBottomLeftFilled() {
        Vec2[] test = new Vec2[3];
        test[0] = new Vec2(0, 1);
        test[1] = new Vec2(1, 0);
        test[2] = new Vec2(2, 1);
        mCalculator.fillGrid(test).fillEmptySectors();
        assertThat(mCalculator.getGrid()[MULTIPLIER][MULTIPLIER], is(not(0)));
    }

    @Test
    public void testFillGridEmptyCellTopBottomFilled() {
        Vec2[] test = new Vec2[2];
        test[0] = new Vec2(1, 0);
        test[1] = new Vec2(1, 2);
        mCalculator.fillGrid(test).fillEmptySectors();

        assertThat(mCalculator.getGrid()[MULTIPLIER][MULTIPLIER], is(not(0)));
    }

    @Test
    public void testFillGridEmptyCellLeftRightFilled() {
        Vec2[] test = new Vec2[2];
        test[0] = new Vec2(0, 1);
        test[1] = new Vec2(2, 1);
        mCalculator.fillGrid(test).fillEmptySectors();
        assertThat(mCalculator.getGrid()[MULTIPLIER][MULTIPLIER], is(not(0)));
    }

}
