package com.mbaytar.newsglance.domain.model

import com.mbaytar.newsglance.data.remote.dto.Source

data class News(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
)