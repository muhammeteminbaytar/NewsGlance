package com.mbaytar.newsglance.data.di

import android.content.Context
import androidx.room.Room
import com.mbaytar.newsglance.data.local.NewsDao
import com.mbaytar.newsglance.data.local.NewsDatabase
import com.mbaytar.newsglance.data.local.PreferencesHelper
import com.mbaytar.newsglance.data.remote.NewsAPI
import com.mbaytar.newsglance.data.remote.repository.NewsRepositoryImpl
import com.mbaytar.newsglance.domain.repository.NewsRepository
import com.mbaytar.newsglance.util.Constants.BASE_URL
import com.mbaytar.newsglance.util.NetworkObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApi() : NewsAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideNetworkObserver(@ApplicationContext context: Context): NetworkObserver {
        return NetworkObserver(context)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(
        api: NewsAPI,
        newsDao: NewsDao
    ): NewsRepository {
        return NewsRepositoryImpl(api, newsDao)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            NewsDatabase::class.java,
            "news_database"
        ).build()
    }

    @Provides
    fun provideNewsDao(database: NewsDatabase): NewsDao {
        return database.newsDao()
    }

    @Provides
    @Singleton
    fun providePreferencesHelper(@ApplicationContext context: Context): PreferencesHelper {
        return PreferencesHelper(context)
    }
}