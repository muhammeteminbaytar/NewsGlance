package com.mbaytar.newsglance.presentation.ui.screens.homescreen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
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
import com.mbaytar.newsglance.presentation.ui.theme.PrimaryColor
import com.mbaytar.newsglance.presentation.ui.theme.SecondaryColor
import com.mbaytar.newsglance.util.components.ShimmerEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val settingIsOpen = viewModel.sortIsOpen.collectAsState().value
    var hasPermission by remember { mutableStateOf(false) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasPermission = isGranted
    }

    LaunchedEffect(Unit) {
        hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

        if (!hasPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(topBar = {
        AppTopBar(navController = navController, actions = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SearchBox(viewModel, onSearch = {
                        viewModel.onEvent(NewsEvent.Search(it))
                    })
                    SortButton(sortClick = {
                        viewModel.setSortIsOpen(!settingIsOpen)
                    })
                }

            }

        })
    }) { padding ->
        Column(Modifier.padding(padding)) {
            NewsList(viewModel = viewModel, navController)
        }
    }

    if (settingIsOpen) {
        SortBottomSheet(viewModel = viewModel) {
            viewModel.setSortIsOpen(false)
        }
    }

}


@Composable
fun NewsCarousel(news: List<News>, navController: NavController) {
    if (news.isNotEmpty()) {
        var currentPage by remember { mutableIntStateOf(0) }
        val pagerState = rememberPagerState { news.size }
        val pageCount = news.size

        LaunchedEffect(key1 = currentPage) {
            while (true) {
                delay(3000L)
                currentPage = (pagerState.currentPage + 1) % pageCount
                if (pagerState.currentPage == pageCount - 1) {
                    pagerState.scrollToPage(0)
                } else {
                    pagerState.animateScrollToPage(currentPage)
                }
            }
        }

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                currentPage = page
            }
        }

        Column(
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
        ) {
            HorizontalPager(
                beyondViewportPageCount = pageCount,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) { page ->
                val article = news[page]
                Box(
                    modifier = Modifier
                        .clickable {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "newsDetail",
                                article
                            )
                            navController.navigate(Screen.DetailScreen.route)
                        }
                ) {
                    Image(
                        painter = if (article.urlToImage.isNotEmpty()) rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(article.urlToImage)
                                .apply {
                                    if (article.urlToImage.endsWith("svg", true)) {
                                        decoderFactory(SvgDecoder.Factory())
                                    }
                                }
                                .build(),
                            error = painterResource(id = R.drawable.news),
                            placeholder = painterResource(id = R.drawable.news)
                        ) else painterResource(
                            id = R.drawable.landscape_news
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = article.author ?: "Unknown",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = article.title,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 16.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = article.publishedAt,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pageCount) { page ->
                    val color =
                        if (page == pagerState.currentPage) PrimaryColor else Color.LightGray
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }
        }
    }
}


@Composable
fun SearchBox(viewModel: HomeScreenViewModel, onSearch: (String) -> Unit) {
    var searchText by remember { mutableStateOf(viewModel.state.value.search) }
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text(text = stringResource(id = R.string.search)) },
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
fun SortButton(sortClick: () -> Unit) {
    Row {
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            Modifier
                .clickable {
                    sortClick()
                }
                .clip(shape = CircleShape)
                .background(LightGray)
                .padding(12.dp)) {
            androidx.compose.material.Icon(
                painterResource(id = R.drawable.ic_filter),
                contentDescription = "",
                tint = Color.DarkGray
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    viewModel: HomeScreenViewModel,
    onDismiss: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val selectedOption by viewModel.selectedSortOption.collectAsState()
    var tempSelectedOption by remember { mutableStateOf(selectedOption) }

    ModalBottomSheet(
        onDismissRequest = {
            scope.launch {
                modalBottomSheetState.hide()
                onDismiss()
            }
        },
        sheetState = modalBottomSheetState,
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top
        ) {
            val options = listOf("publishedAt", "relevancy", "popularity")

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.sort_by),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 16.dp),
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = {
                        viewModel.updateSortOption(tempSelectedOption)
                        viewModel.onEvent(NewsEvent.Refresh)
                        scope.launch {
                            modalBottomSheetState.hide()
                            onDismiss()
                        }
                    }, Modifier.padding(end = 16.dp)) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = "")
                    }
                }

                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                tempSelectedOption = option
                            }
                            .padding(16.dp)
                    ) {
                        RadioButton(
                            selected = tempSelectedOption == option,
                            onClick = { tempSelectedOption = option },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = SecondaryColor,
                                unselectedColor = Color.Gray,
                            )
                        )
                        Text(text = option.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        }, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsList(viewModel: HomeScreenViewModel, navController: NavController) {
    val state = viewModel.state.value
    val refreshing = state.isLoading
    val refreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            viewModel.onEvent(NewsEvent.Refresh)
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(refreshState)
    ) {
        if (refreshing && state.news.isEmpty()) {
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
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.error),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(48.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    NewsCarousel(news = viewModel.state.value.news.let { list ->
                        if (list.size <= 5) list
                        else list.take(5)
                    }, navController = navController)
                }
                items(state.news) { article ->
                    NewsItem(article = article, navController)
                }
            }
        }

        PullRefreshIndicator(
            refreshing = refreshing,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
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
                navController.currentBackStackEntry?.savedStateHandle?.set("newsDetail", article)
                navController.navigate(Screen.DetailScreen.route)
            }
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(article.urlToImage)
                    .apply {
                        if (article.urlToImage.endsWith("svg", true)) {
                            decoderFactory(SvgDecoder.Factory())
                        }
                    }
                    .build(),
                error = painterResource(id = R.drawable.news),
                placeholder = painterResource(id = R.drawable.news)
            ),
            contentDescription = "",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
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
                val maxLength = 16
                val displayedAuthor = article.author.let {
                    if (it.length > maxLength) it.take(maxLength) + "..." else it
                } ?: "Unknown"

                Text(
                    text = displayedAuthor,
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
