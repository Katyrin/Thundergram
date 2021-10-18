package com.katyrin.thundergram.view.chatlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.katyrin.thundergram.databinding.ItemChatListBinding
import com.katyrin.thundergram.model.entities.ChatListItem
import com.katyrin.thundergram.utils.setChatIconFromUri

class ChatListAdapter : ListAdapter<ChatListItem, ChatListAdapter.ChatListHolder>(DiffCallback()) {

    private class DiffCallback : DiffUtil.ItemCallback<ChatListItem>() {
        override fun areItemsTheSame(oldItem: ChatListItem, newItem: ChatListItem): Boolean =
            oldItem.chatId == newItem.chatId

        override fun areContentsTheSame(oldItem: ChatListItem, newItem: ChatListItem): Boolean =
            oldItem == newItem
    }

    class ChatListHolder(
        private val itemBinding: ItemChatListBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(chatListItem: ChatListItem) {
            itemBinding.apply {
                chatName.text = chatListItem.chatName
                chatImage.setChatIconFromUri(chatListItem.chatImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListHolder =
        ChatListHolder(
            ItemChatListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ChatListHolder, position: Int) {
        holder.bind(currentList[position])
    }
}