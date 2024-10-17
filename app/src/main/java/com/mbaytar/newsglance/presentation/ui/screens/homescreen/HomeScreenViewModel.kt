package com.mbaytar.newsglance.presentation.ui.screens.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbaytar.newsglance.domain.use_case.get_news_top.GetNewsEverythingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getNewsTopUseCase: GetNewsEverythingUseCase
) : ViewModel(){

    init {
        getNewsTop()
    }
    private fun getNewsTop(){
        getNewsTopUseCase.executeGetNewsTop().onEach {

        }.launchIn(viewModelScope)
    }
}