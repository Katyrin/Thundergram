package com.katyrin.thundergram.view.chat.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.katyrin.thundergram.model.entities.ChatMessage

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(chatMessage: ChatMessage, position: Int)
}