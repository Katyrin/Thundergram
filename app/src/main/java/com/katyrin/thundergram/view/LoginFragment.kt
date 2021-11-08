package com.katyrin.thundergram.view

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import com.katyrin.thundergram.R
import com.katyrin.thundergram.databinding.FragmentLoginBinding
import com.katyrin.thundergram.utils.hideKeyboard
import com.katyrin.thundergram.view.inputvalidators.LoginCodeValidator
import com.katyrin.thundergram.view.inputvalidators.PhoneNumberValidator
import com.katyrin.thundergram.view.main.LoginListener
import com.katyrin.thundergram.view.main.MainActivity
import com.katyrin.thundergram.viewmodel.LoginViewModel
import com.katyrin.thundergram.viewmodel.appstates.AuthState
import javax.inject.Inject

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: LoginViewModel by viewModels(factoryProducer = { factory })
    private var loginListener: LoginListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginListener = context as MainActivity
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.authState.observe(viewLifecycleOwner, ::renderAuthState)
        initViews()
    }

    private fun initViews() {
        binding?.apply {
            changePhoneButton.setOnClickListener { setEnterPhoneState() }
            sendSmsCodeButton.setOnClickListener {
                viewModel.resendAuthenticationCode()
                sendSmsCodeButton.isVisible = false
            }
        }
    }

    private fun renderAuthState(authState: AuthState?): Unit = when (authState) {
        is AuthState.EnterPhone -> setEnterPhoneState()
        is AuthState.EnterCode -> setEnterCodeState()
        is AuthState.EnterPassword -> setEnterPasswordState()
        is AuthState.LoggedIn -> setLoggedInState()
        null -> Unit
    }

    private fun setLoggedInState() {
        loginListener?.onLoginState()
        openChatListScreen()
    }

    private fun setEnterPhoneState() {
        val phoneNumberValidator = PhoneNumberValidator()
        setVisibilityRepeatButton(false)
        binding?.apply {
            numberInputLayout.setHint(R.string.phone_number)
            numberEditText.setText(R.string.char_plus)
            numberEditText.inputType = InputType.TYPE_CLASS_PHONE
            numberEditText.addTextChangedListener(phoneNumberValidator)
            sendButton.setOnClickListener { clickSendButtonEnterPhone(phoneNumberValidator) }
            numberEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    clickSendButtonEnterPhone(phoneNumberValidator)
                false
            }
        }
    }

    private fun clickSendButtonEnterPhone(phoneNumberValidator: PhoneNumberValidator) {
        binding?.apply {
            if (phoneNumberValidator.isValid) {
                hideKeyboard()
                viewModel.sendPhone(numberEditText.text.toString())
                numberEditText.removeTextChangedListener(phoneNumberValidator)
                setVisibilityRepeatButton(true)
                numberInputLayout.helperText = ""
            } else
                numberInputLayout.helperText = requireContext().getText(R.string.helper_text_phone)
        }
    }

    private fun setEnterCodeState() {
        binding?.apply {
            val loginCodeValidator = LoginCodeValidator()
            numberEditText.addTextChangedListener(loginCodeValidator)
            numberInputLayout.setHint(R.string.enter_code)
            numberEditText.setText("")
            numberEditText.inputType = InputType.TYPE_CLASS_NUMBER
            sendButton.setOnClickListener { clickSendButtonCode(loginCodeValidator) }
            numberEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) clickSendButtonCode(loginCodeValidator)
                false
            }
        }
    }

    private fun clickSendButtonCode(loginCodeValidator: LoginCodeValidator) {
        binding?.apply {
            if (loginCodeValidator.isValid) {
                viewModel.sendCode(numberEditText.text.toString())
                numberEditText.removeTextChangedListener(loginCodeValidator)
                numberInputLayout.helperText = ""
            } else
                numberInputLayout.helperText = requireContext().getText(R.string.helper_text_code)
        }
    }

    private fun setEnterPasswordState() {
        setVisibilityRepeatButton(false)
        binding?.apply {
            numberInputLayout.setHint(R.string.enter_password)
            numberEditText.setText("")
            numberEditText.inputType = InputType.TYPE_CLASS_TEXT
            sendButton.setOnClickListener { viewModel.sendPassword(numberEditText.text.toString()) }
        }
    }

    private fun setVisibilityRepeatButton(isVisible: Boolean) {
        binding?.changePhoneButton?.isVisible = isVisible
        binding?.sendSmsCodeButton?.isVisible = isVisible
    }

    private fun openChatListScreen() {
        val navDirections: NavDirections =
            LoginFragmentDirections.actionLoginFragmentToChatListFragment()
        navController?.navigate(navDirections)
    }

    override fun onDestroyView() {
        loginListener = null
        super.onDestroyView()
    }
}