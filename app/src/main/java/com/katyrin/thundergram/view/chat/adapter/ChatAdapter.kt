package com.katyrin.thundergram.view.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.google.android.exoplayer2.ExoPlayer
import com.katyrin.thundergram.databinding.*
import com.katyrin.thundergram.model.entities.ChatMessage
import kotlinx.coroutines.CoroutineScope

class ChatAdapter(
    private val exoPlayer: ExoPlayer,
    private val coroutineScope: CoroutineScope,
    private val onPhoneNumberClick: (String) -> Unit,
    private val onSubscribeUser: (Long, Long) -> Unit
) : ListAdapter<ChatMessage, BaseViewHolder>(DiffCallback()) {

    private class DiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        with(LayoutInflater.from(parent.context)) {
            when (viewType) {
                MY_MESSAGE ->
                    MyTextMessageHolder(
                        ItemMyTextMessageBinding.inflate(this, parent, false),
                        onPhoneNumberClick,
                        onSubscribeUser
                    )
                USER_MESSAGE ->
                    UserTextMessageHolder(
                        ItemUserTextMessageBinding.inflate(this, parent, false),
                        onPhoneNumberClick,
                        onSubscribeUser
                    )
                MY_VOICE_MESSAGE ->
                    MyVoiceMessageHolder(
                        coroutineScope,
                        exoPlayer,
                        ItemMyVoiceMessageBinding.inflate(this, parent, false),
                        onSubscribeUser
                    )
                USER_VOICE_MESSAGE ->
                    UserVoiceMessageHolder(
                        coroutineScope,
                        exoPlayer,
                        ItemUserVoiceMessageBinding.inflate(this, parent, false),
                        onSubscribeUser
                    )
                MY_PHOTO_MESSAGE ->
                    MyPhotoMessageHolder(
                        ItemMyPhotoMessageBinding.inflate(this, parent, false),
                        onPhoneNumberClick,
                        onSubscribeUser
                    )
                else ->
                    UserPhotoMessageHolder(
                        ItemUserPhotoMessageBinding.inflate(this, parent, false),
                        onPhoneNumberClick,
                        onSubscribeUser
                    )
            }
        }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(currentList[position], position)
    }

    override fun getItemViewType(position: Int): Int = when (val item = getItem(position)) {
        is ChatMessage.Text -> if (item.isMyMessage) MY_MESSAGE else USER_MESSAGE
        is ChatMessage.Voice -> if (item.isMyMessage) MY_VOICE_MESSAGE else USER_VOICE_MESSAGE
        is ChatMessage.Photo -> if (item.isMyMessage) MY_PHOTO_MESSAGE else USER_PHOTO_MESSAGE
    }

    private companion object {
        const val MY_MESSAGE = 1
        const val USER_MESSAGE = 2
        const val MY_VOICE_MESSAGE = 3
        const val USER_VOICE_MESSAGE = 4
        const val MY_PHOTO_MESSAGE = 5
        const val USER_PHOTO_MESSAGE = 6
    }
}