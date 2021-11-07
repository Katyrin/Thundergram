package com.katyrin.thundergram.view.chat.adapter

import com.katyrin.thundergram.databinding.ItemUserTextMessageBinding
import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.utils.setChatIconFromUri

class UserTextMessageHolder(
    private val itemBinding: ItemUserTextMessageBinding,
    onPhoneNumberClick: (String) -> Unit,
    private val onSubscribeUser: (chatId: Long, userId: Long) -> Unit
) : BaseTextVewHolder(itemBinding.root, onPhoneNumberClick) {

    override fun bind(chatMessage: ChatMessage, position: Int): Unit =
        with(chatMessage as ChatMessage.Text) {
            itemBinding.messageTextView.text = message
            setSpannableString(itemBinding.messageTextView)
            itemBinding.userImageView.setChatIconFromUri(userPhotoPath)
            itemBinding.root.setOnClickListener { onSubscribeUser(chatId, userId) }
        }
}