package com.mbaytar.newsglance.presentation.ui.screens.webviewscreen

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mbaytar.newsglance.presentation.ui.theme.LightGray
import com.mbaytar.newsglance.presentation.ui.theme.PrimaryColor
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun WebViewScreen(
    navController: NavController,
    viewModel: WebViewViewModel = hiltViewModel(),
    newsUrl: String
) {
    val decodedUrl = URLDecoder.decode(newsUrl, StandardCharsets.UTF_8.toString())

    LaunchedEffect(decodedUrl) {
        viewModel.setNewsItem(decodedUrl)
    }

    val currentUrl = viewModel.newsUrl.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value

    Scaffold(
        topBar = {
            BackButton(navController = navController)
        }
    ) {
        Column(Modifier.padding(it)) {
            if (currentUrl.isNotEmpty()) {
                Box(Modifier.fillMaxSize()) {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 12.dp),
                        factory = { context ->
                            WebView(context).apply {
                                webViewClient = object : WebViewClient() {
                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        viewModel.pageLoaded()
                                    }
                                }
                                settings.javaScriptEnabled = true
                                loadUrl(currentUrl)
                            }
                        }
                    )

                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center)
                        ) {
                            CircularProgressIndicator(color = PrimaryColor)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BackButton(navController: NavController) {
    Column {
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                Modifier
                    .clickable { navController.popBackStack() }
                    .clip(shape = CircleShape)
                    .background(LightGray)
                    .padding(12.dp)) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = Color.DarkGray
                )
            }
        }
    }
    
}