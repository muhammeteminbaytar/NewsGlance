package com.mbaytar.newsglance.data.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelper @Inject constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "news_prefs"
        private const val KEY_SEARCH_QUERY = "key_search_query"
    }

    fun saveLastSearchQuery(query: String) {
        sharedPreferences.edit().putString(KEY_SEARCH_QUERY, query).apply()
    }

    fun getLastSearchQuery(): String {
        return sharedPreferences.getString(KEY_SEARCH_QUERY, "") ?: ""
    }
}