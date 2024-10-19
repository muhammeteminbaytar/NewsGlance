package com.mbaytar.newsglance.presentation.ui.screens.detailarticlescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.mbaytar.newsglance.R
import com.mbaytar.newsglance.domain.model.News
import com.mbaytar.newsglance.presentation.Screen
import com.mbaytar.newsglance.presentation.ui.components.AppTopBar
import com.mbaytar.newsglance.presentation.ui.theme.LightGray
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun DetailArticleScreen(
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel()
) {

    val newsItem = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<News>("newsDetail")

    LaunchedEffect(newsItem) {
        newsItem?.let {
            viewModel.setNewsItem(it)
        }
    }


    Scaffold(topBar = {
        AppTopBar(navController = navController, navigation = {
            BackButton(navController)
        }, actions = {
            OpenWebSite(navController, viewModel)
        })
    }) {
        Column(Modifier.padding(it)) {
            NewsDetail(viewModel)
        }
    }
}

@Composable
fun BackButton(navController: NavController) {
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


@Composable
fun OpenWebSite(navController: NavController, viewModel: DetailViewModel) {
    val newsUrl = viewModel.newsItem.collectAsState().value?.url ?: ""

    val encodedUrl = URLEncoder.encode(newsUrl, StandardCharsets.UTF_8.toString())

    Box(
        Modifier
            .clickable { navController.navigate(Screen.WebViewScreen.createRoute(encodedUrl)) }
            .clip(shape = CircleShape)
            .background(LightGray)
            .padding(12.dp)) {
        Icon(
            imageVector = Icons.Filled.Newspaper,
            contentDescription = "Open Web Site",
            tint = Color.DarkGray
        )
    }
    Spacer(modifier = Modifier.width(12.dp))
}

@Composable
fun NewsDetail(viewModel: DetailViewModel) {
    val news = viewModel.newsItem.collectAsState()

    Column {
        news.value?.let { newsItem ->
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .clip(shape = RoundedCornerShape(12.dp)),
                painter = if (newsItem.urlToImage.isNotEmpty()) rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(newsItem.urlToImage)
                        .apply {
                            if (newsItem.urlToImage.endsWith("svg", true)) {
                                decoderFactory(SvgDecoder.Factory())
                            }
                        }
                        .build(),
                    error = painterResource(id = R.drawable.news),
                    placeholder = painterResource(id = R.drawable.news)
                ) else painterResource(
                    id = R.drawable.landscape_news
                ),
                contentDescription = "",
                contentScale = ContentScale.Crop,
            )
        }

        Column {
            news.value?.title?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Row(modifier = Modifier.padding(12.dp)) {
                news.value?.author?.let { Text(text = it, Modifier.padding(end = 8.dp)) }
                Box(
                    modifier = Modifier
                        .background(Color.DarkGray, shape = CircleShape)
                        .size(8.dp)
                        .align(Alignment.CenterVertically)
                )
                news.value?.publishedAt?.let { Text(text = it, Modifier.padding(start = 8.dp)) }
            }
            news.value?.description?.let {
                Text(
                    text = it,
                    Modifier.padding(start = 8.dp, top = 12.dp)
                )
            }
        }

    }

}