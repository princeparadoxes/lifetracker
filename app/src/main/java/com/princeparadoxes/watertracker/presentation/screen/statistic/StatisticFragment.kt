package com.princeparadoxes.watertracker.presentation.screen.statistic

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.content.ContextCompat
import android.text.SpannableStringBuilder
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import com.daimajia.swipe.SwipeLayout
import com.princeparadoxes.watertracker.ProjectApplication
import com.princeparadoxes.watertracker.R
import com.princeparadoxes.watertracker.base.BaseFragment
import com.princeparadoxes.watertracker.domain.entity.Drink
import com.princeparadoxes.watertracker.domain.entity.StatisticModel
import com.princeparadoxes.watertracker.utils.AnimatorUtils
import com.princeparadoxes.watertracker.utils.DimenTools
import com.princeparadoxes.watertracker.utils.SpannableUtils
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import timber.log.Timber
import javax.inject.Inject

class StatisticFragment : BaseFragment(), DiscreteScrollView.OnItemChangedListener<StatisticItemViewHolder>, DiscreteScrollView.ScrollStateChangeListener<StatisticItemViewHolder> {

    ///////////////////////////////////////////////////////////////////////////
    // INJECTS
    ///////////////////////////////////////////////////////////////////////////

    @Inject
    internal lateinit var statisticViewModel: StatisticViewModel

    ///////////////////////////////////////////////////////////////////////////
    // RESOURCES
    ///////////////////////////////////////////////////////////////////////////

    private val headerView by lazy { view!!.findViewById(R.id.statistic_header) as SwipeLayout }
    private val headerStartText by lazy { view!!.findViewById(R.id.statistic_header_start) as TextView }
    private val headerCenterText by lazy { view!!.findViewById(R.id.statistic_header_center) as TextView }
    private val headerEndText by lazy { view!!.findViewById(R.id.statistic_header_end) as TextView }
    private val typePicker by lazy { view!!.findViewById(R.id.statistic_type_view) as DiscreteScrollView }
    private val chartView by lazy { view!!.findViewById(R.id.statistic_forecast_view) as StatisticChartView }
    private val topBorderView by lazy { view!!.findViewById(R.id.statistic_top_border) as View }

    private val chevronDownDrawable by lazy { ContextCompat.getDrawable(context!!, R.drawable.ic_chevron_down) }
    private val chevronUpDrawable by lazy { ContextCompat.getDrawable(context!!, R.drawable.ic_chevron_up) }

