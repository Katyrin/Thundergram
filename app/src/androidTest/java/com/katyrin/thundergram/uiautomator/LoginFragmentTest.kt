package com.katyrin.thundergram.uiautomator

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class LoginFragmentTest {

    private val uiDevice: UiDevice = UiDevice.getInstance(getInstrumentation())
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val packageName = context.packageName

    @Before
    fun setUp() {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        uiDevice.pressHome()
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT)
    }

    @Test
    fun device_isNotNull() {
        assertNotNull(uiDevice)
    }

    @Test
    fun appPackage_isNotNul() {
        assertNotNull(packageName)
    }

    @Test
    fun mainActivity_isNotNull() {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        assertNotNull(intent)
    }

    @Test
    fun loginScreen_isStarted() {
        val editText = uiDevice.findObject(By.res(packageName, "number_edit_text"))
        assertNotNull(editText)
    }

    @Test
    fun sendPhoneNumber_isNotNull() {
        val editText = uiDevice.findObject(By.res(packageName, "number_edit_text"))
        editText.text = "+79998887766"
        val sendButton = uiDevice.findObject(By.res(packageName, "send_button"))
        sendButton.click()
        val sendSmsCodeButton = uiDevice
            .wait(Until.findObject(By.res(packageName, "send_sms_code_button")), TIMEOUT)
        assertNotNull(sendSmsCodeButton)
    }

    @Test
    fun chatScreen_isStarted() {
        val editText = uiDevice.findObject(By.res(packageName, "number_edit_text"))
        editText.text = "+79998887766"
        val sendButton = uiDevice.findObject(By.res(packageName, "send_button"))
        sendButton.click()
        editText.text = "12345"
        sendButton.click()
        val recyclerChatList = uiDevice
            .wait(Until.findObject(By.res(packageName, "recycler_chat_list")), TIMEOUT)
        assertNotNull(recyclerChatList)
    }

    @Test
    fun chatScreen_isNotStarted() {
        val editText = uiDevice.findObject(By.res(packageName, "number_edit_text"))
        editText.text = "+79998887766"
        val sendButton = uiDevice.findObject(By.res(packageName, "send_button"))
        sendButton.click()
        editText.text = "123456"
        sendButton.click()
        val recyclerChatList = uiDevice
            .wait(Until.findObject(By.res(packageName, "recycler_chat_list")), TIMEOUT)
        assertNull(recyclerChatList)
    }

    private companion object {
        const val TIMEOUT = 5000L
    }
}