package com.katyrin.thundergram.view

import android.os.Bundle
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import com.katyrin.thundergram.R
import com.katyrin.thundergram.databinding.FragmentLoginBinding
import com.katyrin.thundergram.utils.toast
import com.katyrin.thundergram.view.inputvalidators.LoginCodeValidator
import com.katyrin.thundergram.view.inputvalidators.PhoneNumberValidator
import com.katyrin.thundergram.viewmodel.LoginViewModel
import com.katyrin.thundergram.viewmodel.appstates.AuthState
import javax.inject.Inject

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: LoginViewModel by viewModels(factoryProducer = { factory })

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.authState.observe(viewLifecycleOwner, ::renderAuthState)
        viewModel.errorState.observe(viewLifecycleOwner, ::toast)
        viewModel.loggedState.observe(viewLifecycleOwner, ::renderLoggedState)
    }

    private fun renderAuthState(authState: AuthState?) {
        if (authState == null) return
        when (authState) {
            is AuthState.EnterPhone -> setEnterPhoneState()
            is AuthState.EnterCode -> setEnterCodeState()
            is AuthState.EnterPassword -> setEnterPasswordState()
            is AuthState.LoggedIn -> {
                viewModel.setLogged(true)
                openChatListScreen()
            }
        }
    }

    private fun renderLoggedState(isLogged: Boolean) {
        if (isLogged) openChatListScreen()
        else setEnterPhoneState()
    }

    private fun setEnterPhoneState() {
        binding?.apply {
            val phoneNumberValidator = PhoneNumberValidator()
            numberEditText.addTextChangedListener(phoneNumberValidator)
            numberEditText.setHint(R.string.phone_number)
            numberInputLayout.setHint(R.string.phone_number)
            numberEditText.setText(R.string.russian_phone_code)
            numberEditText.inputType = InputType.TYPE_CLASS_PHONE
            sendButton.setOnClickListener {
                if (phoneNumberValidator.isValid) {
                    viewModel.sendPhone(numberEditText.text.toString())
                    numberEditText.removeTextChangedListener(phoneNumberValidator)
                }
            }
        }
    }

    private fun setEnterCodeState() {
        binding?.apply {
            val loginCodeValidator = LoginCodeValidator()
            numberEditText.addTextChangedListener(loginCodeValidator)
            numberEditText.setHint(R.string.enter_code)
            numberInputLayout.setHint(R.string.enter_code)
            numberEditText.setText("")
            numberEditText.inputType = InputType.TYPE_CLASS_NUMBER
            sendButton.setOnClickListener {
                if (loginCodeValidator.isValid) {
                    viewModel.sendCode(numberEditText.text.toString())
                    numberEditText.removeTextChangedListener(loginCodeValidator)
                }
            }
        }
    }

    private fun setEnterPasswordState() {
        binding?.apply {
            numberEditText.setHint(R.string.enter_password)
            numberInputLayout.setHint(R.string.enter_password)
            numberEditText.setText("")
            numberEditText.inputType = InputType.TYPE_CLASS_TEXT
            sendButton.setOnClickListener { viewModel.sendPassword(numberEditText.text.toString()) }
        }
    }

    private fun openChatListScreen() {
        val navDirections: NavDirections =
            LoginFragmentDirections.actionLoginFragmentToChatListFragment()
        navController?.navigate(navDirections)
    }
}