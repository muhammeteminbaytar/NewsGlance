package com.mbaytar.newsglance.domain.use_case.get_news_top

import com.mbaytar.newsglance.data.remote.dto.toNewsEverythingList
import com.mbaytar.newsglance.domain.model.News
import com.mbaytar.newsglance.domain.repository.NewsRepository
import com.mbaytar.newsglance.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOError
import java.lang.Exception
import javax.inject.Inject

class GetNewsEverythingUseCase @Inject constructor(private val repository: NewsRepository) {

    fun executeGetNewsTop(): Flow<Resource<List<News>>> = flow {
        try {
            emit(Resource.Loading())
            val newsList = repository.getNewsEverything()

            if (newsList.articles.isNotEmpty()) {
                emit(Resource.Success(newsList.toNewsEverythingList()))
            } else {
                emit(Resource.Error(message = "NoArticles"))
            }
        } catch (e: IOError) {
            emit(Resource.Error(message = "No Internet"))
        } catch (e: Exception) {
            println(e.localizedMessage?.toString())
        }
    }
}