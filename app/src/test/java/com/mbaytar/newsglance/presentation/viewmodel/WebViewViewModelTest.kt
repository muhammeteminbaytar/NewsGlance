package com.mbaytar.newsglance.presentation.viewmodel

import com.mbaytar.newsglance.presentation.ui.screens.webviewscreen.WebViewViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WebViewViewModelTest {

    private lateinit var viewModel: WebViewViewModel

    @Before
    fun setup() {
        viewModel = WebViewViewModel()
    }

    @Test
    fun `setNewsItem updates newsUrl`() = runTest {
        val testUrl = "https://example.com"
        viewModel.setNewsItem(testUrl)

        val newsUrl = viewModel.newsUrl.first()
        assertEquals(testUrl, newsUrl)
    }

    @Test
    fun `pageLoaded sets isLoading to false`() = runTest {
        viewModel.pageLoaded()

        val isLoading = viewModel.isLoading.first()
        assertEquals(false, isLoading)
    }
}
