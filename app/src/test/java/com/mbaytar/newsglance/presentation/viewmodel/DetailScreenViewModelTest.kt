package com.mbaytar.newsglance.presentation.viewmodel

import com.mbaytar.newsglance.domain.model.News
import com.mbaytar.newsglance.presentation.ui.screens.detailarticlescreen.DetailViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class DetailViewModelTest {

    private lateinit var viewModel: DetailViewModel

    @Before
    fun setUp() {
        viewModel = DetailViewModel()
    }

    @Test
    fun `setNewsItem updates newsItem state`() = runTest {
        val testNews = News("Author", "Content", "Description", "2024-10-20", "Title", "url", "imageUrl")

        viewModel.setNewsItem(testNews)

        val newsItem = viewModel.newsItem.first()
        assertEquals(testNews, newsItem)
    }
}