package com.katyrin.thundergram.view.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.katyrin.thundergram.databinding.FragmentChatBinding
import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.utils.hideKeyboard
import com.katyrin.thundergram.utils.toast
import com.katyrin.thundergram.view.BaseFragment
import com.katyrin.thundergram.view.chat.adapter.ChatAdapter
import com.katyrin.thundergram.viewmodel.ChatViewModel
import com.katyrin.thundergram.viewmodel.appstates.ChatState
import javax.inject.Inject
import com.google.android.exoplayer2.SimpleExoPlayer


class ChatFragment : BaseFragment<FragmentChatBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: ChatViewModel by viewModels(factoryProducer = { factory })

    @Inject
    lateinit var simpleExoPlayer: SimpleExoPlayer

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatBinding = FragmentChatBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.liveData.observe(viewLifecycleOwner, ::renderData)
        viewModel.getMessages(getChatId())
        viewModel.getNewMessage(getChatId()).observe(viewLifecycleOwner, ::updateList)
    }

    private fun getChatId(): Long = ChatFragmentArgs.fromBundle(requireArguments()).chatId

    private fun initViews() {
        binding?.apply {
            chatRecyclerView.adapter = ChatAdapter(simpleExoPlayer, lifecycleScope)
            chatTextInputLayout.setEndIconOnClickListener { sendMessage() }
            chatInputEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) sendMessage()
                false
            }
        }
    }

    private fun sendMessage() {
        val message: String = binding?.chatInputEditText?.text.toString()
        if (message.isNotEmpty()) {
            viewModel.sendMessage(getChatId(), message)
            binding?.chatInputEditText?.setText("")
            hideKeyboard()
        }
    }

    private fun renderData(chatState: ChatState): Unit? = when (chatState) {
        is ChatState.Error -> chatState.message?.let { toast(it) }
        is ChatState.Success -> updateList(chatState.chatMessageList)
    }

    private fun updateList(chatList: List<ChatMessage>) {
        (binding?.chatRecyclerView?.adapter as ChatAdapter).apply {
            submitList(chatList)
            registerAdapterDataObserver(adapterDataObserver())
        }
    }

    private fun adapterDataObserver() = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int): Unit =
            with(binding?.chatRecyclerView?.layoutManager as LinearLayoutManager) {
                if (findFirstVisibleItemPosition() == FIRST_POSITION)
                    scrollToPosition(FIRST_POSITION)
                else super.onItemRangeInserted(positionStart, itemCount)
            }
    }

    private companion object {
        const val FIRST_POSITION = 0
    }
}