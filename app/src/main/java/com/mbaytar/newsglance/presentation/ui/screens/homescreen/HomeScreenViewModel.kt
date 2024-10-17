package com.mbaytar.newsglance.presentation.ui.screens.homescreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbaytar.newsglance.domain.use_case.get_news_top.GetNewsEverythingUseCase
import com.mbaytar.newsglance.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getNewsTopUseCase: GetNewsEverythingUseCase
) : ViewModel() {

    private val _state = mutableStateOf(NewsState())
    val state: State<NewsState> = _state

    private val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    init {
        getNews(_state.value.search)
    }

    private var job: Job? = null
    private fun getNews(searchString: String) {
        job?.cancel()

        job = getNewsTopUseCase.executeGetNewsTop(searchString).onEach {
            when (it) {
                is Resource.Success -> {
                    val formattedNewsList = it.data?.map { article ->
                        article.copy(
                            publishedAt = formatDate(article.publishedAt)
                        )
                    } ?: emptyList()
                    _state.value = NewsState(news = formattedNewsList)
                }

                is Resource.Error -> {
                    _state.value = NewsState(error = it.message ?: "Error!")
                }

                is Resource.Loading -> {
                    _state.value = NewsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: NewsEvent) {
        when(event) {
            is NewsEvent.Search -> {
                getNews(event.searchString)
            }
        }
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