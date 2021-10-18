package com.katyrin.thundergram.di.modules

import androidx.lifecycle.ViewModel
import com.katyrin.thundergram.di.ViewModelKey
import com.katyrin.thundergram.model.repository.ChatListRepository
import com.katyrin.thundergram.model.repository.ChatListRepositoryImpl
import com.katyrin.thundergram.view.chatlist.ChatListFragment
import com.katyrin.thundergram.viewmodel.ChatListViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
interface ChatListModule {

    @ContributesAndroidInjector
    fun bindChatListFragment(): ChatListFragment

    @Binds
    @IntoMap
    @ViewModelKey(ChatListViewModel::class)
    fun bindChatListViewModel(viewModel: ChatListViewModel): ViewModel

    @Binds
    @Singleton
    fun bindChatListRepository(chatListRepositoryImpl: ChatListRepositoryImpl): ChatListRepository
}