package com.mbaytar.newsglance.data.remote

import com.mbaytar.newsglance.data.remote.dto.NewsEverythingDto
import com.mbaytar.newsglance.util.Constants.NEWS_API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("everything")
    suspend fun getNewsEverything(
        @Query("q") country: String,
        @Query("apiKey") apiKey: String = NEWS_API_KEY
    ): NewsEverythingDto
}