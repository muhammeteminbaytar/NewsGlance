package com.mbaytar.newsglance.domain.repository

import com.mbaytar.newsglance.data.remote.dto.NewsEverythingDto


interface NewsRepository {
    suspend fun getNewsEverything(searchString: String) : NewsEverythingDto
}