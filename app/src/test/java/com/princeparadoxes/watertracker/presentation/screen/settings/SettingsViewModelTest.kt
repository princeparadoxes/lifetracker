package com.princeparadoxes.watertracker.presentation.screen.settings

import android.text.Editable
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent
import com.princeparadoxes.watertracker.R
import com.princeparadoxes.watertracker.domain.entity.Gender
import com.princeparadoxes.watertracker.domain.interactor.settings.DayNormUseCase
import io.reactivex.Observable
import io.reactivex.Single
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class SettingsViewModelTest {

    @Mock
    lateinit var dayNormUseCase: DayNormUseCase
    @Mock
    lateinit var weightTextChanges: TextViewAfterTextChangeEvent
    @Mock
    lateinit var weightText: Editable

    private lateinit var settingsViewModel: SettingsViewModel

    @Before
    fun setupTest() {
        MockitoAnnotations.initMocks(this);
        settingsViewModel = SettingsViewModel(dayNormUseCase)
    }

    @Test
    fun observeCalculate() {
        Mockito.`when`(weightTextChanges.editable()).then { weightText }
        Mockito.`when`(weightText.toString()).then { "50" }
        Mockito.`when`(dayNormUseCase.calcDayNorm(Gender.FEMALE, 50F)).then { Single.just(1500) }
        val clicks = Observable.just(Any())
        val gender = Observable.just(R.id.settings_gender_group_female)
        val weight = Observable.just(weightTextChanges)
        val result = settingsViewModel.observeCalculate(clicks, gender, weight).blockingFirst()
        assertThat(result, `is`(1500))
    }

    @Test
    fun observeSave() {
    }

}