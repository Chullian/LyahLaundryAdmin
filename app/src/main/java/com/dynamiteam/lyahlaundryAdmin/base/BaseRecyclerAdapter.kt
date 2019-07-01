package com.dynamiteam.lyahlaundryAdmin.base

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface FoodBankViewholder<T> {
    fun bind(t: T)
}

interface OnItemClickedListener<T> {
    fun onClickedItem(item: T)
}

abstract class BaseRecyclerViewAdapter<TData, TViewHolder>(context: Context, data: List<TData> = listOf()) :
        RecyclerView.Adapter<TViewHolder>() where TViewHolder : RecyclerView.ViewHolder, TViewHolder : FoodBankViewholder<TData> {


    protected var onItemClickedListener: OnItemClickedListener<TData>? = null
    protected val appContext: Context = context
    protected val inflater: LayoutInflater = LayoutInflater.from(appContext)
    protected val data: MutableList<TData> = data.toMutableList()

    companion object {
        private const val FIRST_POSITION = 0
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TViewHolder, position: Int) = getItem(position).let { holder.bind(it) }

    @Throws(ArrayIndexOutOfBoundsException::class)
    fun getItem(position: Int) = data[position]

    fun add(element: TData) = data.add(element)

    fun add(oldPosition: Int, newPosition: Int) = data.add(newPosition, remove(oldPosition))

    operator fun set(position: Int, element: TData) = data.set(position, element)

    fun remove(element: TData) = data.remove(element)

    fun remove(position: Int) = data.removeAt(position)

    suspend fun updateListItems(newObjects: List<TData>) {
        createDiff(this@BaseRecyclerViewAdapter.data, newObjects)
    }

    private suspend fun createDiff(currentData: List<TData>, updatedData: List<TData>) {
        val diffCallback = BaseDiffCallback<TData>(currentData, updatedData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        withContext(Dispatchers.Main) {
            data.clear()
            diffResult.dispatchUpdatesTo(this@BaseRecyclerViewAdapter)
            data.addAll(updatedData)
        }
    }

    fun updateAllNotify(newObjects: List<TData>) {
        clear()
        addAll(newObjects)
        notifyDataSetChanged()
    }

    val all: List<TData>
        get() = data

    fun clear() {
        data.clear()
    }

    fun addAll(collection: Collection<TData>) = data.addAll(collection)

    val snapshot: List<TData>
        get() = data.toMutableList()

    fun getItemPosition(element: TData) = data.indexOf(element)

    fun insert(element: TData, position: Int) = data.add(position, element)

    fun insertFirstWithNotify(element: TData) {
        data.add(FIRST_POSITION, element)
        notifyItemInserted(FIRST_POSITION)
    }

    fun insertAll(element: Collection<TData>, position: Int) = data.addAll(position, element)

    fun isEmpty() = data.isEmpty()

    fun isNotEmpty() = data.isNotEmpty()

    fun setOnItemClickedlistener(onItemClickedListener: OnItemClickedListener<TData>) {
        this.onItemClickedListener = onItemClickedListener
    }

}