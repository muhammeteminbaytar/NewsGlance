package com.mbaytar.newsglance.presentation.ui.screens.homescreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.mbaytar.newsglance.R
import com.mbaytar.newsglance.domain.model.News
import com.mbaytar.newsglance.presentation.Screen
import com.mbaytar.newsglance.presentation.ui.components.AppTopBar
import com.mbaytar.newsglance.presentation.ui.theme.LightGray
import com.mbaytar.newsglance.presentation.ui.theme.PrimaryColor
import com.mbaytar.newsglance.util.components.ShimmerEffect

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        AppTopBar(navController = navController, actions = {
            SearchBox(onSearch = {
                viewModel.onEvent(NewsEvent.Search(it))
            })
        })
    }) {
        Column(Modifier.padding(it)) {
            NewsList(viewModel = viewModel, navController)
        }
    }
}

@Composable
fun SearchBox(onSearch: (String) -> Unit) {
    var searchText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text(text = "Search") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(56.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(
                    width = 2.dp,
                    color = if (isFocused) PrimaryColor else Color.Gray,
                    shape = RoundedCornerShape(24.dp)
                )
                .background(Color.White)
                .onFocusChanged { isFocused = it.isFocused },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search Icon",
                    tint = if (isFocused) PrimaryColor else Color.Gray
                )
            },
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                onSearch(searchText)
            })
        )
    }
}


@Composable
fun NewsList(viewModel: HomeScreenViewModel, navController: NavController) {
    val state = viewModel.state.value

    if (state.isLoading) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(5) {
                NewsItemShimmer()
            }
        }
    } else if (state.error.isNotEmpty()) {
        Text(text = state.error, modifier = Modifier.fillMaxSize())
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.news) { article ->
                NewsItem(article = article, navController)
            }
        }
    }
}

@Composable
fun NewsItem(article: News, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(120.dp)
            .clickable {
                navController.currentBackStackEntry?.arguments?.putParcelable("newsDetail", article)
                navController.navigate(Screen.DetailScreen.createRoute(article))
            }
    ) {
        Image(
            painter = if (article.urlToImage.isNotEmpty()) rememberAsyncImagePainter(article.urlToImage) else painterResource(
                id = R.drawable.news
            ),
            contentDescription = "",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,

            )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                modifier = Modifier.padding(top = 6.dp),
                text = article.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(Modifier.padding(bottom = 24.dp)) {
                Text(
                    text = article.author ?: "Unknown",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = article.publishedAt,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun NewsItemShimmer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(120.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray.copy(alpha = 0.5f))
        ) {
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ShimmerEffect(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            ShimmerEffect(
                modifier = Modifier
                    .height(14.dp)
                    .fillMaxWidth(0.6f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                ShimmerEffect(
                    modifier = Modifier
                        .height(14.dp)
                        .width(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                )
                Spacer(Modifier.weight(1f))
                ShimmerEffect(
                    modifier = Modifier
                        .height(14.dp)
                        .width(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                )
            }
        }
    }
}
