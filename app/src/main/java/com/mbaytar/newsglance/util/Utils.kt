package com.mbaytar.newsglance.util

import android.content.res.Configuration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class Utils {
    fun getScreenWidthFraction(fraction: Float, configuration: Configuration): Dp {
        val screenWidth = configuration.screenWidthDp.dp
        return screenWidth * fraction
    }

    fun getScreenHeightFraction(fraction: Float, configuration: Configuration): Dp {
        val screenHeight = configuration.screenHeightDp.dp
        return screenHeight * fraction
    }
}