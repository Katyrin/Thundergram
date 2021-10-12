package com.katyrin.libtd_ktx.extensions

import com.katyrin.libtd_ktx.core.TelegramFlow
import com.katyrin.libtd_ktx.coroutines.*
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Proxy
import org.drinkless.td.libcore.telegram.TdApi.ProxyType

/**
 * Interface for access [TdApi.Proxy] extension functions. Can be used alongside with other
 * extension interfaces of the package. Must contain [TelegramFlow] instance field to access its
 * functionality
 */
interface ProxyKtx : BaseKtx {
    /**
     * Instance of the [TelegramFlow] connecting extensions to the Telegram Client
     */
    override val api: TelegramFlow

    /**
     * Suspend function, which edits an existing proxy server for network requests. Can be called
     * before authorization.
     *
     * @param server Proxy server IP address.
     * @param port Proxy server port.
     * @param enable True, if the proxy should be enabled.
     * @param type Proxy type.
     *
     * @return [TdApi.Proxy] Contains information about a proxy server.
     */
    suspend fun Proxy.edit(
        server: String?,
        port: Int,
        enable: Boolean,
        type: ProxyType?
    ): Proxy = api.editProxy(id, server, port, enable, type)

    /**
     * Suspend function, which enables a proxy. Only one proxy can be enabled at a time. Can be called
     * before authorization.
     */
    suspend fun Proxy.enable(): Unit = api.enableProxy(id)

    /**
     * Suspend function, which returns an HTTPS link, which can be used to add a proxy. Available only
     * for SOCKS5 and MTProto proxies. Can be called before authorization.
     *
     *
     * @return [TdApi.Text] Contains some text.
     */
    suspend fun Proxy.getLink(): TdApi.Text = api.getProxyLink(id)

    /**
     * Suspend function, which computes time needed to receive a response from a Telegram server
     * through a proxy. Can be called before authorization.
     *
     *
     * @return [TdApi.Seconds] Contains a value representing a number of seconds.
     */
    suspend fun Proxy.ping(): TdApi.Seconds = api.pingProxy(id)

    /**
     * Suspend function, which removes a proxy server. Can be called before authorization.
     */
    suspend fun Proxy.remove(): Unit = api.removeProxy(id)
}