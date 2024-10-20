package com.mbaytar.newsglance.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.mbaytar.newsglance.R
import com.mbaytar.newsglance.data.local.PreferencesHelper
import com.mbaytar.newsglance.data.worker.NewsWorker
import com.mbaytar.newsglance.util.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkObserver: NetworkObserver,
    private val preferencesHelper: PreferencesHelper
) : ViewModel(){
    private val _isNetworkAvailable = MutableStateFlow(true)
    val isNetworkAvailable: StateFlow<Boolean> = _isNetworkAvailable

    val isShowOnBoard = preferencesHelper.getOnBoardScreenState()

    init {
        _isNetworkAvailable.value = networkObserver.isNetworkAvailable()

        observeNetworkChanges()
    }

    private fun observeNetworkChanges() {
        viewModelScope.launch {
            networkObserver.networkStatusFlow.collect { isAvailable ->
                _isNetworkAvailable.value = isAvailable
            }
        }
    }

    fun startNetworkMonitoring() {
        networkObserver.startNetworkCallback()
    }

    fun stopNetworkMonitoring() {
        networkObserver.stopNetworkCallback()
    }

    fun scheduleDreamWorkManagerProcess(context: Context) {
        val data = Data.Builder()
            .putString("notification_title", context.getString(R.string.notification_title))
            .build()

        val workRequest = OneTimeWorkRequestBuilder<NewsWorker>()
            .setInputData(data)
            .setInitialDelay(1, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }
}