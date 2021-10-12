package com.katyrin.libtd_ktx.flows

import com.katyrin.libtd_ktx.core.TelegramFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Chat
import org.drinkless.td.libcore.telegram.TdApi.SecretChat
import org.drinkless.td.libcore.telegram.TdApi.UpdateChatActionBar
import org.drinkless.td.libcore.telegram.TdApi.UpdateChatChatList
import org.drinkless.td.libcore.telegram.TdApi.UpdateChatDefaultDisableNotification
import org.drinkless.td.libcore.telegram.TdApi.UpdateChatDraftMessage
import org.drinkless.td.libcore.telegram.TdApi.UpdateChatHasScheduledMessages
import org.drinkless.td.libcore.telegram.TdApi.UpdateChatIsMarkedAsUnread
import org.drinkless.td.libcore.telegram.TdApi.UpdateChatIsPinned
import org.drinkless.td.libcore.telegram.TdApi.UpdateChatIsSponsored
import org.drinkless.td.libcore.telegram.TdApi.UpdateChatLastMessage
import org.drinkless.td.libcore.telegram.TdApi.UpdateChatNotificationSettings
import org.drinkless.td.libcore.telegram.TdApi.UpdateChatOnlineMemberCount
import org.drinkless.td.libcore.telegram.TdApi.UpdateChatOrder
import org.drinkless.td.libcore.telegram.TdApi.UpdateChatPermissions
import org.drinkless.td.libcore.telegram.TdApi.UpdateChatPhoto
import org.drinkless.td.libcore.telegram.TdApi.UpdateChatPinnedMessage
import org.drinkless.td.libcore.telegram.TdApi.UpdateChatReadInbox
import org.drinkless.td.libcore.telegram.TdApi.UpdateChatReadOutbox
import org.drinkless.td.libcore.telegram.TdApi.UpdateChatReplyMarkup
import org.drinkless.td.libcore.telegram.TdApi.UpdateChatTitle
import org.drinkless.td.libcore.telegram.TdApi.UpdateChatUnreadMentionCount
import org.drinkless.td.libcore.telegram.TdApi.UpdateUnreadChatCount
import org.drinkless.td.libcore.telegram.TdApi.UpdateUserChatAction

/**
 * emits [Chat] if a new chat has been loaded/created. This update is guaranteed to come before the
 * chat identifier is returned to the client. The chat field changes will be reported through separate
 * updates.
 */
fun TelegramFlow.newChatFlow(): Flow<Chat> =
    getUpdatesFlowOfType<TdApi.UpdateNewChat>().mapNotNull { it.chat }

/**
 * emits [UpdateChatChatList] if the list to which the chat belongs was changed. This update is
 * guaranteed to be sent only when chat.order == 0 and the current or the new chat list is null.
 */
fun TelegramFlow.chatChatListFlow(): Flow<UpdateChatChatList> = getUpdatesFlowOfType()

/**
 * emits [UpdateChatTitle] if the title of a chat was changed.
 */
fun TelegramFlow.chatTitleFlow(): Flow<UpdateChatTitle> = getUpdatesFlowOfType()

/**
 * emits [UpdateChatPhoto] if a chat photo was changed.
 */
fun TelegramFlow.chatPhotoFlow(): Flow<UpdateChatPhoto> = getUpdatesFlowOfType()

/**
 * emits [UpdateChatPermissions] if chat permissions was changed.
 */
fun TelegramFlow.chatPermissionsFlow(): Flow<UpdateChatPermissions> = getUpdatesFlowOfType()

/**
 * emits [UpdateChatLastMessage] if the last message of a chat was changed. If lastMessage is null,
 * then the last message in the chat became unknown. Some new unknown messages might be added to the
 * chat in this case.
 */
fun TelegramFlow.chatLastMessageFlow(): Flow<UpdateChatLastMessage> = getUpdatesFlowOfType()

/**
 * emits [UpdateChatOrder] if the order of the chat in the chat list has changed. Instead of this
 * update updateChatLastMessage, updateChatIsPinned, updateChatDraftMessage, or updateChatIsSponsored
 * might be sent.
 */
fun TelegramFlow.chatOrderFlow(): Flow<UpdateChatOrder> = getUpdatesFlowOfType()

/**
 * emits [UpdateChatIsPinned] if a chat was pinned or unpinned.
 */
fun TelegramFlow.chatIsPinnedFlow(): Flow<UpdateChatIsPinned> = getUpdatesFlowOfType()

