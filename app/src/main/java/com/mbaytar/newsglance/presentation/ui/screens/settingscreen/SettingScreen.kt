package com.mbaytar.newsglance.presentation.ui.screens.settingscreen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mbaytar.newsglance.R
import com.mbaytar.newsglance.presentation.Screen
import com.mbaytar.newsglance.util.Constants.ABOUT_URL
import com.mbaytar.newsglance.util.Constants.FAQ_URL
import com.mbaytar.newsglance.util.Constants.FEED_BACK_MAIL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingScreen(
    navController: NavController,
    viewModel: SettingScreenViewModel = hiltViewModel()
) {
    Scaffold {
        Column(Modifier.padding(it)) {
            Text(
                text = stringResource(id = R.string.settings),
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(16.dp)
            )

            val encodedAboutUrl = URLEncoder.encode(ABOUT_URL, StandardCharsets.UTF_8.toString())

            ListItem(
                modifier = Modifier.clickable {
                    navController.navigate(Screen.WebViewScreen.createRoute(encodedAboutUrl))
                },
                text = { Text(stringResource(id = R.string.about)) },
                icon = {
                    Icon(Icons.Default.Info, contentDescription = "About Icon")
                }
            )

            val encodedFaqUrl = URLEncoder.encode(FAQ_URL, StandardCharsets.UTF_8.toString())

            ListItem(
                modifier = Modifier.clickable {
                    navController.navigate(Screen.WebViewScreen.createRoute(encodedFaqUrl))
                },
                text = { Text(stringResource(id = R.string.faq)) },
                icon = {
                    Icon(Icons.Default.QuestionAnswer, contentDescription = "FAQ Icon")
                }
            )

            ListItem(
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:")
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(FEED_BACK_MAIL))
                        putExtra(Intent.EXTRA_SUBJECT, "Feedback")
                    }
                    navController.context.startActivity(intent)
                },
                text = { Text(stringResource(id = R.string.send_feedback)) },
                icon = {
                    Icon(Icons.Default.Email, contentDescription = "Feedback Icon")
                }
            )
        }
    }
}
