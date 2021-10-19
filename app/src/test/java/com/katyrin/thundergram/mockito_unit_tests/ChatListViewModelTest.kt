package com.katyrin.thundergram.mockito_unit_tests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.katyrin.thundergram.model.entities.ChatListItem
import com.katyrin.thundergram.model.repository.ChatListRepository
import com.katyrin.thundergram.viewmodel.ChatListViewModel
import com.katyrin.thundergram.viewmodel.appstates.ChatListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class ChatListViewModelTest {

    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var chatListRepository: ChatListRepository
    private lateinit var chatListViewModel: ChatListViewModel
    private val mainThreadSurrogate = TestCoroutineDispatcher()


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(mainThreadSurrogate)
        chatListViewModel = ChatListViewModel(chatListRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test liveData is Success`(): Unit = runBlockingTest(mainThreadSurrogate) {
        val mockList: List<ChatListItem> = listOf()
        `when`(chatListRepository.getChats()).thenReturn(mockList)
        chatListViewModel.getChats()
        assertTrue(chatListViewModel.liveData.value is ChatListState.Success)
    }

    @Test(expected = RuntimeException::class)
    fun `test liveData is Error`(): Unit = runBlockingTest(mainThreadSurrogate) {
        val fakeException = Mockito.mock(IOException::class.java)
        `when`(chatListRepository.getChats()).thenThrow(fakeException)
        chatListViewModel.getChats()
        assertTrue(chatListViewModel.liveData.value is ChatListState.Error)
    }
}