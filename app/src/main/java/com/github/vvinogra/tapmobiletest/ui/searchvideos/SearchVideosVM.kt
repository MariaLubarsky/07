package com.github.vvinogra.tapmobiletest.ui.searchvideos

import androidx.lifecycle.ViewModel
import com.github.vvinogra.tapmobiletest.ui.searchvideos.adapter.VideoItemData
import com.github.vvinogra.tapmobiletest.ui.searchvideos.webviewparser.youtube.YoutubeVideoLinksConstantsHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchVideosVM @Inject constructor(
    private val youtubeVideoLinksConstantsHelper: YoutubeVideoLinksConstantsHelper
) : ViewModel() {

    private val _uiData = MutableStateFlow(provideInitialData())
    val uiData = _uiData.asStateFlow()

    fun onScrolledToBottom() {
        _uiData.update {
            it.copy(webViewEvent = WebViewParserEvent.FetchNewUrls)
        }
    }

    fun requestLoadedUrls() {
        _uiData.update {
            it.copy(webViewEvent = WebViewParserEvent.RequestLoadedUrls)
        }
    }

    fun onGetLoadedUrls(urls: List<String>) {
        val videosList = urls.mapNotNull { url ->
            val link = youtubeVideoLinksConstantsHelper
                .convertYoutubePageLinkIntoEmbeddedVideoLink(url)

            link?.let {
                VideoItemData(it)
            }
        }

        if (videosList.isEmpty()) {
            Timber.d("Ignoring. Videos list should not be empty")
            return
        }

        _uiData.update {
            it.copy(videosList = videosList)
        }
    }

    fun onWebViewParserEventHandled() {
        _uiData.update {
            it.copy(webViewEvent = null)
        }
    }

    fun onQueryTextChange(newQuery: String) {
        _uiData.update {
            it.copy(
                searchQuery = newQuery
            )
        }
    }

    fun onSearchQueryTextSubmit(newQuery: String) {
        _uiData.update {
            it.copy(
                searchQuery = newQuery,
                webViewEvent = WebViewParserEvent.StartSearch(newQuery)
            )
        }
    }

    private fun provideInitialData(): SearchVideosData {
        return SearchVideosData(
            searchQuery = "",
            videosList = listOf(),
            webViewEvent = null
        )
    }
}

data class SearchVideosData(
    val searchQuery: String,
    val videosList: List<VideoItemData>,
    val webViewEvent: WebViewParserEvent?
)

sealed class WebViewParserEvent {
    data class StartSearch(val query: String): WebViewParserEvent()
    object FetchNewUrls: WebViewParserEvent()
    object RequestLoadedUrls: WebViewParserEvent()
}