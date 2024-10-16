package com.mbaytar.newsglance.presentation.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentScreen: String,
    navController: NavController,
    actions: @Composable (() -> Unit)? = null
) {
}