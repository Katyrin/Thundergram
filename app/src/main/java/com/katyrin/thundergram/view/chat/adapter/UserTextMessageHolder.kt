package com.katyrin.thundergram.view.chat.adapter

import com.katyrin.thundergram.databinding.ItemUserTextMessageBinding
import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.utils.setChatIconFromUri

class UserTextMessageHolder(
    private val itemBinding: ItemUserTextMessageBinding
) : BaseViewHolder(itemBinding.root) {
    override fun bind(chatMessage: ChatMessage, position: Int): Unit =
        with(chatMessage as ChatMessage.Text) {
            itemBinding.messageTextView.text = message
            itemBinding.userImageView.setChatIconFromUri(userPhotoPath)
        }
}