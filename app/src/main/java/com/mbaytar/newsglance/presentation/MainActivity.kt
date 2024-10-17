package com.mbaytar.newsglance.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mbaytar.newsglance.presentation.ui.components.AppBottomBar
import com.mbaytar.newsglance.presentation.ui.components.AppTopBar
import com.mbaytar.newsglance.presentation.ui.screens.detailarticlescreen.DetailArticleScreen
import com.mbaytar.newsglance.presentation.ui.screens.homescreen.HomeScreen
import com.mbaytar.newsglance.presentation.ui.screens.savescreen.SaveScreen
import com.mbaytar.newsglance.presentation.ui.theme.NewsGlanceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsGlanceTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = { AppBottomBar(navController = navController) }) {
                    Column(Modifier.padding(it)) {
                        Content(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun Content(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(route = Screen.HomeScreen.route) {
            HomeScreenContent(navController = navController)
        }
        composable(route = Screen.DetailScreen.route) {
            DetailArticleScreenContent(navController = navController)
        }
        composable(route = Screen.SaveScreen.route) {
            SaveScreenContent(navController = navController)
        }
    }
}

@Composable
fun HomeScreenContent(navController: NavController) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeScreen(navController = navController)
    }
}

@Composable
fun DetailArticleScreenContent(navController: NavController) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DetailArticleScreen(navController = navController)
    }
}

@Composable
fun SaveScreenContent(navController: NavController) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SaveScreen(navController = navController)
    }
}