package com.mbaytar.newsglance.domain.model

import com.mbaytar.newsglance.data.remote.dto.Source
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val title: String,
    val url: String,
    val urlToImage: String
) : Parcelable