package com.katyrin.thundergram.view.chat.adapter

import com.katyrin.thundergram.databinding.ItemUserPhotoMessageBinding
import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.utils.getContentPhotoFromUri
import com.katyrin.thundergram.utils.setChatIconFromUri

class UserPhotoMessageHolder(
    private val itemBinding: ItemUserPhotoMessageBinding
) : BaseViewHolder(itemBinding.root) {
    override fun bind(chatMessage: ChatMessage, position: Int): Unit =
        with(chatMessage as ChatMessage.Photo) {
            itemBinding.messageTextView.text = message
            itemBinding.userImageView.setChatIconFromUri(userPhotoPath)
            itemBinding.contentImageView.getContentPhotoFromUri(photoFilePath)
        }
}