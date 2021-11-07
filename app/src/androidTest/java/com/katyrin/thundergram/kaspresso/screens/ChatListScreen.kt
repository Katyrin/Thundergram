package com.katyrin.thundergram.kaspresso.screens

import android.view.View
import com.katyrin.thundergram.R
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.screen.Screen
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object ChatListScreen : Screen<ChatListScreen>() {

    val recyclerChatListView = KRecyclerView(
        builder = { withId(R.id.recycler_chat_list) },
        itemTypeBuilder = {
            itemType(::ChatItem)
        }
    )

    class ChatItem(parent: Matcher<View>) : KRecyclerItem<ChatItem>(parent) {
        val chatImageView = KImageView(parent) { withId(R.id.chat_image) }
        val chatNameView = KTextView(parent) { withId(R.id.chat_name) }
    }
}