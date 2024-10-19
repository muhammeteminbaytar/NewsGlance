package com.mbaytar.newsglance.presentation.ui.screens.homescreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbaytar.newsglance.data.local.PreferencesHelper
import com.mbaytar.newsglance.domain.use_case.get_news_top.GetNewsEverythingUseCase
import com.mbaytar.newsglance.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getNewsEverythingUseCase: GetNewsEverythingUseCase,
    private val preferencesHelper: PreferencesHelper
) : ViewModel() {

    private val _state = mutableStateOf(NewsState())
    val state: State<NewsState> = _state

    private val _sortIsOpen = MutableStateFlow(false)
    val sortIsOpen: StateFlow<Boolean> = _sortIsOpen

    private val _selectedSortOption = MutableStateFlow(preferencesHelper.getSortOption())
    val selectedSortOption: StateFlow<String> = _selectedSortOption

    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    private var currentSearchQuery: String = ""


    init {
        var lastSearch = preferencesHelper.getLastSearchQuery()

        if (lastSearch.isNotEmpty()) {
            _state.value = _state.value.copy(search = lastSearch, error = "")
        } else {
            lastSearch = _state.value.search
        }
        getNews(lastSearch)
    }

    private var job: Job? = null
    private fun getNews(searchString: String) {
        currentSearchQuery = searchString
        job?.cancel()

        job = getNewsEverythingUseCase.executeGetNewsEverything(searchString, _selectedSortOption.value).onEach {
            when (it) {
                is Resource.Success -> {
                    val formattedNewsList = it.data?.map { article ->
                        article.copy(
                            publishedAt = formatDate(article.publishedAt)
                        )
                    } ?: emptyList()
                    _state.value = _state.value.copy(
                        news = formattedNewsList,
                        isLoading = false,
                        search = searchString,
                        error = ""
                    )
                    preferencesHelper.saveLastSearchQuery(searchString)
                }

                is Resource.Error -> {
                    _state.value =
                        _state.value.copy(error = it.message ?: "Error!", isLoading = false)
                }

                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: NewsEvent) {
        when (event) {
            is NewsEvent.Search -> {
                getNews(event.searchString)
            }

            NewsEvent.Refresh -> {
                refreshNews()
            }
        }
    }

    private fun refreshNews() {
        _state.value = _state.value.copy(isLoading = true, error = "")
        getNews(currentSearchQuery)
    }

    fun setSortIsOpen(isOpen: Boolean){
        _sortIsOpen.value = isOpen
    }

    fun updateSortOption(option: String) {
        _selectedSortOption.value = option
        preferencesHelper.saveSortOption(option)
    }

    private fun formatDate(isoDate: String?): String {
        return isoDate?.let {
            try {
                ZonedDateTime.parse(it).format(dateFormatter)
            } catch (e: Exception) {
                "Unknown date"
            }
        } ?: "Unknown date"
    }
}