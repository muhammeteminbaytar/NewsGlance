package com.mbaytar.newsglance.presentation

sealed class Screen(val route : String) {
    data object HomeScreen : Screen("Home")
    data object SaveScreen : Screen("Save")

    data object DetailScreen : Screen("Detail")

}