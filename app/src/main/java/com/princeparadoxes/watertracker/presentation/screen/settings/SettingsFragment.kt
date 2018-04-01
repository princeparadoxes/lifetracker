package com.princeparadoxes.watertracker.presentation.screen.settings

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxRadioGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import com.princeparadoxes.watertracker.ProjectApplication
import com.princeparadoxes.watertracker.R
import com.princeparadoxes.watertracker.base.BaseFragment
import com.princeparadoxes.watertracker.utils.safeSubscribe
import javax.inject.Inject

class SettingsFragment : BaseFragment() {

    ///////////////////////////////////////////////////////////////////////////
    // INJECTS
    ///////////////////////////////////////////////////////////////////////////

    @Inject
    internal lateinit var settingsViewModel: SettingsViewModel

    ///////////////////////////////////////////////////////////////////////////
    // RESOURCES
    ///////////////////////////////////////////////////////////////////////////

    private val genderGroup by lazy { view!!.findViewById(R.id.settings_gender_group) as RadioGroup }
    private val weightView by lazy { view!!.findViewById(R.id.settings_weight) as EditText }
    private val calculateButton by lazy { view!!.findViewById(R.id.settings_calculate) as View }
    private val dayNormView by lazy { view!!.findViewById(R.id.settings_day_norm) as EditText }
    private val saveButton by lazy { view!!.findViewById(R.id.settings_save) as View }

    ///////////////////////////////////////////////////////////////////////////
    // INIT SCREEN
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerSettingsScope_Component.builder()
                .projectComponent(ProjectApplication.component())
                .module(SettingsScope.Module(this))
                .build()
                .inject(this)
    }

    override fun layoutId(): Int = R.layout.fragment_settings

    ///////////////////////////////////////////////////////////////////////////
    // START SCREEN
    ///////////////////////////////////////////////////////////////////////////

    override fun onStart() {
        super.onStart()
        unsubscribeOnStop(
                settingsViewModel.observeCalculate(RxView.clicks(calculateButton),
                        RxRadioGroup.checkedChanges(genderGroup),
                        RxTextView.afterTextChangeEvents(weightView))
                        .safeSubscribe { dayNormView.setText(it.toString()) },
                settingsViewModel.observeSave(RxView.clicks(saveButton),
                        RxTextView.afterTextChangeEvents(dayNormView))
                        .safeSubscribe {  }
        )
    }

    companion object {

        fun newInstance(): SettingsFragment {
            val args = Bundle()
            val fragment = SettingsFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
