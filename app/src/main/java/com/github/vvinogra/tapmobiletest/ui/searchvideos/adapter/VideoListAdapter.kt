package com.github.vvinogra.tapmobiletest.ui.searchvideos.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class VideoListAdapter: RecyclerView.Adapter<VideoVH>() {
    private val _dataList = mutableListOf<VideoItemData>()

    fun setData(data: List<VideoItemData>) {
        val diffCallback: DiffUtil.Callback = VideoItemDataDiffCallback(data, _dataList)

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        updateDataList(data)
    }

    override fun onViewRecycled(holder: VideoVH) {
        super.onViewRecycled(holder)

        holder.onViewRecycled()
    }

    private fun updateDataList(data: List<VideoItemData>) {
        _dataList.clear()
        _dataList.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoVH {
        return VideoVH(parent)
    }

    override fun onBindViewHolder(holder: VideoVH, position: Int) {
        holder.apply(_dataList[position])
    }

    override fun getItemCount() = _dataList.size
}

data class VideoItemData(val videoUrl: String)