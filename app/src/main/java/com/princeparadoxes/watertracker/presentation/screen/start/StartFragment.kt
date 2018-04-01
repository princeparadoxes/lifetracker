package com.princeparadoxes.watertracker.presentation.screen.start

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.transition.Fade
import android.widget.TextView
import com.princeparadoxes.watertracker.ProjectApplication
import com.princeparadoxes.watertracker.R
import com.princeparadoxes.watertracker.presentation.base.BaseFragment
import com.princeparadoxes.watertracker.presentation.base.FragmentSwitcherCompat
import com.princeparadoxes.watertracker.presentation.screen.settings.SettingsFragment
import com.princeparadoxes.watertracker.presentation.utils.AnimatorUtils

class StartFragment : BaseFragment() {

    private val mCenterText by lazy { view!!.findViewById(R.id.fragment_start_center_text) as TextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val component = DaggerStartScope_Component.builder()
                .projectComponent(ProjectApplication.component())
                .build()
        component.inject(this)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_start
    }

    override fun onResume() {
        super.onResume()
        mCenterText.setText(R.string.start_thanks)
        val animator = AnimatorUtils.createChangeTextAnimatorSet(
                mCenterText,
                true,
                R.string.start_you_are_beautiful,
                R.string.start_day_norm,
                R.string.start_empty)
        animator.startDelay = 1000
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                FragmentSwitcherCompat.start(fragmentManager)
                        .fragment(SettingsFragment.newInstance())
                        .containerId(R.id.main_start_fragment_container)
                        .enterTransition(Fade(Fade.IN))
                        .replace();
            }
        })
        animator.start()

    }

    companion object {

        fun newInstance(): StartFragment {
            val args = Bundle()
            val fragment = StartFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
