package com.whyranoid.presentation.community.group.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whyranoid.domain.model.FinishNotification
import com.whyranoid.domain.model.GroupNotification
import com.whyranoid.domain.model.StartNotification
import com.whyranoid.presentation.databinding.MyFinishNotificationItemBinding
import com.whyranoid.presentation.databinding.MyStartNotificationItemBinding
import com.whyranoid.presentation.databinding.OtherFinishNotificationItemBinding
import com.whyranoid.presentation.databinding.OtherStartNotificationItemBinding

class GroupNotificationAdapter(private val myUid: String) :
    ListAdapter<GroupNotification, GroupNotificationAdapter.NotificationViewHolder>(
        notificationDiffUtil
    ) {

    companion object {
        private val notificationDiffUtil = object : DiffUtil.ItemCallback<GroupNotification>() {
            override fun areItemsTheSame(
                oldItem: GroupNotification,
                newItem: GroupNotification
            ): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: GroupNotification,
                newItem: GroupNotification
            ): Boolean =
                oldItem.uid == newItem.uid
        }

        const val MY_START_NOTIFICATION_TYPE = 0
        const val MY_FINISH_NOTIFICATION_TYPE = 1
        const val OTHER_START_NOTIFICATION_TYPE = 2
        const val OTHER_FINISH_NOTIFICATION_TYPE = 3
    }

    abstract class NotificationViewHolder(binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(notification: GroupNotification)
    }

    class MyStartNotificationViewHolder(private val binding: MyStartNotificationItemBinding) :
        NotificationViewHolder(binding) {

        override fun bind(notification: GroupNotification) {
            if (notification is StartNotification) {
                binding.notifications = notification
            }
        }
    }

    class MyFinishNotificationViewHolder(private val binding: MyFinishNotificationItemBinding) :
        NotificationViewHolder(binding) {

        override fun bind(notification: GroupNotification) {
            if (notification is FinishNotification) {
                binding.notifications = notification
            }
        }
    }

    class OtherStartNotificationViewHolder(private val binding: OtherStartNotificationItemBinding) :
        NotificationViewHolder(binding) {

        override fun bind(notification: GroupNotification) {
            if (notification is StartNotification) {
                binding.notifications = notification
            }
        }
    }

    class OtherFinishNotificationViewHolder(private val binding: OtherFinishNotificationItemBinding) :
        NotificationViewHolder(binding) {

        override fun bind(notification: GroupNotification) {
            if (notification is FinishNotification) {
                binding.notifications = notification
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val notification = getItem(position)) {
            is StartNotification -> {
                if (notification.uid == myUid) MY_START_NOTIFICATION_TYPE
                else OTHER_START_NOTIFICATION_TYPE
            }
            else -> {
                if (notification.uid == myUid) MY_FINISH_NOTIFICATION_TYPE
                else OTHER_FINISH_NOTIFICATION_TYPE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return when (viewType) {
            MY_START_NOTIFICATION_TYPE -> {
                val layoutInflater =
                    MyStartNotificationItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                MyStartNotificationViewHolder(layoutInflater)
            }
            MY_FINISH_NOTIFICATION_TYPE -> {
                val layoutInflater =
                    MyFinishNotificationItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                MyFinishNotificationViewHolder(layoutInflater)
            }
            OTHER_START_NOTIFICATION_TYPE -> {
                val layoutInflater =
                    OtherStartNotificationItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                OtherStartNotificationViewHolder(layoutInflater)
            }
            else -> {
                val layoutInflater =
                    OtherFinishNotificationItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                OtherFinishNotificationViewHolder(layoutInflater)
            }
        }
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
