package com.katyrin.thundergram

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.katyrin.thundergram.view.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginFragmentTest : KTestCase() {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun fragmentLoginScreenTest() {
        run {
            step("First step") {
                LoginScreen {
                    descriptionTextView {
                        isDisplayed()
                        hasText(R.string.login_description_text)
                    }
                }
            }
            step("Send phone number") {
                LoginScreen {
                    numberEditTextView {
                        isDisplayed()
                        click()
                        hasText("+7")
                        typeText("9998887766")
                    }
                    closeSoftKeyboard()
                    sendButtonView {
                        isDisplayed()
                        click()
                    }

                }
            }
            step("Send code") {
                LoginScreen {
                    numberEditTextView {
                        isDisplayed()
                        click()
                        hasText("")
                        typeText("12345")
                    }
                    closeSoftKeyboard()
                    sendButtonView {
                        isDisplayed()
                        click()
                    }

                }
            }
        }
    }
}