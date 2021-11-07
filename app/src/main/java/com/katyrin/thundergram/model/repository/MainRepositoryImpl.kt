package com.katyrin.thundergram.model.repository

import com.katyrin.libtd_ktx.core.TelegramFlow
import com.katyrin.libtd_ktx.flows.newMessageFlow
import com.katyrin.thundergram.model.storage.Storage
import com.katyrin.thundergram.utils.UNSUBSCRIBE_ID
import com.katyrin.thundergram.utils.regexPhoneNumber
import com.katyrin.thundergram.viewmodel.appstates.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.TdApi
import java.util.regex.Pattern
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val storage: Storage,
    override val api: TelegramFlow
) : MainRepository {

    override suspend fun getUserState(): UserState = withContext(Dispatchers.IO) {
        if (storage.getLogged()) UserState.LoggedState else UserState.NotLoggedState
    }

    override fun getSubscribedPhone(): Flow<String> =
        api.newMessageFlow()
            .filter(::checkSubscribedMessage)
            .map(::getTextMessage)
            .filter(::checkPhoneNumber)
            .map(::getUriPhoneNumber)
            .onEach { clearSubscribeId() }

    private fun checkSubscribedMessage(message: TdApi.Message): Boolean = with(storage) {
        getSubscribeChatId() == message.chatId && getSubscribeUserId() == message.senderUserId.toLong()
    }

    private fun getTextMessage(message: TdApi.Message): String =
        when (val content = message.content) {
            is TdApi.MessageText -> content.text.text
            is TdApi.MessagePhoto -> content.caption.text
            else -> ""
        }

    private fun checkPhoneNumber(message: String): Boolean =
        Pattern.compile(regexPhoneNumber).matcher(message).find()

    private fun getUriPhoneNumber(phoneNumber: String): String = URI_PHONE_PREFIX + phoneNumber

    private fun clearSubscribeId() {
        storage.setSubscribeChatId(UNSUBSCRIBE_ID)
        storage.setSubscribeUserId(UNSUBSCRIBE_ID)
    }

    private companion object {
        const val URI_PHONE_PREFIX = "tel:"
    }
}