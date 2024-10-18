package com.mbaytar.newsglance.domain.use_case.get_news_from_database

import com.mbaytar.newsglance.domain.model.News
import com.mbaytar.newsglance.domain.model.NewsEntity
import com.mbaytar.newsglance.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsFromDatabaseUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(): List<NewsEntity> {
        return repository.getNewsFromLocal()
    }
}