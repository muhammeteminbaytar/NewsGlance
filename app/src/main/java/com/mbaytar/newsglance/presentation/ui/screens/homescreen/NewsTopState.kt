package com.mbaytar.newsglance.presentation.ui.screens.homescreen

import com.mbaytar.newsglance.domain.model.News

data class NewsTopState(
    val isLoading: Boolean = false,
    val newsTops: List<News> = emptyList(),
    val error: String = ""
)
