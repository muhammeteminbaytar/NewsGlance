package com.mbaytar.newsglance.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mbaytar.newsglance.domain.model.NewsEntity

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(newsList: List<NewsEntity>)

    @Query("SELECT * FROM news_table")
    suspend fun getAllNews(): List<NewsEntity>
}