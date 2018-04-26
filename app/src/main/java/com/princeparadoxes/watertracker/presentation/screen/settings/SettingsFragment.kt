package com.princeparadoxes.watertracker.presentation.screen.settings

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.princeparadoxes.watertracker.ProjectApplication
import com.princeparadoxes.watertracker.R
import com.princeparadoxes.watertracker.domain.entity.Gender
import com.princeparadoxes.watertracker.presentation.base.BaseFragment
import com.princeparadoxes.watertracker.presentation.utils.KeyboardUtils
import com.princeparadoxes.watertracker.safeSubscribe
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

    private val genderMaleCheck by lazy { view!!.findViewById(R.id.settings_gender_male_check) as View }
    private val genderMale by lazy { view!!.findViewById(R.id.settings_gender_male) as View }
    private val genderFemaleCheck by lazy { view!!.findViewById(R.id.settings_gender_female_check) as View }
    private val genderFemale by lazy { view!!.findViewById(R.id.settings_gender_female) as View }
    private val weightView by lazy { view!!.findViewById(R.id.settings_weight) as EditText }
    private val calculateButton by lazy { view!!.findViewById(R.id.settings_calculate) as View }
    private val dayNormView by lazy { view!!.findViewById(R.id.settings_day_norm) as EditText }
    private val saveButton by lazy { view!!.findViewById(R.id.settings_save) as View }

    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(view!!.parent as ViewGroup)!! }

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
                settingsViewModel.observeChangeGender(RxView.clicks(genderFemale), RxView.clicks(genderMale))
                        .safeSubscribe {
                            when(it){
                                Gender.MALE -> {
                                    genderMaleCheck.visibility = View.VISIBLE
                                    genderFemaleCheck.visibility = View.GONE
                                }
                                Gender.FEMALE -> {
                                    genderMaleCheck.visibility = View.GONE
                                    genderFemaleCheck.visibility = View.VISIBLE
                                }
                                Gender.NOT_SPECIFIED ->{
                                    genderMaleCheck.visibility = View.GONE
                                    genderFemaleCheck.visibility = View.GONE
                                }
                            }
                        },
                settingsViewModel.observeCalculate(RxView.clicks(calculateButton),
                        RxTextView.afterTextChangeEvents(weightView))
                        .safeSubscribe {
                            dayNormView.setText(it.toString())
                            KeyboardUtils.hideSoftKeyboard(activity)
                            dayNormView.requestFocus()
                        },
                settingsViewModel.observeSave(RxView.clicks(saveButton),
                        RxTextView.afterTextChangeEvents(dayNormView))
                        .safeSubscribe { bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED }
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
