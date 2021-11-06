package com.katyrin.thundergram.architecture_tests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.katyrin.thundergram.model.entities.ChatMessage
import com.katyrin.thundergram.model.repository.ChatRepository
import com.katyrin.thundergram.viewmodel.ChatViewModel
import com.katyrin.thundergram.viewmodel.appstates.ChatState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class ChatViewModelTest {

    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var chatRepository: ChatRepository
    private lateinit var chatViewModel: ChatViewModel
    private val mainThreadSurrogate = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(mainThreadSurrogate)
        chatViewModel = ChatViewModel(chatRepository)
    }

    @Test
    fun coroutines_TestReturnValueIsNotNull(): Unit = runBlockingTest(mainThreadSurrogate) {
        val observer = Observer<ChatState> {}
        val liveData = chatViewModel.liveData
        val messages: MutableList<ChatMessage> = mutableListOf()
        for (i in 1..100) messages.add(ChatMessage.Text(i.toLong()))

        `when`(chatRepository.getHistoryMessages(FAKE_CHAT_ID)).thenReturn(messages)

        try {
            liveData.observeForever(observer)
            chatViewModel.getMessages(FAKE_CHAT_ID)
            assertNotNull(liveData.value)
        } finally {
            liveData.removeObserver(observer)
        }
    }

    @Test
    fun coroutines_TestReturnValueIsError(): Unit = runBlockingTest(mainThreadSurrogate) {
        val observer = Observer<ChatState> {}
        val liveData = chatViewModel.liveData

        `when`(chatRepository.getHistoryMessages(FAKE_CHAT_ID)).thenReturn(null)

        try {
            liveData.observeForever(observer)
            chatViewModel.getMessages(FAKE_CHAT_ID)
            assertTrue(chatViewModel.liveData.value is ChatState.Error)
        } finally {
            liveData.removeObserver(observer)
        }
    }

    @Test
    fun coroutines_TestException(): Unit = runBlockingTest(mainThreadSurrogate) {
        val observer = Observer<ChatState> {}
        val liveData = chatViewModel.liveData

        try {
            liveData.observeForever(observer)
            chatViewModel.getMessages(FAKE_CHAT_ID)
            val value: ChatState.Error = liveData.value as ChatState.Error
            assertEquals(value.message, ERROR_TEXT)
        } finally {
            liveData.removeObserver(observer)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    companion object {
        const val FAKE_CHAT_ID = 1L
        const val ERROR_TEXT = "Parameter specified as non-null is null: method com.katyrin.thundergram.viewmodel.appstates.ChatState\$Success.<init>, parameter chatMessageList"
    }
}