package com.katyrin.thundergram.view.chatlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.katyrin.thundergram.R
import com.katyrin.thundergram.databinding.ItemChatListBinding
import com.katyrin.thundergram.model.entities.ChatListItem
import com.katyrin.thundergram.utils.checkNotification
import com.katyrin.thundergram.utils.setChatIconFromUri

class ChatListAdapter(
    private val onClickItem: (Long, String) -> Unit,
    private val onClickVolume: (Long, Boolean) -> Unit
) : ListAdapter<ChatListItem, ChatListAdapter.ChatListHolder>(DiffCallback()) {

    private class DiffCallback : DiffUtil.ItemCallback<ChatListItem>() {
        override fun areItemsTheSame(oldItem: ChatListItem, newItem: ChatListItem): Boolean =
            oldItem.chatId == newItem.chatId

        override fun areContentsTheSame(oldItem: ChatListItem, newItem: ChatListItem): Boolean =
            oldItem == newItem
    }

    inner class ChatListHolder(
        private val itemBinding: ItemChatListBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(chatListItem: ChatListItem): Unit = with(chatListItem) {
            itemBinding.apply {
                chatName.text = chatListItem.chatName
                chatImage.setChatIconFromUri(chatListItem.chatImage)
                if (isVolumeOn) setVolumeOnState(chatId) else setVolumeOffState(chatId)
                root.setOnClickListener { onClickItem(chatListItem.chatId, chatListItem.chatName) }
            }
        }

        private fun setVolumeOnState(chatId: Long) {
            itemBinding.volumeImageView.setImageResource(R.drawable.ic_volume_on)
            itemBinding.volumeImageView.setOnClickListener {
                onClickVolume(chatId, false)
                setVolumeOffState(chatId)
            }
        }

        private fun setVolumeOffState(chatId: Long) {
            itemBinding.volumeImageView.setImageResource(R.drawable.ic_volume_off)
            itemBinding.volumeImageView.setOnClickListener {
                (itemBinding.root.context as AppCompatActivity).checkNotification(itemBinding.root) {
                    onClickVolume(chatId, true)
                    setVolumeOnState(chatId)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListHolder =
        ChatListHolder(
            ItemChatListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ChatListHolder, position: Int): Unit =
        holder.bind(currentList[position])
}