package com.mbaytar.newsglance.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mbaytar.newsglance.R
import com.mbaytar.newsglance.presentation.ui.theme.ErrorRed
import com.mbaytar.newsglance.presentation.ui.theme.PrimaryColor

@Composable
fun AppBottomBar(navController: NavController, isNetworkConnect: Boolean) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Save")
    val icons = listOf(
        R.drawable.ic_house,
        R.drawable.ic_bookmark,
    )
    val iconsFill = listOf(
        R.drawable.ic_house_fill,
        R.drawable.ic_bookmark_fill
    )

    Box {
        Column(Modifier.background(Color.White)) {
            if (!isNetworkConnect)
                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .fillMaxWidth()
                        .background(ErrorRed)
                        .alpha(.4f)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(id = R.string.network_not_connection),
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.fillMaxSize(),
                        textAlign = TextAlign.Center)
                }
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .alpha(.4f)
            )
            NavigationBar(
                modifier = Modifier
                    .height(80.dp),
                containerColor = Color.Transparent

            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(items[selectedItem]) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.height(40.dp)
                            ) {

                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(id = if (selectedItem == index) iconsFill[index] else icons[index]),
                                    contentDescription = item,
                                    tint = if (selectedItem == index) Color.White else Color.Gray
                                )
                                if (selectedItem == index) {
                                    Text(
                                        text = item,
                                        color = Color.White,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryColor,
                            unselectedIconColor = Color.Gray,
                            selectedTextColor = Color.White,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = PrimaryColor.copy(alpha = 0.2f)
                        )
                    )

                }
            }

        }
    }
}