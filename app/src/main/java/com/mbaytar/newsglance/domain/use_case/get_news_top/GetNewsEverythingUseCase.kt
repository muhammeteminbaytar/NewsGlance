package com.mbaytar.newsglance.domain.use_case.get_news_top

import android.content.Context
import com.mbaytar.newsglance.data.remote.dto.toNewsEverythingList
import com.mbaytar.newsglance.domain.model.News
import com.mbaytar.newsglance.domain.model.toDomain
import com.mbaytar.newsglance.domain.repository.NewsRepository
import com.mbaytar.newsglance.util.NetworkUtils
import com.mbaytar.newsglance.util.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOError
import java.lang.Exception
import javax.inject.Inject

class GetNewsEverythingUseCase @Inject constructor(
    private val repository: NewsRepository,
    @ApplicationContext private val context: Context
) {

    fun executeGetNewsEverything(search: String, sortBy: String): Flow<Resource<List<News>>> = flow {
        try {
            emit(Resource.Loading())
            if (NetworkUtils.hasInternetConnection(context)) {
                val newsList = repository.getNewsEverything(search, sortBy)

                if (newsList.articles.isNotEmpty()) {
                    emit(Resource.Success(newsList.toNewsEverythingList()))
                } else {
                    emit(Resource.Error(message = "NoArticles"))
                }
            } else {
                val localNews = repository.getNewsFromLocal()
                val newsList = localNews.map { it.toDomain() }

                if (localNews.isNotEmpty()) {
                    emit(Resource.Success(newsList))
                } else {
                    emit(Resource.Error(message = "No data available offline"))
                }
            }

        } catch (e: IOError) {
            emit(Resource.Error(message = "No Internet"))
        } catch (e: Exception) {
            println(e.localizedMessage?.toString())
        }
    }
}