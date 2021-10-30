package com.katyrin.thundergram.view


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.katyrin.thundergram.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        val textInputEditText = onView(
            allOf(
                withId(R.id.number_edit_text), withText("+"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.number_input_layout),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("+79998887766"))

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.number_edit_text), withText("+79998887766"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.number_input_layout),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textInputEditText2.perform(closeSoftKeyboard())

        val materialButton = onView(
            allOf(
                withId(R.id.send_button), withText("Send"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.main_container),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val materialButton2 = onView(
            allOf(
                withId(R.id.send_sms_code_button), withText("Send the code as an SMS"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.main_container),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        val materialButton3 = onView(
            allOf(
                withId(R.id.change_phone_button), withText("Change phone number"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.main_container),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.number_edit_text), withText("+"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.number_input_layout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText3.perform(click())

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.number_edit_text), withText("+"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.number_input_layout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText4.perform(replaceText("+79998887766"))

        val textInputEditText5 = onView(
            allOf(
                withId(R.id.number_edit_text), withText("+79998887766"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.number_input_layout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText5.perform(closeSoftKeyboard())

        val materialButton4 = onView(
            allOf(
                withId(R.id.send_button), withText("Send"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.main_container),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())

        val textInputEditText6 = onView(
            allOf(
                withId(R.id.number_edit_text),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.number_input_layout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText6.perform(replaceText("12345"), closeSoftKeyboard())

        val materialButton5 = onView(
            allOf(
                withId(R.id.send_button), withText("Send"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.main_container),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        materialButton5.perform(click())

        val materialButton6 = onView(
            allOf(
                withId(R.id.send_button), withText("Send"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.main_container),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        materialButton6.perform(click())

        val materialButton7 = onView(
            allOf(
                withId(R.id.send_sms_code_button), withText("Send the code as an SMS"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.main_container),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        materialButton7.perform(click())

        val textInputEditText7 = onView(
            allOf(
                withId(R.id.number_edit_text),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.number_input_layout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText7.perform(replaceText("12345"), closeSoftKeyboard())

        val materialButton8 = onView(
            allOf(
                withId(R.id.send_button), withText("Send"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.main_container),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        materialButton8.perform(click())

        val textInputEditText8 = onView(
            allOf(
                withId(R.id.number_edit_text), withText("12345"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.number_input_layout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText8.perform(replaceText("123456"))

        val textInputEditText9 = onView(
            allOf(
                withId(R.id.number_edit_text), withText("123456"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.number_input_layout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText9.perform(closeSoftKeyboard())

        val materialButton9 = onView(
            allOf(
                withId(R.id.send_button), withText("Send"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.main_container),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        materialButton9.perform(click())

        val textInputEditText10 = onView(
            allOf(
                withId(R.id.number_edit_text), withText("123456"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.number_input_layout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText10.perform(replaceText("12345"))

        val textInputEditText11 = onView(
            allOf(
                withId(R.id.number_edit_text), withText("12345"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.number_input_layout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText11.perform(closeSoftKeyboard())

        val materialButton10 = onView(
            allOf(
                withId(R.id.send_button), withText("Send"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.main_container),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        materialButton10.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
