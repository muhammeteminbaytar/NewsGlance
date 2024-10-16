package com.mbaytar.newsglance.domain.use_case.get_news_top

import com.mbaytar.newsglance.data.remote.dto.toNewsTopList
import com.mbaytar.newsglance.domain.model.NewsTop
import com.mbaytar.newsglance.domain.repository.NewsRepository
import com.mbaytar.newsglance.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOError
import javax.inject.Inject

class GetNewsTopUseCase @Inject constructor(private val repository: NewsRepository) {

    fun executeGetNewsTop(): Flow<Resource<List<NewsTop>>> = flow {
        try {
            emit(Resource.Loading())
            val newsList = repository.getNewsTop()

            if (newsList.articles.isNotEmpty()) {
                emit(Resource.Success(newsList.toNewsTopList()))
            } else {
                emit(Resource.Error(message = "NoArticles"))
            }
        } catch (e: IOError) {
            emit(Resource.Error(message = "No Internet"))
        }
    }
}