package com.katyrin.libtd_ktx.coroutines

import com.katyrin.libtd_ktx.core.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi.*

/**
 * Suspend function, which adds the specified data to data usage statistics. Can be called before
 * authorization.
 *
 * @param entry The network statistics entry with the data to be added to statistics.
 */
suspend fun TelegramFlow.addNetworkStatistics(entry: NetworkStatisticsEntry?): Unit =
    sendFunctionLaunch(AddNetworkStatistics(entry))

/**
 * Suspend function, which returns network data usage statistics. Can be called before
 * authorization.
 *
 * @param onlyCurrent If true, returns only data for the current library launch.
 *
 * @return [NetworkStatistics] A full list of available network statistic entries.
 */
suspend fun TelegramFlow.getNetworkStatistics(onlyCurrent: Boolean): NetworkStatistics =
    sendFunctionAsync(GetNetworkStatistics(onlyCurrent))

/**
 * Suspend function, which resets all network data usage statistics to zero. Can be called before
 * authorization.
 */
suspend fun TelegramFlow.resetNetworkStatistics(): Unit =
    sendFunctionLaunch(ResetNetworkStatistics())

/**
 * Suspend function, which sets the current network type. Can be called before authorization.
 * Calling this method forces all network connections to reopen, mitigating the delay in switching
 * between different networks, so it should be called whenever the network is changed, even if the
 * network type remains the same. Network type is used to check whether the library can use the network
 * at all and also for collecting detailed network data usage statistics.
 *
 * @param type The new network type. By default, networkTypeOther.
 */
suspend fun TelegramFlow.setNetworkType(type: NetworkType?): Unit =
    sendFunctionLaunch(SetNetworkType(type))

/**
 * Suspend function, which sends a simple network request to the Telegram servers; for testing only.
 * Can be called before authorization.
 */
suspend fun TelegramFlow.testNetwork(): Unit = sendFunctionLaunch(TestNetwork())