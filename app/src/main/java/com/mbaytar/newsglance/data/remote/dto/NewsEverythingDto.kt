package com.mbaytar.newsglance.data.remote.dto

import com.mbaytar.newsglance.domain.model.News

data class NewsEverythingDto(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)

fun NewsEverythingDto.toNewsEverythingList(): List<News> {
    return articles.map { article ->
        News(
            author = article.author ?: "Unknown",
            content = article.content ?: "No content",
            description = article.description ?: "No description",
            publishedAt = article.publishedAt ?: "Unknown date",
            source = article.source ?: Source("Unknown", "Unknown"),
            title = article.title ?: "No title",
            url = article.url ?: "",
            urlToImage = article.urlToImage ?: ""
        )
    }
}