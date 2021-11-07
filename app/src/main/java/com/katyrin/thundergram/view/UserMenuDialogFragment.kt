package com.katyrin.thundergram.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.katyrin.thundergram.databinding.DialogFragmentUserMenuBinding
import com.katyrin.thundergram.utils.UNSUBSCRIBE_ID
import com.katyrin.thundergram.viewmodel.UserMenuViewModel
import com.katyrin.thundergram.viewmodel.appstates.UserMenuState
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class UserMenuDialogFragment : DialogFragment(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: UserMenuViewModel by viewModels(factoryProducer = { factory })

    private var binding: DialogFragmentUserMenuBinding? = null

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = DialogFragmentUserMenuBinding.inflate(inflater, container, false)
        .also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveData.observe(viewLifecycleOwner, ::renderData)
        val chatId: Long = UserMenuDialogFragmentArgs.fromBundle(requireArguments()).chatId
        val userId: Long = UserMenuDialogFragmentArgs.fromBundle(requireArguments()).userId
        viewModel.checkExistSubscribe(chatId, userId)
    }

    private fun renderData(userMenuState: UserMenuState) {
        when (userMenuState) {
            is UserMenuState.ShowSubscribe ->
                showSubscribeState(userMenuState.chatId, userMenuState.userId)
            is UserMenuState.ShowUnsubscribe -> showUnsubscribeState()
        }
    }

    private fun showSubscribeState(chatId: Long, userId: Long) {
        binding?.subscribeLayout?.isVisible = true
        binding?.unsubscribeLayout?.isVisible = false
        binding?.subscribeLayout?.setOnClickListener {
            viewModel.subscribeUserInChat(chatId, userId)
            dismiss()
        }
    }

    private fun showUnsubscribeState() {
        binding?.subscribeLayout?.isVisible = false
        binding?.unsubscribeLayout?.isVisible = true
        binding?.unsubscribeLayout?.setOnClickListener {
            viewModel.subscribeUserInChat(UNSUBSCRIBE_ID, UNSUBSCRIBE_ID)
            dismiss()
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}