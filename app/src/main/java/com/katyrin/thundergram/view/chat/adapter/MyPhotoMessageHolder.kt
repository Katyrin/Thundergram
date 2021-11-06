package com.katyrin.thundergram.view.chat.adapter

import com.katyrin.thundergram.databinding.ItemMyPhotoMessageBinding
import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.utils.getContentPhotoFromUri
import com.katyrin.thundergram.utils.setChatIconFromUri

class MyPhotoMessageHolder(
    private val itemBinding: ItemMyPhotoMessageBinding,
    onPhoneNumberClick: (String) -> Unit,
    private val onSubscribeUser: (chatId: Long, userId: Long) -> Unit
) : BaseTextVewHolder(itemBinding.root, onPhoneNumberClick) {

    override fun bind(chatMessage: ChatMessage, position: Int): Unit =
        with(chatMessage as ChatMessage.Photo) {
            itemBinding.messageTextView.text = message
            setSpannableString(itemBinding.messageTextView)
            itemBinding.userImageView.setChatIconFromUri(userPhotoPath)
            itemBinding.contentImageView.getContentPhotoFromUri(photoFilePath)
            itemBinding.root.setOnClickListener { onSubscribeUser(chatId, userId) }
        }
}