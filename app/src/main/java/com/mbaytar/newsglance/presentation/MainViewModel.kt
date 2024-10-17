package com.mbaytar.newsglance.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbaytar.newsglance.util.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkObserver: NetworkObserver
) : ViewModel(){
    private val _isNetworkAvailable = MutableStateFlow(true)
    val isNetworkAvailable: StateFlow<Boolean> = _isNetworkAvailable

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
}