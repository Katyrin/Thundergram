package com.katyrin.thundergram.view.chat.adapter

import android.view.View
import com.katyrin.thundergram.databinding.ItemMyTextMessageBinding
import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.utils.setChatIconFromUri

class MyTextMessageHolder(
    private val itemBinding: ItemMyTextMessageBinding,
    onPhoneNumberClick: (String) -> Unit,
    private val onSubscribeUser: (chatId: Long, userId: Long, view: View, name: String) -> Unit
) : BaseTextVewHolder(itemBinding.root, onPhoneNumberClick) {

    override fun bind(chatMessage: ChatMessage, position: Int): Unit =
        with(chatMessage as ChatMessage.Text) {
            itemBinding.userName.text = chatMessage.userName
            itemBinding.messageTextView.text = message
            setSpannableString(itemBinding.messageTextView)
            itemBinding.userImageView.setChatIconFromUri(userPhotoPath)
            itemBinding.root.setOnClickListener {
                onSubscribeUser(chatId, userId, itemBinding.userImageView, userName)
            }
        }
}