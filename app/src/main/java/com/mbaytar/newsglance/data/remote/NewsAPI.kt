package com.mbaytar.newsglance.data.remote

import com.mbaytar.newsglance.data.remote.dto.NewsTopDto
import com.mbaytar.newsglance.util.Constants.NEWS_API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("top-headlines")
    suspend fun getNewsTop(
        @Query("apiKey") apiKey : String = NEWS_API_KEY
    ) : NewsTopDto
}