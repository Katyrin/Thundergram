package com.katyrin.thundergram.myview

import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions
import com.google.android.material.textfield.TextInputLayout
import io.github.kakaocup.kakao.common.assertions.BaseAssertions
import io.github.kakaocup.kakao.common.matchers.TextInputLayoutCounterEnabledMatcher
import io.github.kakaocup.kakao.common.matchers.TextInputLayoutErrorEnabledMatcher
import io.github.kakaocup.kakao.common.matchers.TextInputLayoutHintEnabledMatcher

interface MyTextInputLayoutAssertions : BaseAssertions {

    fun hasHint(hint: String) {
        view.check(ViewAssertion { view, notFoundException ->
            if (view is TextInputLayout) {
                if (hint != view.hint) {
                    throw AssertionError(
                        "Expected hint is $hint," +
                                " but actual is ${view.hint}"
                    )
                }
            } else {
                notFoundException?.let { throw AssertionError(it) }
            }
        })
    }

    fun isHintEnabled() {
        view.check(ViewAssertions.matches(TextInputLayoutHintEnabledMatcher(true)))
    }

    fun isHintDisabled() {
        view.check(ViewAssertions.matches(TextInputLayoutHintEnabledMatcher(false)))
    }

    fun hasError(error: String) {
        view.check(ViewAssertion { view, notFoundException ->
            if (view is TextInputLayout) {
                if (error != view.error) {
                    throw AssertionError(
                        "Expected error is $error," +
                                " but actual is ${view.error}"
                    )
                }
            } else {
                notFoundException?.let { throw AssertionError(it) }
            }
        })
    }

    fun isErrorEnabled() {
        view.check(ViewAssertions.matches(TextInputLayoutErrorEnabledMatcher(true)))
    }

    fun isErrorDisabled() {
        view.check(ViewAssertions.matches(TextInputLayoutErrorEnabledMatcher(false)))
    }

    fun hasCounterMaxLength(length: Int) {
        view.check(ViewAssertion { view, notFoundException ->
            if (view is TextInputLayout) {
                if (length != view.counterMaxLength) {
                    throw AssertionError(
                        "Expected counter max length is $length," +
                                " but actual is ${view.counterMaxLength}"
                    )
                }
            } else {
                notFoundException?.let { throw AssertionError(it) }
            }
        })
    }

    fun isCounterEnabled() {
        view.check(ViewAssertions.matches(TextInputLayoutCounterEnabledMatcher(true)))
    }

    fun isCounterDisabled() {
        view.check(ViewAssertions.matches(TextInputLayoutCounterEnabledMatcher(false)))
    }

    fun hasHelperText(helperText: String?) {
        view.check { view, notFoundException ->
            if (view is TextInputLayout) {
                if (helperText != view.helperText) {
                    throw AssertionError(
                        "Expected helperText is $helperText," +
                                " but actual is ${view.helperText}"
                    )
                }
            } else {
                notFoundException?.let { throw AssertionError(it) }
            }
        }
    }

    fun isHelperTextDisabled() {
        view.check(ViewAssertions.matches(TextInputLayoutHelperTextEnabledMatcher(false)))
    }

    fun isHelperTextEnabled() {
        view.check(ViewAssertions.matches(TextInputLayoutHelperTextEnabledMatcher(true)))
    }
}