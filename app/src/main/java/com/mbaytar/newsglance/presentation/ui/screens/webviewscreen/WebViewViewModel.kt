package com.mbaytar.newsglance.presentation.ui.screens.webviewscreen

import androidx.lifecycle.ViewModel
import com.mbaytar.newsglance.domain.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor() : ViewModel() {
    private val _newsUrl = MutableStateFlow<String>("")
    val newsUrl: StateFlow<String> = _newsUrl

    fun setNewsItem(newsUrl: String) {
        _newsUrl.value = newsUrl
    }
}