package com.mbaytar.newsglance.presentation

import com.mbaytar.newsglance.domain.model.News

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("Home")
    data object SaveScreen : Screen("Save")

    data object DetailScreen : Screen("Detail") {
        fun createRoute(news: News) = "Detail"
    }

}