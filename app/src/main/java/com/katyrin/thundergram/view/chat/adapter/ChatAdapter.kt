package com.katyrin.thundergram.view.chat.adapter

import android.annotation.SuppressLint
import android.view.*
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.google.android.exoplayer2.ExoPlayer
import com.katyrin.thundergram.R
import com.katyrin.thundergram.databinding.*
import com.katyrin.thundergram.model.entities.ChatMessage
import kotlinx.coroutines.CoroutineScope

class ChatAdapter(
    private val exoPlayer: ExoPlayer,
    private val coroutineScope: CoroutineScope,
    private val onPhoneNumberClick: (String) -> Unit,
    private val onClickUser: (Long, Long) -> Unit,
    private val onUpdateSoundSpeed: () -> Unit,
    private val onClickPlayButton: (ChatMessage.Voice, Boolean) -> Unit,
    private val onUpdateSeekTo: (Long) -> Unit,
    private val onClickUserMenu: () -> Unit
) : ListAdapter<ChatMessage, BaseViewHolder>(DiffCallback()) {

    private var clickedView: View? = null
    private var clickedUserName: String? = null

    private class DiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        with(LayoutInflater.from(parent.context)) {
            when (viewType) {
                MY_MESSAGE -> MyTextMessageHolder(
                    ItemMyTextMessageBinding.inflate(this, parent, false),
                    onPhoneNumberClick
                ) { chatId, userId, view, name ->
                    clickedView = view
                    clickedUserName = name
                    onClickUser(chatId, userId)
                }
                USER_MESSAGE -> UserTextMessageHolder(
                    ItemUserTextMessageBinding.inflate(this, parent, false),
                    onPhoneNumberClick,
                ) { chatId, userId, view, name ->
                    clickedView = view
                    clickedUserName = name
                    onClickUser(chatId, userId)
                }
                MY_VOICE_MESSAGE -> MyVoiceMessageHolder(
                    coroutineScope,
                    exoPlayer,
                    ItemMyVoiceMessageBinding.inflate(this, parent, false),
                    onUpdateSoundSpeed,
                    onClickPlayButton,
                    onUpdateSeekTo
                ) { chatId, userId, view, name ->
                    clickedView = view
                    clickedUserName = name
                    onClickUser(chatId, userId)
                }
                USER_VOICE_MESSAGE -> UserVoiceMessageHolder(
                    coroutineScope,
                    exoPlayer,
                    ItemUserVoiceMessageBinding.inflate(this, parent, false),
                    onUpdateSoundSpeed,
                    onClickPlayButton,
                    onUpdateSeekTo
                ) { chatId, userId, view, name ->
                    clickedView = view
                    clickedUserName = name
                    onClickUser(chatId, userId)
                }
                MY_PHOTO_MESSAGE -> MyPhotoMessageHolder(
                    ItemMyPhotoMessageBinding.inflate(this, parent, false),
                    onPhoneNumberClick,
                ) { chatId, userId, view, name ->
                    clickedView = view
                    clickedUserName = name
                    onClickUser(chatId, userId)
                }
                else -> UserPhotoMessageHolder(
                    ItemUserPhotoMessageBinding.inflate(this, parent, false),
                    onPhoneNumberClick,
                ) { chatId, userId, view, name ->
                    clickedView = view
                    clickedUserName = name
                    onClickUser(chatId, userId)
                }
            }
        }

    @SuppressLint("RestrictedApi")
    fun onShowPopupMenu(isSubscribed: Boolean) {
        clickedView?.let { view ->
            val menuBuilder = MenuBuilder(view.context)
            menuBuilder.add(clickedUserName)
            MenuInflater(view.context).inflate(R.menu.user_menu, menuBuilder)
            if (isSubscribed) menuBuilder.removeItem(R.id.subscribe)
            else menuBuilder.removeItem(R.id.unsubscribe)
            val optionsMenu = MenuPopupHelper(view.context, menuBuilder, view)
            optionsMenu.setForceShowIcon(true)
            menuBuilder.setCallback(object : MenuBuilder.Callback {
                override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean =
                    when (item.itemId) {
                        R.id.subscribe -> {
                            onClickUserMenu()
                            true
                        }
                        R.id.unsubscribe -> {
                            onClickUserMenu()
                            true
                        }
                        else -> {
                            onClickUserMenu()
                            false
                        }
                    }

                override fun onMenuModeChange(menu: MenuBuilder) {}
            })
            optionsMenu.show()
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