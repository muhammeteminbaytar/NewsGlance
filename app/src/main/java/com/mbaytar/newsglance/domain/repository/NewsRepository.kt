package com.mbaytar.newsglance.domain.repository

import com.mbaytar.newsglance.data.remote.dto.NewsTopDto

interface NewsRepository {
    suspend fun getNewsTop() : NewsTopDto
}