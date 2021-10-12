package com.katyrin.libtd_ktx.extensions

import com.katyrin.libtd_ktx.core.TelegramFlow
import com.katyrin.libtd_ktx.coroutines.acceptCall
import com.katyrin.libtd_ktx.coroutines.discardCall
import com.katyrin.libtd_ktx.coroutines.sendCallDebugInformation
import com.katyrin.libtd_ktx.coroutines.sendCallRating
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.*

/**
 * Interface for access [TdApi.Call] extension functions. Can be used alongside with other extension
 * interfaces of the package. Must contain [TelegramFlow] instance field to access its functionality
 */
interface CallKtx : BaseKtx {
    /**
     * Instance of the [TelegramFlow] connecting extensions to the Telegram Client
     */
    override val api: TelegramFlow

    /**
     * Suspend function, which accepts an incoming call.
     *
     * @param protocol Description of the call protocols supported by the client.
     */
    suspend fun Call.accept(protocol: CallProtocol?): Unit = api.acceptCall(this.id, protocol)

    /**
     * Suspend function, which discards a call.
     *
     * @param isDisconnected True, if the user was disconnected.
     * @param duration The call duration, in seconds.
     * @param connectionId Identifier of the connection used during the call.
     */
    suspend fun Call.discard(
        isDisconnected: Boolean,
        duration: Int,
        connectionId: Long
    ): Unit = api.discardCall(this.id, isDisconnected, duration, connectionId)

    /**
     * Suspend function, which sends debug information for a call.
     *
     * @param debugInformation Debug information in application-specific format.
     */
    suspend fun Call.sendDebugInformation(debugInformation: String?): Unit =
        api.sendCallDebugInformation(this.id, debugInformation)

    /**
     * Suspend function, which sends a call rating.
     *
     * @param rating Call rating; 1-5.
     * @param comment An optional user comment if the rating is less than 5.
     * @param problems List of the exact types of problems with the call, specified by the user.
     */
    suspend fun Call.sendRating(
        rating: Int,
        comment: String?,
        problems: Array<CallProblem>?
    ): Unit = api.sendCallRating(this.id, rating, comment, problems)
}