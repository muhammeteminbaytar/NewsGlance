package com.mbaytar.newsglance.presentation.ui.screens.detailarticlescreen

import androidx.lifecycle.ViewModel
import com.mbaytar.newsglance.domain.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor() : ViewModel() {
    private val _newsItem = MutableStateFlow<News?>(null)
    val newsItem: StateFlow<News?> = _newsItem

    fun setNewsItem(news: News) {
        _newsItem.value = news
    }
}