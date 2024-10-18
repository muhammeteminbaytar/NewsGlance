package com.mbaytar.newsglance.presentation.ui.screens.homescreen

sealed class NewsEvent {
    data class Search(val searchString : String) : NewsEvent()
    data object Refresh : NewsEvent()
}