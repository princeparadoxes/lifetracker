package com.princeparadoxes.watertracker.ui.screen.main;

import android.content.Context;
import android.content.Intent;
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
import com.princeparadoxes.watertracker.ui.screen.main.water.WaterFragment;
import com.princeparadoxes.watertracker.utils.StatusBarUtil;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements HasFragmentContainer {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  VIEWS  //////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @BindView(R.id.main_start_fragment_container)
    View mStartFragmentContainer;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private BottomSheetBehavior mBottomSheetBehavior;

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

    @Override
    public int fragmentsContainerId() {
        return R.id.main_water_fragment_container;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CREATE SCREEN  //////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            addStartScreen();
            addWaterScreen();
        }
        int color = ContextCompat.getColor(this, R.color.accent);
//        StatusBarUtil.setColor(this, color);
        getWindow().setStatusBarColor(color);
        getWindow().setNavigationBarColor(color);

        mBottomSheetBehavior = BottomSheetBehavior.from(mStartFragmentContainer);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mBottomSheetBehavior.setPeekHeight(0);
    }

    public void setTranslucentStatus() {
        StatusBarUtil.setTranslucentStatus(this, true);
    }

    public void setTranslucentStatusPadding() {
        StatusBarUtil.setTransparent(this);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  START SCREEN  ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onStart() {
        super.onStart();
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

    private void addWaterScreen() {
        FragmentSwitcherCompat.start(getSupportFragmentManager())
                .fragment(WaterFragment.newInstance())
                .containerId(R.id.main_water_fragment_container)
                .add();
    }

    private void addStartScreen() {
        FragmentSwitcherCompat.start(getSupportFragmentManager())
                .fragment(StartFragment.newInstance())
                .containerId(R.id.main_start_fragment_container)
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
