package com.princeparadoxes.watertracker.presentation.screen.tablet.controls

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior.*
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import com.princeparadoxes.watertracker.ProjectApplication
import com.princeparadoxes.watertracker.R
import com.princeparadoxes.watertracker.presentation.base.BaseFragment
import com.princeparadoxes.watertracker.presentation.base.FragmentSwitcherCompat
import com.princeparadoxes.watertracker.presentation.screen.settings.SettingsFragment
import com.princeparadoxes.watertracker.presentation.screen.statistic.StatisticScope
import com.princeparadoxes.watertracker.presentation.utils.ApplicationSwitcher
import com.princeparadoxes.watertracker.presentation.utils.SpannableUtils
import com.princeparadoxes.watertracker.safeSubscribe
import javax.inject.Inject

class TabletControlsFragment : BaseFragment() {

    ///////////////////////////////////////////////////////////////////////////
    // INJECTS
    ///////////////////////////////////////////////////////////////////////////

    @Inject
    internal lateinit var tabletControlsViewModel: TabletControlsViewModel

    ///////////////////////////////////////////////////////////////////////////
    // RESOURCES
    ///////////////////////////////////////////////////////////////////////////

    private val settingsContainer by lazy { activity!!.findViewById(R.id.main_start_fragment_container) as View }
    private val headerSettings by lazy { view!!.findViewById(R.id.statistic_header_settings) as TextView }
    private val headerReport by lazy { view!!.findViewById(R.id.statistic_header_report) as TextView }
    private val headerRevert by lazy { view!!.findViewById(R.id.statistic_header_revert) as TextView }
    private val headerStartText by lazy { view!!.findViewById(R.id.statistic_header_start) as TextView }
    private val headerEndText by lazy { view!!.findViewById(R.id.statistic_header_end) as TextView }

    ///////////////////////////////////////////////////////////////////////////
    // INIT SCREEN
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerTabletControlsScope_Component.builder()
                .projectComponent(ProjectApplication.component())
                .module(TabletControlsScope.Module(this))
                .build()
                .inject(this)
    }

    override fun layoutId(): Int = R.layout.fragment_tablet_controls

    ///////////////////////////////////////////////////////////////////////////
    // START SCREEN
    ///////////////////////////////////////////////////////////////////////////

    override fun onStart() {
        super.onStart()
        unsubscribeOnStop(
                tabletControlsViewModel.observeDeleteWater(RxView.clicks(headerRevert)).safeSubscribe { handleDeleteWater(it) },
                tabletControlsViewModel.observeReport(RxView.clicks(headerReport)).safeSubscribe { handleReport() },
                tabletControlsViewModel.observeSetting(RxView.clicks(headerSettings)).safeSubscribe {
                    FragmentSwitcherCompat.start(fragmentManager)
                            .fragment(SettingsFragment.newInstance())
                            .containerId(R.id.main_start_fragment_container)
                            .replace()

                    from<View>(settingsContainer).state = STATE_EXPANDED
                },
                tabletControlsViewModel.observeDaySum().safeSubscribe({ this.handleDaySum(it) }),
                tabletControlsViewModel.observeLastDrink().safeSubscribe({ this.handleLastDrink(it) })
        )
    }

    private fun handleReport() {
        ApplicationSwitcher.start(context).openEmailApplication("tzarsuperman@gmail.com", getString(R.string.report_subject))
    }

    private fun handleDeleteWater(it: Int) {
    }

    private fun handleDaySum(daySum: Int) {
        val builder = SpannableStringBuilder()
        builder.append(SpannableUtils.getAbsoluteSizeSpan(context, getString(R.string.statistic_type_day), 12))
                .append("\n")
                .append(daySum.toString())
                .append(getString(R.string.ml))
        headerStartText.text = builder
    }

    private fun handleLastDrink(lastByDay: Int) {
        val builder = SpannableStringBuilder()
        builder.append(SpannableUtils.getAbsoluteSizeSpan(context, getString(R.string.statistic_last), 12))
                .append("\n")
                .append(if (lastByDay != 0) lastByDay.toString() + getString(R.string.ml) else getString(R.string.none))
        headerEndText.text = builder
    }

    companion object {

        fun newInstance(): TabletControlsFragment {
            val args = Bundle()
            val fragment = TabletControlsFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
