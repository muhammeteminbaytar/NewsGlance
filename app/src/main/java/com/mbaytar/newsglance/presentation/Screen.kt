package com.mbaytar.newsglance.presentation

sealed class Screen(val route : String) {
    data object HomeScreen : Screen("Home")
}