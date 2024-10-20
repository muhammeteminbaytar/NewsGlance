package com.mbaytar.newsglance.presentation.ui.screens.onboardingscreen

import androidx.lifecycle.ViewModel
import com.mbaytar.newsglance.data.local.PreferencesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingScreenViewModel @Inject constructor(private val preferencesHelper: PreferencesHelper) : ViewModel(){
    fun finishOnBoard() {
        preferencesHelper.saveOnBoardScreenState(true)
    }
}