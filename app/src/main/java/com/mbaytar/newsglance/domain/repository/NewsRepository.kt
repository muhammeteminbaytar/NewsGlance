package com.mbaytar.newsglance.domain.repository

import com.mbaytar.newsglance.data.remote.dto.NewsEverythingDto
import com.mbaytar.newsglance.domain.model.News
import com.mbaytar.newsglance.domain.model.NewsEntity


interface NewsRepository {
    suspend fun getNewsEverything(searchString: String) : NewsEverythingDto
    suspend fun saveNewsToLocal(newsList: List<NewsEntity>)
    suspend fun getNewsFromLocal(): List<NewsEntity>
}