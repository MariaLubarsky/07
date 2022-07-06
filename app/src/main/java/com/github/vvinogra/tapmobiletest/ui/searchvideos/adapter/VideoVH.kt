package com.github.vvinogra.tapmobiletest.ui.searchvideos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.vvinogra.tapmobiletest.R
import com.github.vvinogra.tapmobiletest.databinding.VhVideoBinding

class VideoVH(
    parent: ViewGroup
): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.vh_video, parent, false)
) {
    private val binding = VhVideoBinding.bind(itemView)

    fun onViewRecycled() {
        binding.webview.stopLoading()
        binding.webview.clearView()
    }

    fun apply(data: VideoItemData) {
        with(binding) {
            webview.settings.javaScriptEnabled = true
            webview.loadUrl(data.videoUrl)
        }
    }
}