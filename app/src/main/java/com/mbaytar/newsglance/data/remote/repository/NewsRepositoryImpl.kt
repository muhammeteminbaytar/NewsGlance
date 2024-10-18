package com.mbaytar.newsglance.data.remote.repository

import com.mbaytar.newsglance.data.local.NewsDao
import com.mbaytar.newsglance.data.remote.NewsAPI
import com.mbaytar.newsglance.data.remote.dto.NewsEverythingDto
import com.mbaytar.newsglance.data.remote.dto.toNewsEverythingList
import com.mbaytar.newsglance.domain.model.News
import com.mbaytar.newsglance.domain.model.NewsEntity
import com.mbaytar.newsglance.domain.model.toEntity
import com.mbaytar.newsglance.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsAPI, private val newsDao: NewsDao
) : NewsRepository {
    override suspend fun getNewsEverything(searchString: String): NewsEverythingDto {
        val localNews =
            api.getNewsEverything(searchString).toNewsEverythingList().map { it.toEntity() }
        saveNewsToLocal(localNews)

        return api.getNewsEverything(searchString)
    }

    override suspend fun saveNewsToLocal(newsList: List<NewsEntity>) {
        newsDao.insertNews(newsList)
    }

    override suspend fun getNewsFromLocal(): List<NewsEntity> {
        return newsDao.getAllNews()
    }
}