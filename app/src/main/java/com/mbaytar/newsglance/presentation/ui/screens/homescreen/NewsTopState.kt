package com.mbaytar.newsglance.presentation.ui.screens.homescreen

import com.mbaytar.newsglance.domain.model.News

data class NewsState(
    val isLoading: Boolean = false,
    val news: List<News> = emptyList(),
    val error: String = "",
    val search: String = "android"
)
