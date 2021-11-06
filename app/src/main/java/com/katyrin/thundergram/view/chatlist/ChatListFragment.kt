package com.katyrin.thundergram.view.chatlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.OnSwipe
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import com.katyrin.thundergram.R
import com.katyrin.thundergram.databinding.FragmentChatListBinding
import com.katyrin.thundergram.model.entities.ChatListItem
import com.katyrin.thundergram.utils.toast
import com.katyrin.thundergram.view.BaseFragment
import com.katyrin.thundergram.view.main.MainActivity
import com.katyrin.thundergram.view.main.ToolBarMotionListener
import com.katyrin.thundergram.viewmodel.ChatListViewModel
import com.katyrin.thundergram.viewmodel.appstates.ChatListState
import javax.inject.Inject

class ChatListFragment : BaseFragment<FragmentChatListBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: ChatListViewModel by viewModels(factoryProducer = { factory })
    private var toolBarMotionListener: ToolBarMotionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        toolBarMotionListener = context as MainActivity
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatListBinding = FragmentChatListBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveData.observe(viewLifecycleOwner, ::renderData)
        viewModel.updateList.observe(viewLifecycleOwner, ::renderData)
        viewModel.getChats()
        initViews()
    }

    override fun onResume() {
        super.onResume()
        toolBarMotionListener?.onChangeSceneTransitionSwipe(OnSwipe.DRAG_UP)
        toolBarMotionListener?.onSetToolBarText(getString(R.string.app_name))
    }

    private fun initViews() {
        binding?.recyclerChatList?.adapter = ChatListAdapter(::openChat)
    }

    private fun openChat(chatId: Long, chatName: String) {
        val navDirections: NavDirections =
            ChatListFragmentDirections.actionChatListFragmentToChatFragment(chatId, chatName)
        navController?.navigate(navDirections)
    }

    private fun renderData(chatListState: ChatListState): Unit? = when (chatListState) {
        is ChatListState.Success -> updateList(chatListState.chatList)
        is ChatListState.Error -> chatListState.message?.let { toast(it) }
    }

    private fun updateList(chatList: List<ChatListItem>) {
        (binding?.recyclerChatList?.adapter as ChatListAdapter).submitList(chatList)
    }

    override fun onDetach() {
        toolBarMotionListener = null
        super.onDetach()
    }
}