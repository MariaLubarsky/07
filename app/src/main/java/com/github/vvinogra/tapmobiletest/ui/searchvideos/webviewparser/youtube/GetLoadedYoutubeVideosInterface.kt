package com.github.vvinogra.tapmobiletest.ui.searchvideos.webviewparser.youtube

import android.webkit.JavascriptInterface
import timber.log.Timber

class GetLoadedYoutubeVideosInterface(private val action: (List<String>) -> Unit) {

    @JavascriptInterface
    fun execute(parsedUrls: String) {
        val urls = parsedUrls.split(',')

        Timber.d("urls.size = ${urls.size}")
        action(urls)
    }

    companion object {
        internal const val KEY = "PageParser"
        // Using regex to parse urls
        internal const val SCRIPT = "javascript:window.PageParser.execute(Array.from(document.querySelectorAll('a[class^=\"compact-media-item-image\"]')).toString())"
    }
}