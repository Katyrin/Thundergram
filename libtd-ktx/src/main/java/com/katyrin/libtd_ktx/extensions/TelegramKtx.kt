package com.katyrin.libtd_ktx.extensions

import com.katyrin.libtd_ktx.core.TelegramFlow

/**
 * Interface for access all Telegram objects extension functions. Contains 182 extensions
 */
interface TelegramKtx : BasicGroupKtx, CallKtx, ChatKtx, FileKtx, MessageKtx, NotificationGroupKtx,
    ProxyKtx, SecretChatKtx, SupergroupKtx, UserKtx, CommonKtx {
  /**
   * Instance of the [TelegramFlow] connecting extensions to the Telegram Client
   */
  override val api: TelegramFlow
}
