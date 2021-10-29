package com.katyrin.thundergram.view.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.katyrin.thundergram.databinding.FragmentChatBinding
import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.utils.toast
import com.katyrin.thundergram.view.BaseFragment
import com.katyrin.thundergram.viewmodel.ChatViewModel
import com.katyrin.thundergram.viewmodel.appstates.ChatState
import javax.inject.Inject

class ChatFragment : BaseFragment<FragmentChatBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: ChatViewModel by viewModels(factoryProducer = { factory })

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatBinding = FragmentChatBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveData.observe(viewLifecycleOwner, ::renderData)
        viewModel.getMessages(ChatFragmentArgs.fromBundle(requireArguments()).chatId)
//        viewModel.getNewMessage(ChatFragmentArgs.fromBundle(requireArguments()).chatId)
//            .observe(viewLifecycleOwner, ::updateList)
        initViews()
    }

    private fun initViews() {
        binding?.chatRecyclerView?.adapter = ChatAdapter()
    }

    private fun renderData(chatState: ChatState) {
        when (chatState) {
            is ChatState.Error -> chatState.message?.let { toast(it) }
            is ChatState.Success -> updateList(chatState.chatMessageList)
        }
    }

    private fun updateList(chatList: List<ChatMessage>) {
        (binding?.chatRecyclerView?.adapter as ChatAdapter).submitList(chatList)
    }
}