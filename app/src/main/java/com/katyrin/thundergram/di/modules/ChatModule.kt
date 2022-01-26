package com.katyrin.thundergram.di.modules

import androidx.lifecycle.ViewModel
import com.katyrin.thundergram.di.ViewModelKey
import com.katyrin.thundergram.model.mapping.MessageMapping
import com.katyrin.thundergram.model.mapping.MessageMappingImpl
import com.katyrin.thundergram.model.repository.ChatRepository
import com.katyrin.thundergram.model.repository.ChatRepositoryImpl
import com.katyrin.thundergram.view.chat.ChatFragment
import com.katyrin.thundergram.viewmodel.ChatViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
interface ChatModule {

    @ContributesAndroidInjector
    fun bindChatFragment(): ChatFragment

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    fun bindChatViewModel(viewModel: ChatViewModel): ViewModel

    @Binds
    @Singleton
    fun bindChatRepository(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository

    @Binds
    fun bindMessageMapping(messageMappingImpl: MessageMappingImpl): MessageMapping
}