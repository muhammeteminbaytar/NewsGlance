package com.mbaytar.newsglance.data.remote.repository

import com.mbaytar.newsglance.data.remote.NewsAPI
import com.mbaytar.newsglance.data.remote.dto.NewsTopDto
import com.mbaytar.newsglance.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(private val api : NewsAPI): NewsRepository{
    override suspend fun getNewsTop(): NewsTopDto {
        return api.getNewsTop()
    }
}