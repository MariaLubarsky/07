package com.github.vvinogra.tapmobiletest.ui.searchvideos.adapter

import androidx.recyclerview.widget.DiffUtil

class VideoItemDataDiffCallback(
    private val newList: List<VideoItemData>,
    private val oldList: List<VideoItemData>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.videoUrl == newItem.videoUrl
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}