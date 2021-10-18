package com.katyrin.thundergram.view.chatlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.katyrin.thundergram.databinding.FragmentChatListBinding
import com.katyrin.thundergram.model.entities.ChatListItem
import com.katyrin.thundergram.view.BaseFragment
import com.katyrin.thundergram.viewmodel.ChatListViewModel
import javax.inject.Inject

class ChatListFragment : BaseFragment<FragmentChatListBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: ChatListViewModel by viewModels(factoryProducer = { factory })

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatListBinding = FragmentChatListBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveData.observe(viewLifecycleOwner, ::updateList)
        viewModel.updateList.observe(viewLifecycleOwner, ::updateList)
        viewModel.getChats()
        initViews()
    }

    private fun initViews() {
        binding?.recyclerChatList?.adapter = ChatListAdapter()
    }

    private fun updateList(chatList: List<ChatListItem>) {
        (binding?.recyclerChatList?.adapter as ChatListAdapter).submitList(chatList)
    }
}