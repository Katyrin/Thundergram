package com.katyrin.thundergram.view.tutorial

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.katyrin.thundergram.databinding.FragmentTutorialMainPage3Binding
import com.katyrin.thundergram.view.BaseFragment

class TutorialMainPage3Fragment : BaseFragment<FragmentTutorialMainPage3Binding>() {

    private var countDownTimer: CountDownTimer? = null
    private var stepAnimation = FIRST_STEP

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTutorialMainPage3Binding =
        FragmentTutorialMainPage3Binding.inflate(inflater, container, false)

    override fun onResume() {
        super.onResume()
        countDownTimer?.cancel()
        setThirdStep()
        countDownTimer = object : CountDownTimer(MILLIS_IN_FUTURE, INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                try {
                    when (stepAnimation) {
                        FIRST_STEP -> stepAnimation = SECOND_STEP
                        SECOND_STEP -> setSecondStep()
                        THIRD_STEP -> setThirdStep()
                    }
                } catch (exception: IndexOutOfBoundsException) {
                    exception.printStackTrace()
                    countDownTimer?.cancel()
                }
            }

            override fun onFinish() {}
        }.start()
    }

    private fun setSecondStep() {
        stepAnimation = THIRD_STEP
        binding?.subscribeLayout?.isVisible = false
        binding?.unsubscribeLayout?.isVisible = true
        binding?.unsubscribeLayout?.isPressed = true
        binding?.unsubscribeLayout?.isPressed = false
    }

    private fun setThirdStep() {
        stepAnimation = SECOND_STEP
        binding?.subscribeLayout?.isVisible = true
        binding?.unsubscribeLayout?.isVisible = false
        binding?.subscribeLayout?.isPressed = true
        binding?.subscribeLayout?.isPressed = false
    }

    override fun onPause() {
        super.onPause()
        try {
            countDownTimer?.cancel()
            stepAnimation = FIRST_STEP
            binding?.subscribeLayout?.isVisible = true
            binding?.unsubscribeLayout?.isVisible = false
        } catch (exception: IndexOutOfBoundsException) {
            exception.printStackTrace()
        }
    }

    override fun onDestroyView() {
        countDownTimer?.cancel()
        countDownTimer = null
        super.onDestroyView()
    }

    private companion object {
        const val MILLIS_IN_FUTURE = 60000L
        const val INTERVAL = 2000L
        const val FIRST_STEP = 0
        const val SECOND_STEP = 1
        const val THIRD_STEP = 2
    }
}