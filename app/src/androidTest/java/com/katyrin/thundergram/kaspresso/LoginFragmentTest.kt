package com.katyrin.thundergram.kaspresso

import FALSE_CODE
import NORMAL_CODE
import NORMAL_NUMBER
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.katyrin.thundergram.R
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
                    numberInputLayoutView {
                        isDisplayed()
                        hasHelperText(null)
                    }
                }
            }
            step("Send phone number") {
                LoginScreen {
                    numberEditTextView {
                        isDisplayed()
                        hasHint(R.string.phone_number)
                        click()
                        hasText(R.string.char_plus)
                        typeText("79998887766")
                    }
                    closeSoftKeyboard()
                    sendButtonView {
                        isDisplayed()
                        click()
                    }
                }
            }
            step("Change phone number") {
                LoginScreen {
                    numberEditTextView {
                        isDisplayed()
                        hasHint(R.string.enter_code)
                    }
                    changePhoneButtonView {
                        isDisplayed()
                        click()
                    }
                    numberEditTextView {
                        isDisplayed()
                        hasHint(R.string.phone_number)
                        click()
                        hasText(R.string.char_plus)
                        typeText("755544433221")
                    }
                    closeSoftKeyboard()
                    sendButtonView {
                        isDisplayed()
                        click()
                    }
                    numberInputLayoutView {
                        isDisplayed()
                        hasHelperText("Check if the number is entered correctly. The number must start with a \"+\" sign, followed by your country code, followed by the main number. Also make sure there are no extra characters or spaces in the number.")
                    }
                    numberEditTextView {
                        isDisplayed()
                        hasHint(R.string.phone_number)
                        click()
                        hasText("+755544433221")
                        clearText()
                        typeText(NORMAL_NUMBER)
                    }
                    closeSoftKeyboard()
                    sendButtonView {
                        isDisplayed()
                        click()
                    }
                    numberInputLayoutView {
                        isDisplayed()
                        hasHelperText(null)
                    }
                }
            }
            step("Send code as sms") {
                LoginScreen {
                    numberEditTextView {
                        isDisplayed()
                        hasHint(R.string.enter_code)
                    }
                    numberInputLayoutView {
                        isDisplayed()
                        hasHelperText(null)
                    }
                    sendSmsCodeButtonView {
                        isDisplayed()
                        isVisible()
                        click()
                        isGone()
                    }
                    numberEditTextView {
                        isDisplayed()
                        hasHint(R.string.enter_code)
                        click()
                        hasText("")
                        typeText(FALSE_CODE)
                    }
                    closeSoftKeyboard()
                    sendButtonView {
                        isDisplayed()
                        click()
                    }
                    numberInputLayoutView {
                        isDisplayed()
                        hasHelperText("The code must contain 5 numbers, no letters, spaces, or extra characters.")
                    }
                    numberEditTextView {
                        isDisplayed()
                        hasHint(R.string.enter_code)
                        click()
                        hasText(FALSE_CODE)
                        clearText()
                        typeText(NORMAL_CODE)
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