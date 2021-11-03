package com.katyrin.thundergram.kaspresso.screens

import android.view.View
import com.katyrin.thundergram.R
import io.github.kakaocup.kakao.edit.KTextInputLayout
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.screen.Screen
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object ChatScreen : Screen<ChatScreen>() {

    val chatRecyclerView = KRecyclerView(
        builder = { withId(R.id.chat_recycler_view) },
        itemTypeBuilder = {
            itemType(::MyTextMessageItem)
            itemType(::UserTextMessageItem)
        }
    )

    val chatTextInputLayoutView = KTextInputLayout { withId(R.id.chat_text_input_layout) }

    class MyTextMessageItem(parent: Matcher<View>) : KRecyclerItem<MyTextMessageItem>(parent) {
        val userImageView = KImageView(parent) { withId(R.id.user_image_view) }
        val messageTextView = KTextView(parent) { withId(R.id.message_text_view) }
    }

    class UserTextMessageItem(parent: Matcher<View>) : KRecyclerItem<UserTextMessageItem>(parent) {
        val userImageView = KImageView(parent) { withId(R.id.user_image_view) }
        val messageTextView = KTextView(parent) { withId(R.id.message_text_view) }
    }
}