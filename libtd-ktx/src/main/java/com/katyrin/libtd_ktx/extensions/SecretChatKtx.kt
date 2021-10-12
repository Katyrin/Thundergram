package com.katyrin.libtd_ktx.extensions

import com.katyrin.libtd_ktx.core.TelegramFlow
import com.katyrin.libtd_ktx.coroutines.closeSecretChat
import com.katyrin.libtd_ktx.coroutines.createSecretChat
import com.katyrin.libtd_ktx.coroutines.getSecretChat
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.SecretChat

/**
 * Interface for access [TdApi.SecretChat] extension functions. Can be used alongside with other
 * extension interfaces of the package. Must contain [TelegramFlow] instance field to access its
 * functionality
 */
interface SecretChatKtx : BaseKtx {
    /**
     * Instance of the [TelegramFlow] connecting extensions to the Telegram Client
     */
    override val api: TelegramFlow

    /**
     * Suspend function, which closes a secret chat, effectively transferring its state to
     * secretChatStateClosed.
     */
    suspend fun SecretChat.close(): Unit = api.closeSecretChat(id)

    /**
     * Suspend function, which returns an existing chat corresponding to a known secret chat.
     *
     *
     * @return [TdApi.Chat] A chat. (Can be a private chat, basic group, supergroup, or secret chat.)
     */
    suspend fun SecretChat.create(): TdApi.Chat = api.createSecretChat(id)

    /**
     * Suspend function, which returns information about a secret chat by its identifier. This is an
     * offline request.
     *
     *
     * @return [TdApi.SecretChat] Represents a secret chat.
     */
    suspend fun SecretChat.get(): SecretChat = api.getSecretChat(id)
}