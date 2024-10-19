package com.mbaytar.newsglance.presentation.ui.screens.onboardingscreen

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mbaytar.newsglance.R
import com.mbaytar.newsglance.presentation.Screen
import com.mbaytar.newsglance.presentation.ui.theme.OnBoardingPrimaryColor
import com.mbaytar.newsglance.presentation.ui.theme.PrimaryColor
import com.mbaytar.newsglance.util.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun OnBoardingScreen(
    viewModel: OnBoardingScreenViewModel = hiltViewModel(),
    navController: NavController
) {
    val pages = listOf(
        OnBoardingPage(
            R.drawable.newspaper,
            stringResource(R.string.onboarding_title_1),
            stringResource(R.string.onboarding_desc_1)
        ),
        OnBoardingPage(
            R.drawable.newsapp,
            stringResource(R.string.onboarding_title_2),
            stringResource(R.string.onboarding_desc_2)
        ),
        OnBoardingPage(
            R.drawable.readnews,
            stringResource(R.string.onboarding_title_3),
            stringResource(R.string.onboarding_desc_3)
        )
    )
    val pagerState = rememberPagerState { pages.size }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val utils = Utils()
    val navigationActions = OnBoardingNavigationActions(navController, pagerState, coroutineScope)

    Scaffold {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            PrimaryColor,
                            Color.White,
                            Color.White,
                        )
                    )
                )
        ) {

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                OnBoardingTopBar(
                    currentPage = pagerState.currentPage,
                    totalPages = pages.size,
                    onBack = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    onSkip = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pages.size - 1)
                        }
                    }
                )

                HorizontalPager(
                    beyondViewportPageCount = pages.size,
                    state = pagerState,
                    modifier = Modifier.weight(1f)
                ) { page ->
                    OnBoardingCard(page = pages[page], utils, configuration)
                }

                DotsIndicator(
                    totalDots = pages.size,
                    selectedIndex = pagerState.currentPage
                )

                OnBoardingBottomButton(
                    isLastPage = pagerState.currentPage == pages.size - 1,
                    navigationActions,
                    utils,
                    configuration,
                    viewModel
                )

                Spacer(
                    modifier = Modifier.height(
                        utils.getScreenHeightFraction(
                            0.03f,
                            configuration
                        )
                    )
                )
            }
        }
    }
}

@Composable
fun DotsIndicator(totalDots: Int, selectedIndex: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(totalDots) { index ->
            val color = if (index == selectedIndex) PrimaryColor else Color.LightGray
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .padding(4.dp)
                    .background(color = color, shape = CircleShape)
            )
        }
    }
}

@Composable
fun OnBoardingBottomButton(
    isLastPage: Boolean,
    navigationActions: NavigationActions,
    utils: Utils,
    configuration: Configuration,
    viewModel: OnBoardingScreenViewModel,
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = utils.getScreenWidthFraction(0.05f, configuration),
                end = utils.getScreenWidthFraction(0.05f, configuration),
                top = utils.getScreenHeightFraction(0.04f, configuration),
                bottom = utils.getScreenHeightFraction(0.04f, configuration)
            )
            .border(
                width = 2.dp,
                color = OnBoardingPrimaryColor,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Button(
            onClick = {
                if (isLastPage) {
                    navigationActions.onGetStartedClicked()
                    viewModel.finishOnBoard(context)
                } else {
                    navigationActions.onNextClicked()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = OnBoardingPrimaryColor,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .height(utils.getScreenHeightFraction(0.065f, configuration)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = if (isLastPage) stringResource(id = R.string.start_app) else stringResource(id = R.string.next),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun OnBoardingTopBar(
    currentPage: Int,
    totalPages: Int,
    onBack: () -> Unit,
    onSkip: () -> Unit
) {
    Row(Modifier.padding(start = 4.dp, end = 12.dp, top = 12.dp)) {
        if (currentPage > 0) {
            Button(
                onClick = {
                    onBack()
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    Icons.Filled.ArrowBackIosNew,
                    contentDescription = "",
                    tint = Color.White
                )
            }
        } else {
            Spacer(modifier = Modifier.size(40.dp))
        }

        Spacer(modifier = Modifier.weight(1f))
        if (currentPage != totalPages - 1)
            Button(
                onClick = {
                    onSkip()
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(30.dp)
                    .width(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(
                    start = 4.dp,
                    top = 4.dp,
                    end = 4.dp,
                    bottom = 4.dp,
                )
            ) {
                Text(
                    text = "Skip",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
    }
}

@Composable
fun OnBoardingCard(page: OnBoardingPage, utils: Utils, configuration: Configuration) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = page.imageRes), contentDescription = "",
                Modifier
                    .size(utils.getScreenHeightFraction(0.45f, configuration))
                    .padding(top = utils.getScreenHeightFraction(0.04f, configuration))
            )

            Text(
                text = page.title,
                modifier = Modifier
                    .padding(top = utils.getScreenHeightFraction(0.04f, configuration))
                    .fillMaxWidth(),
                fontSize = 34.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = page.description,
                modifier = Modifier
                    .padding(top = utils.getScreenHeightFraction(0.04f, configuration))
                    .fillMaxWidth(),
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

data class OnBoardingPage(val imageRes: Int, val title: String, val description: String)

class OnBoardingNavigationActions(
    private val navController: NavController,
    private val pagerState: PagerState,
    private val coroutineScope: CoroutineScope
) : NavigationActions {
    override fun onGetStartedClicked() {
        navController.navigate(Screen.HomeScreen.route)
    }

    override fun onNextClicked() {
        coroutineScope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }
}

interface NavigationActions {
    fun onGetStartedClicked()
    fun onNextClicked()
}