package com.github.vvinogra.tapmobiletest.ui.searchvideos

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.vvinogra.tapmobiletest.R
import com.github.vvinogra.tapmobiletest.databinding.FragmentSearchVideosBinding
import com.github.vvinogra.tapmobiletest.ui.searchvideos.adapter.VideoListAdapter
import com.github.vvinogra.tapmobiletest.ui.searchvideos.webviewparser.WebPageParser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SearchVideosFragment : Fragment(R.layout.fragment_search_videos) {

    private lateinit var videoListAdapter: VideoListAdapter

    @Inject
    lateinit var webPageParser: WebPageParser

    private val binding: FragmentSearchVideosBinding by viewBinding()
    private val viewModel: SearchVideosVM  by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoListAdapter = VideoListAdapter()

        setupViews()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(viewModel) {
                    launch { uiData.collect(::handleVideosData) }
                }
            }
        }
    }

    private fun setupViews() {
        with(binding) {
            swSearchVideos.onActionViewExpanded()
            swSearchVideos.clearFocus()
            swSearchVideos.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    swSearchVideos.clearFocus()
                    viewModel.onSearchQueryTextSubmit(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.onQueryTextChange(newText)
                    return true
                }
            })

            rvVideoList.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            rvVideoList.adapter = videoListAdapter
            rvVideoList.layoutManager = LinearLayoutManager(requireContext())

            rvVideoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (!recyclerView.canScrollVertically(1)) {
                        viewModel.onScrolledToBottom()
                    }
                }
            })

            // We need to use an invisible WebView in order to scrap web results
            webPageParser.setupWebView(
                webView = webView,
                onLoadResource =  {
                    viewModel.requestLoadedUrls()
                },
                onReceivedLoadedUrls = {
                    viewModel.onGetLoadedUrls(it)
                }
            )
        }
    }

    private fun handleVideosData(data: SearchVideosData) {
        binding.swSearchVideos.setQuery(data.searchQuery, false)

        videoListAdapter.setData(data.videosList)

        data.webViewEvent?.let {
            handleWebViewParserEvent(it)
        }
    }

    private fun handleWebViewParserEvent(event: WebViewParserEvent) {
        when (event) {
            WebViewParserEvent.FetchNewUrls -> {
                webPageParser.fetchNewUrls()
            }
            WebViewParserEvent.RequestLoadedUrls -> {
                webPageParser.requestLoadedUrls()
            }
            is WebViewParserEvent.StartSearch ->
                webPageParser.startSearch(event.query)
        }

        viewModel.onWebViewParserEventHandled()
    }
}