package com.mbaytar.newsglance.presentation.viewmodel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mbaytar.newsglance.data.local.PreferencesHelper
import com.mbaytar.newsglance.domain.model.News
import com.mbaytar.newsglance.domain.use_case.get_news_top.GetNewsEverythingUseCase
import com.mbaytar.newsglance.presentation.ui.screens.homescreen.HomeScreenViewModel
import com.mbaytar.newsglance.presentation.ui.screens.homescreen.NewsEvent
import com.mbaytar.newsglance.util.Resource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeScreenViewModel
    private val getNewsEverythingUseCase = mockk<GetNewsEverythingUseCase>(relaxed = true)
    private val preferencesHelper: PreferencesHelper = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        every { preferencesHelper.getLastSearchQuery() } returns "android"
        every { preferencesHelper.getSortOption() } returns "publishedAt"

        viewModel = HomeScreenViewModel(getNewsEverythingUseCase, preferencesHelper)
    }

    @Test
    fun `initial state is correct`() = runTest {
        val initialState = viewModel.state.value
        assertEquals(initialState.search, "android")
        assertEquals(initialState.news, emptyList<News>())
        assertFalse(initialState.isLoading)
        assertTrue(initialState.error.isEmpty())
    }

    @Test
    fun `on search event, state is updated with search results`() = runTest {
        val newsList = listOf(
            News("Author1", "Content1", "Description1", viewModel.formatDate("2024-10-18T22:31:43Z"), "Title1", "url1", "image1")
        )
        val flow = flow {
            emit(Resource.Loading())
            emit(Resource.Success(newsList))
        }

        coEvery { getNewsEverythingUseCase.executeGetNewsEverything("android", "publishedAt") } returns flow
        coEvery { preferencesHelper.saveLastSearchQuery(any()) } just Runs

        viewModel.onEvent(NewsEvent.Search("android"))

        assertEquals(viewModel.state.value.isLoading, false)
        advanceUntilIdle()

        val newState = viewModel.state.value
        assertEquals(newState.news, newsList)
        assertFalse(newState.isLoading)
        assertTrue(newState.error.isEmpty())
    }


    @Test
    fun `on refresh event, state is updated`() = runTest {
        val newsList = listOf(
            News("Author2", "Content2", "Description2", viewModel.formatDate("2024-10-18T22:31:43Z"), "Title2", "url2", "image2")
        )
        val flow = flow {
            emit(Resource.Loading())
            emit(Resource.Success(newsList))
        }

        coEvery { getNewsEverythingUseCase.executeGetNewsEverything("android", "publishedAt") } returns flow
        coEvery { preferencesHelper.saveLastSearchQuery(any()) } just Runs

        viewModel.onEvent(NewsEvent.Refresh)

        assertEquals(viewModel.state.value.isLoading, true)
        advanceUntilIdle()

        val newState = viewModel.state.value
        assertEquals(newState.news, newsList)
        assertFalse(newState.isLoading)
        assertTrue(newState.error.isEmpty())
    }


    @Test
    fun `when use case returns error, state is updated with error`() = runTest {
        val flow = flow {
            emit(Resource.Loading())
            emit(Resource.Error<List<News>>("Error"))
        }

        coEvery { getNewsEverythingUseCase.executeGetNewsEverything(any(), any()) } returns flow

        viewModel.onEvent(NewsEvent.Search("android"))

        advanceUntilIdle()

        val newState = viewModel.state.value
        assertTrue(newState.error.isNotEmpty())
        assertEquals(newState.error, "Error")
        assertFalse(newState.isLoading)
        assertTrue(newState.news.isEmpty())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
