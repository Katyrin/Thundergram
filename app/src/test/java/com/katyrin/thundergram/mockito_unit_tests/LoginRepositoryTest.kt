package com.katyrin.thundergram.mockito_unit_tests

import com.katyrin.libtd_ktx.core.TelegramFlow
import com.katyrin.libtd_ktx.flows.authorizationStateFlow
import com.katyrin.thundergram.model.mapping.LoginMapping
import com.katyrin.thundergram.model.mapping.LoginMappingImpl
import com.katyrin.thundergram.model.repository.LoginRepository
import com.katyrin.thundergram.model.repository.LoginRepositoryImpl
import com.katyrin.thundergram.model.storage.Storage
import com.katyrin.thundergram.viewmodel.appstates.AuthState
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.drinkless.td.libcore.telegram.TdApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class LoginRepositoryTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val api: TelegramFlow = mock()
    private val parameters: TdApi.TdlibParameters = mock()
    private val loginMapping: LoginMapping = LoginMappingImpl()
    private val storage: Storage = mock()
    private val loginRepository: LoginRepository =
        LoginRepositoryImpl(parameters, loginMapping, storage, testDispatcher, api)

    @Before
    fun setUp() {
        mockkStatic(TelegramFlow::authorizationStateFlow)
    }

    @Test
    fun `flow emits LoggedIn`(): Unit = runBlocking(testDispatcher) {
        val returnFlow: Flow<TdApi.AuthorizationStateReady> =
            flow { emit(TdApi.AuthorizationStateReady()) }
        every { api.authorizationStateFlow() } returns returnFlow

        val flow = loginRepository.getAuthFlow()
        flow.collect { result: AuthState? ->
            assertEquals(result, AuthState.LoggedIn)
        }
    }

    @Test
    fun `flow emits EnterPhone`(): Unit = runBlocking(testDispatcher) {
        val returnFlow: Flow<TdApi.AuthorizationStateWaitPhoneNumber> =
            flow { emit(TdApi.AuthorizationStateWaitPhoneNumber()) }
        every { api.authorizationStateFlow() } returns returnFlow

        val flow = loginRepository.getAuthFlow()
        flow.collect { result: AuthState? ->
            assertEquals(result, AuthState.EnterPhone)
        }
    }

    @Test
    fun `flow emits EnterPassword`(): Unit = runBlocking(testDispatcher) {
        val returnFlow: Flow<TdApi.AuthorizationStateWaitPassword> =
            flow { emit(TdApi.AuthorizationStateWaitPassword()) }
        every { api.authorizationStateFlow() } returns returnFlow

        val flow = loginRepository.getAuthFlow()
        flow.collect { result: AuthState? ->
            assertEquals(result, AuthState.EnterPassword)
        }
    }

    @Test
    fun `flow emits EnterCode`(): Unit = runBlocking(testDispatcher) {
        val returnFlow: Flow<TdApi.AuthorizationStateWaitCode> =
            flow { emit(TdApi.AuthorizationStateWaitCode()) }
        every { api.authorizationStateFlow() } returns returnFlow

        val flow = loginRepository.getAuthFlow()
        flow.collect { result: AuthState? ->
            assertEquals(result, AuthState.EnterCode)
        }
    }

    @Test
    fun `flow emits null`(): Unit = runBlocking(testDispatcher) {
        val returnFlow: Flow<TdApi.AuthorizationStateWaitEncryptionKey> =
            flow { emit(TdApi.AuthorizationStateWaitEncryptionKey()) }
        every { api.authorizationStateFlow() } returns returnFlow

        val flow = loginRepository.getAuthFlow()
        flow.collect { result: AuthState? ->
            assertNull(result)
        }
    }
}