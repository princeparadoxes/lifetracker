package com.princeparadoxes.watertracker.openGL;

import com.princeparadoxes.watertracker.openGL.drawer.grid.GridDrawer;

import org.jbox2d.common.Vec2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(MockitoJUnitRunner.class)
public class GridDrawerTest {

    @Mock
    GridDrawer mDrawer;


    ///////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////   int[] fillGrid(Vec2[] positions)   ////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void testFillGrid() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = GridDrawer.class.getDeclaredMethod("fillGrid", Vec2[].class, float.class, float.class);
        method.setAccessible(true);
        Vec2[] test = new Vec2[4];
        test[0] = new Vec2(0.5f, 0.7f);
//        test[1] = new Vec2(0.9f, 1.2f);
//        test[2] = new Vec2(19.2f, 0.1f);
//        test[3] = new Vec2(19.2f, 29.6f);
        int[] grid = (int[]) method.invoke(mDrawer, test, 20F, 30.63F);
        assertThat(grid[0], is(1));
    }

//    /**
//     * Result
//     * http://www.wolframalpha.com/input/?i=2*(arcsin((96%2F2)+%2F+sqrt(pow(96%2F2,+2)+%2B+pow(128*+(2+%2B1),+2))))*+(180%2Fpi)
//     */
//    @Test
//    public void testGetIconAngleForRowForRow2Size96Distance128Equals14() {
//        when(mDrawer.getIconSize()).thenReturn(96);
//        when(mDrawer.getDistanceBetweenRows()).thenReturn(128);
//        when(mDrawer.getMaxRows()).thenReturn(2);
//        assertThat(Math.round(mDrawer.getIconAngleForRow(2)), is(14L));
//    }
//
//    /**
//     * Result
//     * http://www.wolframalpha.com/input/?i=2*(arcsin((256%2F2)+%2F+sqrt(pow(256%2F2,+2)+%2B+pow(128*+(0+%2B1),+2))))*+(180%2Fpi)
//     */
//    @Test
//    public void testGetIconAngleForRowForRow0Size256Distance128Equals90() {
//        when(mDrawer.getIconSize()).thenReturn(256);
//        when(mDrawer.getDistanceBetweenRows()).thenReturn(128);
//        assertThat(Math.round(mDrawer.getIconAngleForRow(0)), is(90L));
//    }
//
//    /**
//     * Result
//     * http://www.wolframalpha.com/input/?i=2*(arcsin((512%2F2)+%2F+sqrt(pow(512%2F2,+2)+%2B+pow(0*+(0+%2B1),+2))))*+(180%2Fpi)
//     */
//    @Test
//    public void testGetIconAngleForRowForRow0Size512Distance0Equals180() {
//        when(mDrawer.getIconSize()).thenReturn(512);
//        when(mDrawer.getDistanceBetweenRows()).thenReturn(0);
//        assertThat(Math.round(mDrawer.getIconAngleForRow(0)), is(180L));
//    }
//
//    @Test(expected = ThemeException.class)
//    public void testGetIconAngleForRowForRow3MaxRow2() {
//        when(mDrawer.getIconSize()).thenReturn(96);
//        when(mDrawer.getDistanceBetweenRows()).thenReturn(128);
//        when(mDrawer.getMaxRows()).thenReturn(2);
//        mDrawer.getIconAngleForRow(3);
//    }

}
