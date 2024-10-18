package com.mbaytar.newsglance.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mbaytar.newsglance.domain.model.NewsEntity

@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}