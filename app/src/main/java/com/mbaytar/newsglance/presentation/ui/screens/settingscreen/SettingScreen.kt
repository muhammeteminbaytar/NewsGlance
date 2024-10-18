package com.mbaytar.newsglance.presentation.ui.screens.settingscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun SettingScreen(navController: NavController) {
    Scaffold {
        Column(Modifier.padding(it)) {
            Text(text = "SettingScreen")
        }
    }
}