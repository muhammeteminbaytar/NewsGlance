package com.mbaytar.newsglance.data.remote.repository

import com.mbaytar.newsglance.data.remote.NewsAPI
import com.mbaytar.newsglance.data.remote.dto.NewsEverythingDto
import com.mbaytar.newsglance.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(private val api : NewsAPI): NewsRepository{
    override suspend fun getNewsEverything(): NewsEverythingDto {
        return api.getNewsEverything()
    }
}