package com.github.vvinogra.tapmobiletest.ui.searchvideos.webviewparser

import android.webkit.WebView
import android.webkit.WebViewClient
import com.github.vvinogra.tapmobiletest.ui.searchvideos.webviewparser.youtube.GetLoadedYoutubeVideosInterface
import com.github.vvinogra.tapmobiletest.ui.searchvideos.webviewparser.youtube.YoutubeVideoLinksConstantsHelper
import javax.inject.Inject

class WebPageParser @Inject constructor(
    private val youtubeVideoLinksConstantsHelper: YoutubeVideoLinksConstantsHelper
) {

    private lateinit var webView: WebView

    fun setupWebView(
        webView: WebView,
        onLoadResource: () -> Unit,
        onReceivedLoadedUrls: (List<String>) -> Unit
    ) {
        this.webView = webView

        webView.settings.javaScriptEnabled = true

        webView.addJavascriptInterface(
            GetLoadedYoutubeVideosInterface {
                onReceivedLoadedUrls.invoke(it)
            },
            GetLoadedYoutubeVideosInterface.KEY
        )

        webView.webViewClient = object: WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
            }

            override fun onLoadResource(view: WebView?, url: String) {
                super.onLoadResource(view, url)
                onLoadResource.invoke()
            }
        }
    }

    fun startSearch(searchQuery: String) {
        webView.loadUrl(youtubeVideoLinksConstantsHelper.getYoutubeSearchByQueryLink(searchQuery))
    }

    fun fetchNewUrls() {
        webView.pageDown(true)
    }

    fun requestLoadedUrls() {
        webView.loadUrl(GetLoadedYoutubeVideosInterface.SCRIPT)
    }
}