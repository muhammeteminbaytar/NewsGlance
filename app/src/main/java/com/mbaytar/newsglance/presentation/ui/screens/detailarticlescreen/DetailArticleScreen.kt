package com.mbaytar.newsglance.presentation.ui.screens.detailarticlescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mbaytar.newsglance.presentation.ui.components.AppTopBar

@Composable
fun DetailArticleScreen(
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        AppTopBar(navController = navController)
    }) {
        Column(Modifier.padding(it)) {
            NewsDetail()
        }
    }
}

@Composable
fun NewsDetail() {

}