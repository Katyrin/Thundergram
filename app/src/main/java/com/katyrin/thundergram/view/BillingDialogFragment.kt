package com.katyrin.thundergram.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.katyrin.thundergram.billing.BillingManager
import com.katyrin.thundergram.billing.BillingState
import com.katyrin.thundergram.databinding.FragmentBillingDialogBinding
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


class BillingDialogFragment : BottomSheetDialogFragment(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    @Inject
    lateinit var billingManager: BillingManager

    private var binding: FragmentBillingDialogBinding? = null

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentBillingDialogBinding.inflate(inflater, container, false)
        .also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        billingManager.stateFlow
            .flowWithLifecycle(lifecycle)
            .onEach(::renderBillingState)
            .launchIn(lifecycleScope)
    }

    private fun renderBillingState(billingStateList: List<BillingState>) {
        billingStateList.forEachIndexed { index, billingState ->
            binding?.apply {
                when (index) {
                    0 -> billingState.setProductString(firstBillingText, firstBillingButton)
                    1 -> billingState.setProductString(secondBillingText, secondBillingButton)
                    2 -> billingState.setProductString(thirdBillingText, thirdBillingButton)
                }
            }
        }
    }

    private fun BillingState.setProductString(
        productName: TextView,
        productButton: MaterialButton
    ) {
        productName.text = title
        productButton.text = price
        productButton.setOnClickListener {
            billingManager.launchBillingFlow(requireActivity(), index)
            dismiss()
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}