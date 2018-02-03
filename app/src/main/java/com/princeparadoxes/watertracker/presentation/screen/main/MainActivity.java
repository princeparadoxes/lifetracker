package com.princeparadoxes.watertracker.presentation.screen.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.fpl.liquidfunpaint.physics.WorldLock;
import com.google.fpl.liquidfunpaint.physics.actions.ParticleGroup;
import com.google.fpl.liquidfunpaint.util.MathHelper;
import com.google.fpl.liquidfunpaint.util.Vector2f;
import com.mycardboarddreams.liquidsurface.LiquidSurfaceView;
import com.princeparadoxes.watertracker.ProjectComponent;
import com.princeparadoxes.watertracker.R;
import com.princeparadoxes.watertracker.base.BaseActivity;
import com.princeparadoxes.watertracker.base.FragmentSwitcherCompat;
import com.princeparadoxes.watertracker.domain.interactor.DrinkOutputGateway;
import com.princeparadoxes.watertracker.utils.rx.SchedulerTransformer;
import com.princeparadoxes.watertracker.data.source.sp.ProjectPreferences;
import com.princeparadoxes.watertracker.presentation.view.RulerView;
import com.princeparadoxes.watertracker.presentation.screen.statistic.StatisticFragment;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends BaseActivity {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  INJECTS  ////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Inject
    DrinkOutputGateway drinkOutputGateway;
    @Inject
    ProjectPreferences mProjectPreferences;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  VIEWS  //////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @BindView(R.id.main_ruler_view)
    RulerView mRulerView;
    @BindView(R.id.main_value)
    TextView mValueView;
    @BindView(R.id.main_start_fragment_container)
    View mStartFragmentContainer;
    @BindView(R.id.main_statistic_fragment_container)
    View mStatisticFragmentContainer;
    @BindView(R.id.day_norm_container)
    View mDayNormContainer;
    @BindView(R.id.day_norm_value)
    TextView mDayNormValue;
    @BindView(R.id.main_water_container)
    FrameLayout mWaterContainer;
    @BindView(R.id.water_gl_view)
    LiquidSurfaceView mWaterView;
    @BindView(R.id.main_water_touch_frame)
    View mTouchFrame;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private BottomSheetBehavior mStartBottomSheetBehavior;
    //    private WaterRenderer mWaterRenderer;
    private CompositeDisposable mDisposable;
    private float mDownY;

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
        mDisposable = new CompositeDisposable();
        int color = ContextCompat.getColor(this, R.color.accent);
        getWindow().setStatusBarColor(color);
        getWindow().setNavigationBarColor(color);

        mStartBottomSheetBehavior = BottomSheetBehavior.from(mStartFragmentContainer);
        mStartBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mStartBottomSheetBehavior.setPeekHeight(0);
//        final SwipeDismissBehavior<View> swipe = new SwipeDismissBehavior<>();
//
//        swipe.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_ANY);
//
//        swipe.setListener(new SwipeDismissBehavior.OnDismissListener() {
//            @Override
//            public void onDismiss(View view) {
//                Toast.makeText(MainActivity.this,
//                        "Card swiped !!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onDragStateChanged(int state) {
//            }
//        });
//
//        CoordinatorLayout.LayoutParams coordinatorParams =
//                (CoordinatorLayout.LayoutParams) mStartFragmentContainer.getLayoutParams();
//
//        coordinatorParams.setBehavior(swipe);

        initGlSurfaceViewIfNeeded();
        loadDaySum();
    }

    private void initGlSurfaceViewIfNeeded() {
//        ParticleGroup liquidShape1 = new ParticleGroup(MathHelper.createCircle(getScreenCenter(), 400, 8));
//        mWaterView.createParticles(liquidShape1);
    }

    private Vector2f getScreenCenter() {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        Vector2f center = new Vector2f(size.x / 2, size.y / 2);
        return center;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  START SCREEN  ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onStart() {
        super.onStart();
        initDayNorm();
        initTouchFrame();
    }

    private void initDayNorm() {
        int dayNorm = mProjectPreferences.getCurrentDayNorm();
        mDayNormValue.setText(dayNorm + "ml");
        mWaterView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mWaterView.getViewTreeObserver().removeOnPreDrawListener(this);
                float translationY = ((float) mWaterView.getHeight()) / 10 - mDayNormContainer.getHeight();
                mDayNormContainer.setTranslationY(translationY);
                return false;
            }
        });

    }

    ///////////////////////////////////////////////////////////////////////////
    // TOUCH FRAME
    ///////////////////////////////////////////////////////////////////////////

    private void initTouchFrame() {
        mTouchFrame.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float newTranslationY = mWaterContainer.getTranslationY() + event.getY() - mDownY;
                    newTranslationY = newTranslationY < 0 ? 0 : newTranslationY;
                    mWaterContainer.setTranslationY(newTranslationY);
                    mDownY = event.getY();
                    setCurrentValueText();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    addWater(mRulerView.getNearestValue((int) mWaterContainer.getTranslationY()));
                    WorldLock.getInstance().setBlockAccelerometer(true);
                    WorldLock.getInstance().setGravity(0.0f, mWaterContainer.getTranslationY() / 100, false);
                    ViewCompat.animate(mWaterContainer)
                            .translationY(0)
                            .setInterpolator(new BounceInterpolator())
                            .withEndAction(() -> WorldLock.getInstance().setBlockAccelerometer(false))
                            .start();
                    break;

            }
            return true;
        });
    }

    ///////////////////////////////////////////////////////////////////////////
    // CURRENT VALUE
    ///////////////////////////////////////////////////////////////////////////

    private void setCurrentValueText() {
        int value = mRulerView.getNearestValue((int) mWaterContainer.getTranslationY());
        int sp = value / 25;
        mValueView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12 + sp);
        String s = "";
        if (sp > 15) {
            for (int i = 0; i < sp - 15; i = i + 2) {
                s += "!";
            }
        }
        mValueView.setText(value + "ml" + s);
    }

    ///////////////////////////////////////////////////////////////////////////
    // WATER
    ///////////////////////////////////////////////////////////////////////////

    private void addWater(int ml) {
        if (ml == 0) return;
        drawWater(ml);
        drinkOutputGateway.addWater(ml)
                .compose(SchedulerTransformer.getInstance())
                .subscribe();
    }

    private void drawWater(int ml) {
        if (ml == 0) return;
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        float aspect = ((float) ml) / mProjectPreferences.getCurrentDayNorm();
        Vector2f center = new Vector2f(size.x / 2, size.y / 2);
        Vector2f[] shape = MathHelper.createBox(center, size.x, size.y * aspect);
        ParticleGroup group = new ParticleGroup(shape);
        mWaterView.createParticles(group);
    }

    ///////////////////////////////////////////////////////////////////////////
    // LOAD DAY STATISTIC
    ///////////////////////////////////////////////////////////////////////////

    private void loadDaySum() {
        mDisposable.add(drinkOutputGateway.getDaySum()
                .compose(SchedulerTransformer.getInstance())
                .subscribe(this::handleLoadDaySum, this::handleLoadDaySumError));
    }

    private void handleLoadDaySum(int daySum) {
        drawWater(daySum);
    }

    private void handleLoadDaySumError(Throwable throwable) {
        throwable.printStackTrace();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  PAUSE/RESUME  ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onResume() {
        super.onResume();
        mWaterView.resumePhysics();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWaterView.pausePhysics();
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
//        FragmentSwitcherCompat.start(getSupportFragmentManager())
//                .fragment(StartFragment.newInstance())
//                .containerId(R.id.main_start_fragment_container)
//                .add();
    }

    private void addStatisticScreen() {
        FragmentSwitcherCompat.start(getSupportFragmentManager())
                .fragment(StatisticFragment.newInstance())
                .containerId(R.id.main_statistic_fragment_container)
                .add();
    }

    ///////////////////////////////////////////////////////////////////////////
    // ON DESTROY
    ///////////////////////////////////////////////////////////////////////////

    @Override
    protected void onDestroy() {
        mDisposable.dispose();
        super.onDestroy();
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
