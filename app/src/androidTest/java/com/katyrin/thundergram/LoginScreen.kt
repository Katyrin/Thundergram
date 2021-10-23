package com.katyrin.thundergram

import com.katyrin.thundergram.myview.MyKTextInputLayout
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.edit.KTextInputLayout
import io.github.kakaocup.kakao.screen.Screen
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView

object LoginScreen : Screen<LoginScreen>() {
    val descriptionTextView = KTextView { withId(R.id.description_text) }
    val numberEditTextView = KEditText { withId(R.id.number_edit_text) }
    val sendButtonView = KButton { withId(R.id.send_button) }
    val changePhoneButtonView = KButton { withId(R.id.change_phone_button) }
    val sendSmsCodeButtonView = KButton { withId(R.id.send_sms_code_button) }
    val numberInputLayoutView = MyKTextInputLayout { withId(R.id.number_input_layout) }
}