package com.princeparadoxes.watertracker.presentation.screen.main

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.view.ViewCompat
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.BounceInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import com.google.fpl.liquidfunpaint.physics.WorldLock
import com.google.fpl.liquidfunpaint.physics.actions.ParticleEraser
import com.google.fpl.liquidfunpaint.physics.actions.ParticleGroup
import com.google.fpl.liquidfunpaint.util.MathHelper
import com.google.fpl.liquidfunpaint.util.Vector2f
import com.mycardboarddreams.liquidsurface.LiquidSurfaceView
import com.princeparadoxes.watertracker.ProjectComponent
import com.princeparadoxes.watertracker.R
import com.princeparadoxes.watertracker.base.BaseActivity
import com.princeparadoxes.watertracker.base.FragmentSwitcherCompat
import com.princeparadoxes.watertracker.data.source.sp.ProjectPreferences
import com.princeparadoxes.watertracker.domain.interactor.DrinkOutputGateway
import com.princeparadoxes.watertracker.presentation.screen.statistic.StatisticFragment
import com.princeparadoxes.watertracker.presentation.view.RulerView
import com.princeparadoxes.watertracker.utils.rx.SchedulerTransformer
import com.princeparadoxes.watertracker.utils.safeSubscribe
import com.princeparadoxes.watertracker.utils.toColorInt
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : BaseActivity() {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  INJECTS  ////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Inject
    lateinit var drinkOutputGateway: DrinkOutputGateway
    @Inject
    lateinit var projectPreferences: ProjectPreferences

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  VIEWS  //////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private val rulerView by lazy { findViewById(R.id.main_ruler_view) as RulerView }
    private val valueView by lazy { findViewById(R.id.main_value) as TextView }
    private val startFragmentContainer by lazy { findViewById(R.id.main_start_fragment_container) as View }
    private val statisticFragmentContainer by lazy { findViewById(R.id.main_statistic_fragment_container) as View }
    private val dayNormContainer by lazy { findViewById(R.id.day_norm_container) as View }
    private val dayNormValue by lazy { findViewById(R.id.day_norm_value) as TextView }
    private val waterContainer by lazy { findViewById(R.id.main_water_container) as FrameLayout }
    private val waterView by lazy { findViewById(R.id.water_gl_view) as LiquidSurfaceView }
    private val touchFrame by lazy { findViewById(R.id.main_water_touch_frame) as View }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private var startBottomSheetBehavior: BottomSheetBehavior<*>? = null
    private val disposable: CompositeDisposable = CompositeDisposable()
    private var downY: Float = 0.toFloat()

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  INIT SCREEN  ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    override fun layoutId(): Int? = R.layout.activity_main


    override fun onCreateComponent(projectComponent: ProjectComponent) {
        val component = DaggerMainScope_Component.builder()
                .projectComponent(projectComponent)
                .build()
        component.inject(this)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CREATE SCREEN  //////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            addStartScreen()
            addStatisticScreen()
        }
        window.apply {
            statusBarColor = R.color.accent.toColorInt()
            navigationBarColor = R.color.accent.toColorInt()
        }

        startBottomSheetBehavior = BottomSheetBehavior.from(startFragmentContainer).apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            peekHeight = 0
        }

        loadDaySum()
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  START SCREEN  ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    override fun onStart() {
        super.onStart()
        initDayNorm()
        initTouchFrame()
    }

    private fun initDayNorm() {
        val dayNorm = projectPreferences.currentDayNorm
        dayNormValue.text = dayNorm.toString() + "ml"
        waterView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                waterView.viewTreeObserver.removeOnPreDrawListener(this)
                val translationY = waterView.height.toFloat() / 10 - dayNormContainer.height
                dayNormContainer.translationY = translationY
                return false
            }
        })

    }

    ///////////////////////////////////////////////////////////////////////////
    // TOUCH FRAME
    ///////////////////////////////////////////////////////////////////////////

    private fun initTouchFrame() {
        touchFrame.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> downY = event.y
                MotionEvent.ACTION_MOVE -> {
                    var newTranslationY = waterContainer.translationY + event.y - downY
                    newTranslationY = if (newTranslationY < 0) 0F else newTranslationY
                    waterContainer.translationY = newTranslationY
                    downY = event.y
                    setCurrentValueText()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    addWater(rulerView.getNearestValue(waterContainer.translationY.toInt()))
                    WorldLock.getInstance().setBlockAccelerometer(true)
                    WorldLock.getInstance().setGravity(0.0f, waterContainer.translationY / 100, false)
                    ViewCompat.animate(waterContainer)
                            .translationY(0f)
                            .setInterpolator(BounceInterpolator())
                            .withEndAction { WorldLock.getInstance().setBlockAccelerometer(false) }
                            .start()
                }
            }
            true
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // CURRENT VALUE
    ///////////////////////////////////////////////////////////////////////////

    private fun setCurrentValueText() {
        val value = rulerView.getNearestValue(waterContainer.translationY.toInt())
        val sp = value / 25
        valueView.setTextSize(TypedValue.COMPLEX_UNIT_SP, (12 + sp).toFloat())
        var s = ""
        if (sp > 15) {
            var i = 0
            while (i < sp - 15) {
                s += "!"
                i += 2
            }
        }
        valueView.text = value.toString() + "ml" + s
    }

    ///////////////////////////////////////////////////////////////////////////
    // WATER
    ///////////////////////////////////////////////////////////////////////////

    private fun addWater(ml: Int) {
        if (ml == 0) return
        drinkOutputGateway.addWater(ml)
                .compose(SchedulerTransformer.getInstance())
                .safeSubscribe({ drawWater(ml) })
    }

    private fun drawWater(ml: Int) {
        if (ml == 0) return
        val size = Point().also { windowManager.defaultDisplay.getSize(it) }
        val aspect = ml.toFloat() / projectPreferences.currentDayNorm
        val center = Vector2f((size.x / 2).toFloat(), (size.y / 2).toFloat())
        val shape = MathHelper.createBox(center, size.x.toFloat(), size.y * aspect)
        waterView.createParticles(ParticleGroup(shape))
    }

    ///////////////////////////////////////////////////////////////////////////
    // LOAD DAY STATISTIC
    ///////////////////////////////////////////////////////////////////////////

    private fun loadDaySum() {
        disposable.add(drinkOutputGateway.getDaySum().firstOrError().safeSubscribe({ drawWater(it) }))
        disposable.add(drinkOutputGateway.observeRemoveDrinks().safeSubscribe({ redrawWater(it) }))
    }

    private fun redrawWater(it: Int) {
        val size = Point().also { windowManager.defaultDisplay.getSize(it) }
        val center = Vector2f((size.x / 2).toFloat(), (size.y / 2).toFloat())
        val shape = MathHelper.createBox(center, size.x.toFloat(), size.y.toFloat())
        waterView.eraseParticles(ParticleEraser(shape))
        drawWater(it)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  PAUSE/RESUME  ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public override fun onResume() {
        super.onResume()
        waterView.resumePhysics()
    }

    public override fun onPause() {
        super.onPause()
        waterView.pausePhysics()
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  NAVIGATION  /////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private fun addStartScreen() {
        //        FragmentSwitcherCompat.start(getSupportFragmentManager())
        //                .fragment(StartFragment.newInstance())
        //                .containerId(R.id.main_start_fragment_container)
        //                .add();
    }

    private fun addStatisticScreen() {
        FragmentSwitcherCompat.start(supportFragmentManager)
                .fragment(StatisticFragment.newInstance())
                .containerId(R.id.main_statistic_fragment_container)
                .add()
    }

    ///////////////////////////////////////////////////////////////////////////
    // ON DESTROY
    ///////////////////////////////////////////////////////////////////////////

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }

    companion object {

        fun start(context: Context, clearStack: Boolean) {
            val intent = Intent(context, MainActivity::class.java)
            if (clearStack) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }
    }

}