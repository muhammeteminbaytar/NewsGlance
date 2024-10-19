package com.mbaytar.newsglance.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mbaytar.newsglance.presentation.ui.components.AppBottomBar
import com.mbaytar.newsglance.presentation.ui.screens.detailarticlescreen.DetailArticleScreen
import com.mbaytar.newsglance.presentation.ui.screens.homescreen.HomeScreen
import com.mbaytar.newsglance.presentation.ui.screens.onboardingscreen.OnBoardingScreen
import com.mbaytar.newsglance.presentation.ui.screens.settingscreen.SettingScreen
import com.mbaytar.newsglance.presentation.ui.screens.webviewscreen.WebViewScreen
import com.mbaytar.newsglance.presentation.ui.theme.NewsGlanceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.startNetworkMonitoring()

        setContent {
            NewsGlanceTheme {
                val navController = rememberNavController()
                val isNetworkAvailable by mainViewModel.isNetworkAvailable.collectAsState()

                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination =
                    currentBackStackEntry?.destination?.route ?: Screen.HomeScreen.route

                Scaffold(
                    bottomBar = {
                        if (navController.currentDestination?.route != Screen.OnBoardingScreen.route) {
                            AppBottomBar(
                                navController = navController,
                                isNetworkAvailable
                            )
                        }
                    }) {
                    Column(Modifier.padding(it)) {
                        Content(navController = navController, mainViewModel)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        mainViewModel.stopNetworkMonitoring()
        super.onDestroy()
    }
}

@Composable
fun Content(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    NavHost(
        navController = navController,
        startDestination = if (viewModel.isShowOnBoard) Screen.HomeScreen.route else Screen.OnBoardingScreen.route
    ) {
        composable(route = Screen.HomeScreen.route) {
            HomeScreenContent(navController = navController)
        }
        composable(route = Screen.DetailScreen.route) {
            DetailArticleScreenContent(navController = navController)
        }
        composable(route = Screen.SaveScreen.route) {
            SettingScreenContent(navController = navController)
        }
        composable(route = Screen.OnBoardingScreen.route) {
            OnBoardingScreenContent(navController = navController)
        }
        composable(
            route = Screen.WebViewScreen.route,
            arguments = listOf(navArgument("newsUrl") { type = NavType.StringType })
        ) { backStackEntry ->
            val newsUrl = backStackEntry.arguments?.getString("newsUrl")
            if (newsUrl != null) {
                WebViewScreenContent(navController = navController, newsUrl = newsUrl)
            }
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
fun SettingScreenContent(navController: NavController) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SettingScreen(navController = navController)
    }
}

@Composable
fun OnBoardingScreenContent(navController: NavController) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OnBoardingScreen(navController = navController)
    }
}

@Composable
fun WebViewScreenContent(navController: NavController, newsUrl: String) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WebViewScreen(navController = navController, newsUrl = newsUrl)
    }
}