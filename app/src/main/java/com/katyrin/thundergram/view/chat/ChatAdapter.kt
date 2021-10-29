package com.katyrin.thundergram.view.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.katyrin.thundergram.databinding.ItemMySimpleMessageBinding
import com.katyrin.thundergram.model.entities.ChatMessage

class ChatAdapter : ListAdapter<ChatMessage, ChatAdapter.ChatMessageHolder>(DiffCallback()) {

    private class DiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
            oldItem == newItem
    }

    class ChatMessageHolder(
        private val itemBinding: ItemMySimpleMessageBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(chatMessage: ChatMessage): Unit = with(chatMessage) {
            itemBinding.messageTextView.text = message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageHolder =
        ChatMessageHolder(
            ItemMySimpleMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ChatMessageHolder, position: Int) {
        holder.bind(currentList[position])
    }
}