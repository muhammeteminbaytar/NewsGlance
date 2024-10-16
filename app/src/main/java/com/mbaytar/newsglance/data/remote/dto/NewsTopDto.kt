package com.mbaytar.newsglance.data.remote.dto

import com.mbaytar.newsglance.domain.model.NewsTop

data class NewsTopDto(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)

fun NewsTopDto.toNewsTopList(): List<NewsTop> {
    return articles.map { article ->
        NewsTop(
            author = article.author,
            title = article.title,
            description = article.description,
            url = article.url,
            urlToImage = article.urlToImage,
            publishedAt = article.publishedAt,
            content = article.content,
            source = article.source
        )
    }
}