package com.princeparadoxes.watertracker.presentation.screen.settings

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.Snackbar
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
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat


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
    private val policy by lazy { view!!.findViewById(R.id.fragment_settings_policy) as View }

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
        policy.setOnClickListener {
            openPrivacyPolicy()
        }

        unsubscribeOnStop(settingsViewModel.observeView(
                RxTextView.afterTextChangeEvents(weightView),
                RxTextView.afterTextChangeEvents(dayNormView),
                RxView.clicks(genderFemale),
                RxView.clicks(genderMale))
                .safeSubscribe {
                    if (weightView.text.isEmpty() && it.weight > 0F) {
                        weightView.setText(it.weight.toString())
                        weightView.setSelection(weightView.length())
                    }
                    if (dayNormView.text.isEmpty() && it.dayNorm > 0) {
                        dayNormView.setText(it.dayNorm.toString())
                        dayNormView.setSelection(dayNormView.length())
                    }
                    when (it.gender) {
                        Gender.MALE -> {
                            genderMaleCheck.visibility = View.VISIBLE
                            genderFemaleCheck.visibility = View.GONE
                        }
                        Gender.FEMALE -> {
                            genderMaleCheck.visibility = View.GONE
                            genderFemaleCheck.visibility = View.VISIBLE
                        }
                        Gender.NOT_SPECIFIED -> {
                            genderMaleCheck.visibility = View.GONE
                            genderFemaleCheck.visibility = View.GONE
                        }
                    }
                }
        )

        unsubscribeOnStop(settingsViewModel.observeCalculate(RxView.clicks(calculateButton))
                .safeSubscribe {
                    if (it > 0) {
                        dayNormView.setText(it.toString())
                        KeyboardUtils.hideSoftKeyboard(activity)
                        dayNormView.requestFocus()
                    } else {
                        weightView.requestFocus()
                        Snackbar.make(view!!, R.string.settings_weight_error, Snackbar.LENGTH_SHORT).show()
                    }
                })

        unsubscribeOnStop(settingsViewModel.observeSave(RxView.clicks(saveButton))
                .safeSubscribe { bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED })

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    KeyboardUtils.hideSoftKeyboard(activity)
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

    }

    private fun openPrivacyPolicy() {
        val customTabsIntent = CustomTabsIntent.Builder()
                .addDefaultShareMenuItem()
                .setToolbarColor(ContextCompat.getColor(context!!, R.color.accent))
                .setShowTitle(true)
                .build()
        customTabsIntent.launchUrl(context!!, Uri.parse("https://www.iubenda.com/privacy-policy/39232739"))
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
