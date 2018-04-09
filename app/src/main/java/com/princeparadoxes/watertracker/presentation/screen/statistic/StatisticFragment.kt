package com.princeparadoxes.watertracker.presentation.screen.statistic

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.content.ContextCompat
import android.text.SpannableStringBuilder
import android.transition.Fade
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.daimajia.swipe.SwipeLayout
import com.jakewharton.rxbinding2.view.RxView
import com.princeparadoxes.watertracker.ProjectApplication
import com.princeparadoxes.watertracker.R
import com.princeparadoxes.watertracker.presentation.base.BaseFragment
import com.princeparadoxes.watertracker.domain.entity.Drink
import com.princeparadoxes.watertracker.domain.entity.StatisticModel
import com.princeparadoxes.watertracker.presentation.base.FragmentSwitcherCompat
import com.princeparadoxes.watertracker.presentation.screen.settings.SettingsFragment
import com.princeparadoxes.watertracker.presentation.utils.ApplicationSwitcher
import com.princeparadoxes.watertracker.presentation.utils.DimenTools
import com.princeparadoxes.watertracker.presentation.utils.SpannableUtils
import com.princeparadoxes.watertracker.safeSubscribe
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
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
    private val headerSettings by lazy { view!!.findViewById(R.id.statistic_header_settings) as TextView }
    private val headerReport by lazy { view!!.findViewById(R.id.statistic_header_report) as TextView }
    private val headerRevert by lazy { view!!.findViewById(R.id.statistic_header_revert) as TextView }
    private val headerStartText by lazy { view!!.findViewById(R.id.statistic_header_start) as TextView }
    private val headerCenterText by lazy { view!!.findViewById(R.id.statistic_header_center) as TextView }
    private val headerCenterTextImage by lazy { view!!.findViewById(R.id.statistic_header_center_image) as ImageView }
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
        typePicker.apply {
            adapter = statisticAdapter
            setOnItemChangedListener(this@StatisticFragment)
            setScrollStateChangeListener(this@StatisticFragment)
            scrollToPosition(0)
            setItemTransitionTimeMillis(150)
            setItemTransformer(ScaleTransformer.Builder().setMinScale(0.8f).build())
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // HEADER
    ///////////////////////////////////////////////////////////////////////////

    private fun initHeader() {
        headerView.apply {
            isLeftSwipeEnabled = true
            isRightSwipeEnabled = true
        }
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

    private fun createHeaderAnimator(text: Int, drawable: Drawable?): Animator {
        return when (Math.round(headerCenterTextImage.rotation) == 180) {
            true -> ObjectAnimator.ofFloat(headerCenterTextImage, View.ROTATION, 180F, 0F)
            false -> ObjectAnimator.ofFloat(headerCenterTextImage, View.ROTATION, 0F, 180F)
        }.apply {
            duration = 600
            interpolator = BounceInterpolator()
        }
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
                statisticViewModel.observeDeleteWater(RxView.clicks(headerRevert)).safeSubscribe { handleDeleteWater(it) },
                statisticViewModel.observeReport(RxView.clicks(headerReport)).safeSubscribe { handleReport() },
                statisticViewModel.observeSetting(RxView.clicks(headerSettings)).safeSubscribe {
                    FragmentSwitcherCompat.start(fragmentManager)
                            .fragment(SettingsFragment.newInstance())
                            .containerId(R.id.main_start_fragment_container)
                            .replace();
                    BottomSheetBehavior.from<View>(activity!!.findViewById(R.id.main_start_fragment_container)).state = BottomSheetBehavior.STATE_EXPANDED
                },
                statisticViewModel.observeStatistic().safeSubscribe({ this.handleStatistic(it) }),
                statisticViewModel.observeDaySum().safeSubscribe({ this.handleDaySum(it) }),
                statisticViewModel.observeLastDrink().safeSubscribe({ this.handleLastDrink(it) }),
                statisticViewModel.observeDrinksByPeriod().safeSubscribe({ this.handleDrinksByPeriod(it) }),
                statisticViewModel.observeDetailStatistic().safeSubscribe({ this.handleAverage(it) })
        )
    }

    private fun handleReport() {
        ApplicationSwitcher.start(context).openEmailApplication("", "I have a problem")
    }

    private fun handleDeleteWater(it: Int) {
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
