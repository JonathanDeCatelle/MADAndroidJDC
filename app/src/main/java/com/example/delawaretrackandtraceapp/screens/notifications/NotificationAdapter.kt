package com.example.delawaretrackandtraceapp.screens.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.delawaretrackandtraceapp.databinding.NotificationListItemBinding
import com.example.delawaretrackandtraceapp.domain.Notification

class NotificationAdapter(private val clickListener: NotificationListener, val viewModel: NotificationsViewModel) : ListAdapter<Notification, NotificationItemViewHolder>(NotificationDiffCallback()){
    override fun onBindViewHolder(holder: NotificationItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener, viewModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationItemViewHolder {
        return NotificationItemViewHolder.from(parent)
    }
}

class NotificationItemViewHolder(val binding: NotificationListItemBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(item: Notification, clickListener: NotificationListener, viewModel: NotificationsViewModel) {
        binding.notification = item


        //Tonen details order
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): NotificationItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = NotificationListItemBinding.inflate(layoutInflater, parent, false)
            return NotificationItemViewHolder(binding)
        }
    }
}

class NotificationDiffCallback: DiffUtil.ItemCallback<Notification>(){
    override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.notificationId == newItem.notificationId
    }

    override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem == newItem
    }
}

class NotificationListener(val clickListener: (notification: Notification)->Unit){
    fun onClick(notification: Notification) = clickListener(notification)
}