/**
 * emits [UpdateChatIsMarkedAsUnread] if a chat was marked as unread or was read.
 */
fun TelegramFlow.chatIsMarkedAsUnreadFlow(): Flow<UpdateChatIsMarkedAsUnread> =
    getUpdatesFlowOfType()

/**
 * emits [UpdateChatIsSponsored] if a chat's isSponsored field has changed.
 */
fun TelegramFlow.chatIsSponsoredFlow(): Flow<UpdateChatIsSponsored> = getUpdatesFlowOfType()

/**
 * emits [UpdateChatHasScheduledMessages] if a chat's hasScheduledMessages field has changed.
 */
fun TelegramFlow.chatHasScheduledMessagesFlow(): Flow<UpdateChatHasScheduledMessages> =
    getUpdatesFlowOfType()

/**
 * emits [UpdateChatDefaultDisableNotification] if the value of the default disableNotification
 * parameter, used when a message is sent to the chat, was changed.
 */
fun TelegramFlow.chatDefaultDisableNotificationFlow(): Flow<UpdateChatDefaultDisableNotification> =
    getUpdatesFlowOfType()

/**
 * emits [UpdateChatReadInbox] if incoming messages were read or number of unread messages has been
 * changed.
 */
fun TelegramFlow.chatReadInboxFlow(): Flow<UpdateChatReadInbox> = getUpdatesFlowOfType()

/**
 * emits [UpdateChatReadOutbox] if outgoing messages were read.
 */
fun TelegramFlow.chatReadOutboxFlow(): Flow<UpdateChatReadOutbox> = getUpdatesFlowOfType()

/**
 * emits [UpdateChatUnreadMentionCount] if the chat unreadMentionCount has changed.
 */
fun TelegramFlow.chatUnreadMentionCountFlow(): Flow<UpdateChatUnreadMentionCount> =
    getUpdatesFlowOfType()

/**
 * emits [UpdateChatNotificationSettings] if notification settings for a chat were changed.
 */
fun TelegramFlow.chatNotificationSettingsFlow(): Flow<UpdateChatNotificationSettings> =
    getUpdatesFlowOfType()

/**
 * emits [UpdateChatActionBar] if the chat action bar was changed.
 */
fun TelegramFlow.chatActionBarFlow(): Flow<UpdateChatActionBar> = getUpdatesFlowOfType()

/**
 * emits [UpdateChatPinnedMessage] if the chat pinned message was changed.
 */
fun TelegramFlow.chatPinnedMessageFlow(): Flow<UpdateChatPinnedMessage> = getUpdatesFlowOfType()

/**
 * emits [UpdateChatReplyMarkup] if the default chat reply markup was changed. Can occur because new
 * messages with reply markup were received or because an old reply markup was hidden by the user.
 */
fun TelegramFlow.chatReplyMarkupFlow(): Flow<UpdateChatReplyMarkup> = getUpdatesFlowOfType()

/**
 * emits [UpdateChatDraftMessage] if a chat draft has changed. Be aware that the update may come in
 * the currently opened chat but with old content of the draft. If the user has changed the content of
 * the draft, this update shouldn't be applied.
 */
fun TelegramFlow.chatDraftMessageFlow(): Flow<UpdateChatDraftMessage> = getUpdatesFlowOfType()

/**
 * emits [UpdateChatOnlineMemberCount] if the number of online group members has changed. This
 * update with non-zero count is sent only for currently opened chats. There is no guarantee that it
 * will be sent just after the count has changed.
 */
fun TelegramFlow.chatOnlineMemberCountFlow(): Flow<UpdateChatOnlineMemberCount> =
    getUpdatesFlowOfType()

/**
 * emits [UpdateUserChatAction] if user activity in the chat has changed.
 */
fun TelegramFlow.userChatActionFlow(): Flow<UpdateUserChatAction> = getUpdatesFlowOfType()

/**
 * emits [SecretChat] if some data of a secret chat has changed. This update is guaranteed to come
 * before the secret chat identifier is returned to the client.
 */
fun TelegramFlow.secretChatFlow(): Flow<SecretChat> =
    getUpdatesFlowOfType<TdApi.UpdateSecretChat>().mapNotNull { it.secretChat }

/**
 * emits [UpdateUnreadChatCount] if number of unread chats, i.e. with unread messages or marked as
 * unread, has changed. This update is sent only if the message database is used.
 */
fun TelegramFlow.unreadChatCountFlow(): Flow<UpdateUnreadChatCount> = getUpdatesFlowOfType()