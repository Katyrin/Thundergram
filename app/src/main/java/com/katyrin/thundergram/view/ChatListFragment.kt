package com.katyrin.thundergram.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.katyrin.thundergram.databinding.FragmentChatListBinding
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
        viewModel.liveData.observe(viewLifecycleOwner) {
            binding?.helloStub?.text = it.joinToString("\n")
        }
        viewModel.getChats()
    }
}