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
        private const val KEY_SORT_OPTION = "key_sort_option"
        private const val ON_BOARD_SCREEN_SHOW = "on_board_screen_show"
    }

    fun saveLastSearchQuery(query: String) {
        sharedPreferences.edit().putString(KEY_SEARCH_QUERY, query).apply()
    }

    fun getLastSearchQuery(): String {
        return sharedPreferences.getString(KEY_SEARCH_QUERY, "") ?: ""
    }

    fun saveSortOption(option: String) {
        sharedPreferences.edit().putString(KEY_SORT_OPTION, option).apply()
    }

    fun getSortOption(): String {
        return sharedPreferences.getString(KEY_SORT_OPTION, "publishedAt") ?: "publishedAt"
    }

    fun saveOnBoardScreenState(isOnBoardShow : Boolean) {
        sharedPreferences.edit().putBoolean(ON_BOARD_SCREEN_SHOW, isOnBoardShow).apply()
    }

    fun getOnBoardScreenState(): Boolean {
        return sharedPreferences.getBoolean(ON_BOARD_SCREEN_SHOW, false)
    }
}