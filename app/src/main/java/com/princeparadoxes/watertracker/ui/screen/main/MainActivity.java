package com.princeparadoxes.watertracker.ui.screen.main;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.princeparadoxes.watertracker.ProjectComponent;
import com.princeparadoxes.watertracker.R;
import com.princeparadoxes.watertracker.base.BaseActivity;
import com.princeparadoxes.watertracker.base.FragmentSwitcherCompat;
import com.princeparadoxes.watertracker.base.HasFragmentContainer;
import com.princeparadoxes.watertracker.ui.screen.main.start.StartFragment;
import com.princeparadoxes.watertracker.ui.screen.main.statistic.StatisticFragment;
import com.princeparadoxes.watertracker.ui.screen.main.water.WaterRenderer;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  VIEWS  //////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @BindView(R.id.main_start_fragment_container)
    View mStartFragmentContainer;
    @BindView(R.id.main_statistic_fragment_container)
    View mStatisticFragmentContainer;
    @BindView(R.id.water_gl_view)
    GLSurfaceView mGLSurfaceView;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private BottomSheetBehavior mStartBottomSheetBehavior;
    private WaterRenderer mWaterRenderer;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  INIT SCREEN  ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected Integer layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreateComponent(ProjectComponent projectComponent) {
        MainScope.Component component = DaggerMainScope_Component.builder()
                .projectComponent(projectComponent)
                .build();
        component.inject(this);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CREATE SCREEN  //////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            addStartScreen();
            addStatisticScreen();
        }
        int color = ContextCompat.getColor(this, R.color.accent);
//        StatusBarUtil.setColor(this, color);
        getWindow().setStatusBarColor(color);
        getWindow().setNavigationBarColor(color);

        mStartBottomSheetBehavior = BottomSheetBehavior.from(mStartFragmentContainer);
        mStartBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mStartBottomSheetBehavior.setPeekHeight(0);

        mWaterRenderer = new WaterRenderer(this);
        initGlSurfaceViewIfNeeded();
    }

    private void initGlSurfaceViewIfNeeded() {
        mGLSurfaceView.setEGLContextClientVersion(2);
//        mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 8, 0);
        mGLSurfaceView.setRenderer(mWaterRenderer);
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  START SCREEN  ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onStart() {
        super.onStart();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  PAUSE/RESUME  ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  STOP SCREEN  ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onStop() {
        super.onStop();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  NAVIGATION  /////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private void addStartScreen() {
        FragmentSwitcherCompat.start(getSupportFragmentManager())
                .fragment(StartFragment.newInstance())
                .containerId(R.id.main_start_fragment_container)
                .add();
    }

    private void addStatisticScreen() {
        FragmentSwitcherCompat.start(getSupportFragmentManager())
                .fragment(StatisticFragment.newInstance())
                .containerId(R.id.main_statistic_fragment_container)
                .add();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  INSTANCE  ///////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public static void start(Context context, boolean clearStack) {
        Intent intent = new Intent(context, MainActivity.class);
        if (clearStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(intent);
    }

}
