package com.example.delawaretrackandtraceapp.screens.listoforder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.delawaretrackandtraceapp.R
import com.example.delawaretrackandtraceapp.databinding.HeaderOrderlistBinding
import com.example.delawaretrackandtraceapp.databinding.OrderListItemBinding
import com.example.delawaretrackandtraceapp.domain.Order
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class OrderListAdapter(private val clickListener: OrderListener, private val clickListenerSort: SortListener) : ListAdapter<DataItem, RecyclerView.ViewHolder>(OrderDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val orderItem = getItem(position) as DataItem.OrderItem
                holder.bind(orderItem.order, clickListener, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent, clickListenerSort)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    class TextViewHolder(val binding: HeaderOrderlistBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup, clickListener: SortListener): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HeaderOrderlistBinding.inflate(layoutInflater, parent, false)
                binding.clickListener = clickListener
                return TextViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.OrderItem -> ITEM_VIEW_TYPE_ITEM
            else -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun addHeaderAndSubmitList(list: List<Order>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.OrderItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }
}

class ViewHolder(val binding: OrderListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Order, clickListener: OrderListener, position: Int) {
        binding.order = item

        if (position % 2 != 0) binding.orderItem.setBackgroundColor(Color.rgb(239, 247, 255))

        binding.clickListener = clickListener
    }

    companion object {
        fun from(parent: ViewGroup): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = OrderListItemBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(binding)
        }
    }
}

class OrderDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

class OrderListener(val clickListener: (order: Order) -> Unit) {
    fun onClick(order: Order) = clickListener(order)
}

class SortListener(val clickListener: () -> Unit) {
    fun onClick() = clickListener()
}

sealed class DataItem {
    data class OrderItem(val order: Order) : DataItem() {
        override val id = order.orderId
    }

    object Header : DataItem() {
        override val id = "HEADER"
    }

    abstract val id: String
}