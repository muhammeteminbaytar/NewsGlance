package com.mbaytar.newsglance.presentation.viewmodel
import com.mbaytar.newsglance.data.local.PreferencesHelper
import com.mbaytar.newsglance.presentation.ui.screens.onboardingscreen.OnBoardingScreenViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
class OnBoardingScreenViewModelTest {

    private lateinit var viewModel: OnBoardingScreenViewModel
    private val preferencesHelper: PreferencesHelper = mockk(relaxed = true)

    @Before
    fun setup() {
        viewModel = OnBoardingScreenViewModel(preferencesHelper)
    }

    @Test
    fun `finishOnBoard saves onboarding state`() {
        coEvery { preferencesHelper.saveOnBoardScreenState(true) } just Runs

        viewModel.finishOnBoard()

        verify { preferencesHelper.saveOnBoardScreenState(true) }
    }
}