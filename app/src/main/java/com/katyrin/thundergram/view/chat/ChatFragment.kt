package com.katyrin.thundergram.view.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.motion.widget.OnSwipe
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.katyrin.thundergram.databinding.FragmentChatBinding
import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.utils.checkCallPermission
import com.katyrin.thundergram.utils.hideKeyboard
import com.katyrin.thundergram.utils.toast
import com.katyrin.thundergram.view.BaseFragment
import com.katyrin.thundergram.view.chat.adapter.ChatAdapter
import com.katyrin.thundergram.view.main.CallListener
import com.katyrin.thundergram.view.main.MainActivity
import com.katyrin.thundergram.view.main.ToolBarMotionListener
import com.katyrin.thundergram.viewmodel.ChatViewModel
import com.katyrin.thundergram.viewmodel.appstates.ChatState
import com.katyrin.thundergram.viewmodel.appstates.ChatViewEffect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


class ChatFragment : BaseFragment<FragmentChatBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: ChatViewModel by viewModels(factoryProducer = { factory })

    @Inject
    lateinit var exoPlayer: ExoPlayer

    private var callListener: CallListener? = null
    private var toolBarMotionListener: ToolBarMotionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callListener = context as MainActivity
        toolBarMotionListener = context
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatBinding = FragmentChatBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.liveData.observe(viewLifecycleOwner, ::renderData)
        viewModel.effect
            .flowWithLifecycle(lifecycle)
            .onEach(::renderEffect)
            .launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.getMessages(getChatId())
        viewModel.getNewMessage(getChatId()).observe(viewLifecycleOwner, ::updateList)
    }

    override fun onResume() {
        super.onResume()
        toolBarMotionListener?.onChangeSceneTransitionSwipe(OnSwipe.DRAG_DOWN)
        toolBarMotionListener?.onSetToolBarText(getChatName())
    }

    private fun getChatId(): Long = ChatFragmentArgs.fromBundle(requireArguments()).chatId

    private fun getChatName(): String = ChatFragmentArgs.fromBundle(requireArguments()).chatName

    private fun initViews() {
        binding?.chatRecyclerView?.adapter = ChatAdapter(
            exoPlayer,
            lifecycleScope,
            ::onPhoneNumberClick,
            viewModel::checkExistSubscribe,
            viewModel::onUpdateSoundSpeed,
            viewModel::onClickPlayButton,
            viewModel::onUpdateSeekTo
        ) { requireActivity().checkCallPermission(viewModel::onClickUserMenu) }
        binding?.chatRecyclerView?.itemAnimator = null
        binding?.chatTextInputLayout?.setEndIconOnClickListener { sendMessage() }
        binding?.chatInputEditText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) sendMessage()
            false
        }
    }

    private fun onPhoneNumberClick(phoneNumber: String) {
        callListener?.onPhoneCallNumber(phoneNumber, ONE_COIN)
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

    private fun renderEffect(chatViewEffect: ChatViewEffect): Unit = when (chatViewEffect) {
        is ChatViewEffect.OnIsSubscribed ->
            (binding?.chatRecyclerView?.adapter as ChatAdapter).onShowPopupMenu(true)
        is ChatViewEffect.OnIsNotSubscribed ->
            (binding?.chatRecyclerView?.adapter as ChatAdapter).onShowPopupMenu(false)
    }

    override fun onDestroyView() {
        callListener = null
        toolBarMotionListener = null
        super.onDestroyView()
    }

    private companion object {
        const val FIRST_POSITION = 0
        const val ONE_COIN = 1
    }
}