    ///////////////////////////////////////////////////////////////////////////
    // FIELDS
    ///////////////////////////////////////////////////////////////////////////

    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(view!!.parent as ViewGroup)!! }
    private val statisticAdapter by lazy { StatisticAdapter() }

    ///////////////////////////////////////////////////////////////////////////
    // INIT SCREEN
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerStatisticScope_Component.builder()
                .projectComponent(ProjectApplication.component())
                .module(StatisticScope.Module(this))
                .build()
                .inject(this)
    }

    override fun layoutId(): Int = R.layout.fragment_statistic

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader()
        initBehavior(view.parent as ViewGroup)
        onCollapsed()
        initTypePicker()
    }

    private fun initTypePicker() {
        typePicker.adapter = statisticAdapter
        typePicker.setOnItemChangedListener(this)
        typePicker.setScrollStateChangeListener(this)
        typePicker.scrollToPosition(0)
        typePicker.setItemTransitionTimeMillis(150)
        typePicker.setItemTransformer(ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build())
    }

    ///////////////////////////////////////////////////////////////////////////
    // HEADER
    ///////////////////////////////////////////////////////////////////////////

    private fun initHeader() {
        headerView.isLeftSwipeEnabled = true
        headerView.isRightSwipeEnabled = true
    }

    private fun changeHeaderState(newState: Int) {
        when (newState) {
            BottomSheetBehavior.STATE_COLLAPSED -> onCollapsed()
            BottomSheetBehavior.STATE_EXPANDED -> onExpanded()
        }
    }

    private fun onCollapsed() {
        topBorderView.visibility = View.VISIBLE
        createHeaderAnimator(R.string.statistic_header_open, chevronUpDrawable).start()
    }

    private fun onExpanded() {
        topBorderView.visibility = View.INVISIBLE
        createHeaderAnimator(R.string.statistic_header_closed, chevronDownDrawable).start()
    }

    private fun createHeaderAnimator(text: Int, drawable: Drawable?): AnimatorSet {
        val set = AnimatorSet()
        val out = AnimatorUtils.alphaAnimator(1, 0, headerCenterText, 200, object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                headerCenterText.setText(text)
                headerCenterText.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            }
        })
        out.interpolator = AccelerateInterpolator()
        val `in` = AnimatorUtils.alphaAnimator(0, 1, headerCenterText, 200)
        `in`.interpolator = DecelerateInterpolator()
        set.playSequentially(out, `in`)
        return set
    }

    ///////////////////////////////////////////////////////////////////////////
    // BEHAVIOR
    ///////////////////////////////////////////////////////////////////////////

    private fun initBehavior(container: ViewGroup?) {
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                changeHeaderState(newState)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
        bottomSheetBehavior.apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            isHideable = false
            peekHeight = DimenTools.pxFromDp(context, 64f).toInt()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // START SCREEN
    ///////////////////////////////////////////////////////////////////////////

    override fun onStart() {
        super.onStart()
        unsubscribeOnStop(
                statisticViewModel.observeStatistic().subscribe({ this.handleStatistic(it) }, { Timber.e(it) }),
                statisticViewModel.observeDaySum().subscribe({ this.handleDaySum(it) }, { Timber.e(it) }),
                statisticViewModel.observeLastDrink().subscribe({ this.handleLastDrink(it) }, { Timber.e(it) }),
                statisticViewModel.observeDrinksByPeriod().subscribe({ this.handleDrinksByPeriod(it) }, { Timber.e(it) }),
                statisticViewModel.observeDetailStatistic().subscribe({ this.handleAverage(it) }, { Timber.e(it) })
        )
    }

    private fun handleAverage(it: List<Int>) {
        chartView.bindView(
                it.mapIndexed { index, _ -> index.toFloat() }.toFloatArray(),
                it.map { it.toFloat() }.toFloatArray())
    }

    private fun handleStatistic(statisticModels: List<StatisticModel>) {
        statisticAdapter.setData(statisticModels)
    }

    private fun handleDaySum(daySum: Int) {
        val builder = SpannableStringBuilder()
        builder.append(SpannableUtils.getAbsoluteSizeSpan(context, "Day", 12))
                .append("\n")
                .append(daySum.toString())
                .append("ml")
        headerStartText.text = builder
    }

    private fun handleLastDrink(lastByDay: Int) {
        val builder = SpannableStringBuilder()
        builder.append(SpannableUtils.getAbsoluteSizeSpan(context, "Last", 12))
                .append("\n")
                .append(if (lastByDay != 0) lastByDay.toString() + "ml" else "none")
        headerEndText.text = builder
    }

    private fun handleDrinksByPeriod(drinks: List<Drink>) {
        if (drinks.isEmpty()) return
//        chartView.bindView(
//                drinks.mapIndexed { index, drink -> index.toFloat() }.toFloatArray(),
//                drinks.map { it.size.toFloat() }.toFloatArray())
    }

    ///////////////////////////////////////////////////////////////////////////
    // TYPE PICKER
    ///////////////////////////////////////////////////////////////////////////

    override fun onCurrentItemChanged(viewHolder: StatisticItemViewHolder, adapterPosition: Int) {
//        chartView.bindView(
//                floatArrayOf(0f, 2f, 1.4f, 4f, 3.5f, 4.3f, 2f, 4f, 6f),
//                floatArrayOf(0f, 2f, 1.4f, 4f, 3.5f, 4.3f, 2f, 4f, 6f))
        statisticViewModel.changeDrinkPeriod(statisticAdapter.getItem(adapterPosition).statisticType)
        viewHolder.onChanged()
    }

    override fun onScrollStart(currentItemHolder: StatisticItemViewHolder, adapterPosition: Int) {
        currentItemHolder.onUnchanged()
    }

    override fun onScrollEnd(currentItemHolder: StatisticItemViewHolder, adapterPosition: Int) {

    }

    override fun onScroll(scrollPosition: Float,
                          currentHolder: StatisticItemViewHolder,
                          newCurrent: StatisticItemViewHolder) {
        //        StatisticType current = mStatisticTypes.get(typePicker.getCurrentItem());
        //        int nextPosition = typePicker.getCurrentItem() + (scrollPosition > 0 ? -1 : 1);
        //        if (nextPosition >= 0 && nextPosition < typePicker.getAdapter().getItemCount()) {
        //            StatisticType next = mStatisticTypes.get(nextPosition);
        //            chartView.onScroll(1f - Math.abs(scrollPosition), current, next);
        //        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // STOP SCREEN
    ///////////////////////////////////////////////////////////////////////////

    override fun onDestroyView() {
        super.onDestroyView()
        bottomSheetBehavior.setBottomSheetCallback(null)
    }

    companion object {

        fun newInstance(): StatisticFragment {
            val args = Bundle()
            val fragment = StatisticFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
