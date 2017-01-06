package com.princeparadoxes.watertracker.ui.screen.main.water;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.princeparadoxes.watertracker.ProjectApplication;
import com.princeparadoxes.watertracker.R;
import com.princeparadoxes.watertracker.base.BaseFragment;
import com.princeparadoxes.watertracker.misc.AccelerometerListener;
import com.princeparadoxes.watertracker.misc.AndroidFastRenderView;
import com.princeparadoxes.watertracker.misc.Box;
import com.princeparadoxes.watertracker.misc.EnclosureWorldObject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;

public class WaterFragment extends BaseFragment {


    private static final float XMIN = -10, XMAX = 10, YMIN = -15, YMAX = 15;


    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  VIEWS  //////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

//    @BindView(R.id.toolbar)
//    Toolbar mToolbar;
    @BindView(R.id.water_fast_render_view)
    AndroidFastRenderView mRenderView;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    private WaterScope.Component mComponent;
    private CompositeDisposable mDisposable;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////  INIT SCREEN  /////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectApplication app = ProjectApplication.get(getActivity());
        mComponent = DaggerWaterScope_Component.builder()
                .projectComponent(app.component())
                .build();
        mComponent.inject(this);

        System.loadLibrary("liquidfun");
        System.loadLibrary("liquidfun_jni");
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_water;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////  START SCREEN  ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onStart() {
        super.onStart();
        mDisposable = new CompositeDisposable();
//        initToolbar();
        initGlSurfaceView();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////  TOOLBAR  ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

//    private void initToolbar() {
//        mToolbar.setTitle(R.string.water_fragment_title);
//    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  GL SURFACE VIEW  ////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private void initGlSurfaceView() {

        Box physicalSize = new Box(XMIN, YMIN, XMAX, YMAX);
        Box screenSize = new Box(0, 0, mRenderView.getWidth(), mRenderView.getHeight());
        WaterWorld gw = new WaterWorld(physicalSize, screenSize);
        gw.addGameObject(new EnclosureWorldObject(gw, XMIN, XMAX, YMIN, YMAX));
        gw.addGameObject(new WaterWorldObject(gw, 0, 5));
        mRenderView.setGameworld(gw);
        SensorManager smanager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        if (smanager.getSensorList(Sensor.TYPE_ACCELEROMETER).isEmpty()) {
            Log.i(getString(R.string.app_name), "No accelerometer");
        } else {
            Sensor accelerometer = smanager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            if (!smanager.registerListener(new AccelerometerListener(gw), accelerometer, SensorManager.SENSOR_DELAY_NORMAL))
                Log.i(getString(R.string.app_name), "Could not register accelerometer listener");
        }

//        mRenderView.setEGLContextClientVersion(2);
//        mRenderView.setRenderer(new OpenGLRenderer());
    }


    @Override
    public void onResume() {
        super.onResume();
//        mRenderView.onResume();
        mRenderView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
//        mRenderView.onPause();
        mRenderView.pause();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  STOP SCREEN  ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onStop() {
        mDisposable.dispose();
        super.onStop();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////  NAVIGATION  ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  INSTANCE  ///////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public static WaterFragment newInstance() {
        Bundle args = new Bundle();
        WaterFragment fragment = new WaterFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
