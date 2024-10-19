package com.mbaytar.newsglance.presentation

import com.mbaytar.newsglance.domain.model.News

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("Home")
    data object SaveScreen : Screen("Setting")
    data object OnBoardingScreen : Screen("OnBoard")
    data object WebViewScreen : Screen("WebView/{newsUrl}") {
        fun createRoute(newsUrl: String) = "WebView/$newsUrl"
    }

    data object DetailScreen : Screen("Detail")

}