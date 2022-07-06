package com.github.vvinogra.tapmobiletest.ui.searchvideos.webviewparser.youtube

import android.net.Uri
import javax.inject.Inject

private const val PAGE_QUERY_PARAMETER_VIDEO_ID_KEY = "v"
private const val YOUTUBE_EMBEDDED_VIDEO_BASE_URL = "https://www.youtube.com/embed"
private const val YOUTUBE_SEARCH_BY_QUERY_BASE_URL = "https://m.youtube.com/results?search_query="
private const val YOUTUBE_SEARCH_URL_PART = "search"

class YoutubeVideoLinksConstantsHelper @Inject constructor() {

    fun convertYoutubePageLinkIntoEmbeddedVideoLink(pageLink: String): String? {
        return Uri.parse(pageLink).getQueryParameter(PAGE_QUERY_PARAMETER_VIDEO_ID_KEY)?.let {
            "$YOUTUBE_EMBEDDED_VIDEO_BASE_URL/$it"
        }
    }

    fun getYoutubeSearchByQueryLink(searchQuery: String): String {
        return YOUTUBE_SEARCH_BY_QUERY_BASE_URL + searchQuery
    }

    fun isYoutubeSearchUrl(url: String): Boolean {
        return url.contains(YOUTUBE_SEARCH_URL_PART)
    }
}