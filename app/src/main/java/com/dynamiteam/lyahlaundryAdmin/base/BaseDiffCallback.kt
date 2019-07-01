package com.dynamiteam.lyahlaundryAdmin.base

import androidx.recyclerview.widget.DiffUtil

class BaseDiffCallback<TData>(
    private val oldList: List<TData>, private val newList: List<TData>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[oldItemPosition]
    }

}