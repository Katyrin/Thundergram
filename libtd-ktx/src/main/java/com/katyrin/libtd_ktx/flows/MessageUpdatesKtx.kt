package com.katyrin.libtd_ktx.flows

import com.katyrin.libtd_ktx.core.TelegramFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Message
import org.drinkless.td.libcore.telegram.TdApi.UpdateDeleteMessages
import org.drinkless.td.libcore.telegram.TdApi.UpdateMessageContent
import org.drinkless.td.libcore.telegram.TdApi.UpdateMessageContentOpened
import org.drinkless.td.libcore.telegram.TdApi.UpdateMessageEdited
import org.drinkless.td.libcore.telegram.TdApi.UpdateMessageLiveLocationViewed
import org.drinkless.td.libcore.telegram.TdApi.UpdateMessageMentionRead
import org.drinkless.td.libcore.telegram.TdApi.UpdateMessageSendAcknowledged
import org.drinkless.td.libcore.telegram.TdApi.UpdateMessageSendFailed
import org.drinkless.td.libcore.telegram.TdApi.UpdateMessageSendSucceeded
import org.drinkless.td.libcore.telegram.TdApi.UpdateMessageViews
import org.drinkless.td.libcore.telegram.TdApi.UpdateUnreadMessageCount

/**
 * emits [Message] if a new message was received; can also be an outgoing message.
 */
fun TelegramFlow.newMessageFlow(): Flow<Message> =
    getUpdatesFlowOfType<TdApi.UpdateNewMessage>().mapNotNull { it.message }

/**
 * emits [UpdateMessageSendAcknowledged] if a request to send a message has reached the Telegram
 * server. This doesn't mean that the message will be sent successfully or even that the send message
 * request will be processed. This update will be sent only if the option &quot;use_quick_ack&quot; is
 * set to true. This update may be sent multiple times for the same message.
 */
fun TelegramFlow.messageSendAcknowledgedFlow(): Flow<UpdateMessageSendAcknowledged> =
    getUpdatesFlowOfType()

/**
 * emits [UpdateMessageSendSucceeded] if a message has been successfully sent.
 */
fun TelegramFlow.messageSendSucceededFlow(): Flow<UpdateMessageSendSucceeded> =
    getUpdatesFlowOfType()

/**
 * emits [UpdateMessageSendFailed] if a message failed to send. Be aware that some messages being
 * sent can be irrecoverably deleted, in which case updateDeleteMessages will be received instead of
 * this update.
 */
fun TelegramFlow.messageSendFailedFlow(): Flow<UpdateMessageSendFailed> = getUpdatesFlowOfType()

/**
 * emits [UpdateMessageContent] if the message content has changed.
 */
fun TelegramFlow.messageContentFlow(): Flow<UpdateMessageContent> = getUpdatesFlowOfType()

/**
 * emits [UpdateMessageEdited] if a message was edited. Changes in the message content will come in
 * a separate updateMessageContent.
 */
fun TelegramFlow.messageEditedFlow(): Flow<UpdateMessageEdited> = getUpdatesFlowOfType()

/**
 * emits [UpdateMessageViews] if the view count of the message has changed.
 */
fun TelegramFlow.messageViewsFlow(): Flow<UpdateMessageViews> = getUpdatesFlowOfType()

/**
 * emits [UpdateMessageContentOpened] if the message content was opened. Updates voice note messages
 * to &quot;listened&quot;, video note messages to &quot;viewed&quot; and starts the TTL timer for
 * self-destructing messages.
 */
fun TelegramFlow.messageContentOpenedFlow(): Flow<UpdateMessageContentOpened> =
    getUpdatesFlowOfType()

/**
 * emits [UpdateMessageMentionRead] if a message with an unread mention was read.
 */
fun TelegramFlow.messageMentionReadFlow(): Flow<UpdateMessageMentionRead> = getUpdatesFlowOfType()

/**
 * emits [UpdateMessageLiveLocationViewed] if a message with a live location was viewed. When the
 * update is received, the client is supposed to update the live location.
 */
fun TelegramFlow.messageLiveLocationViewedFlow(): Flow<UpdateMessageLiveLocationViewed> =
    getUpdatesFlowOfType()

/**
 * emits [UpdateDeleteMessages] if some messages were deleted.
 */
fun TelegramFlow.deleteMessagesFlow(): Flow<UpdateDeleteMessages> = getUpdatesFlowOfType()

/**
 * emits [UpdateUnreadMessageCount] if number of unread messages in a chat list has changed. This
 * update is sent only if the message database is used.
 */
fun TelegramFlow.unreadMessageCountFlow(): Flow<UpdateUnreadMessageCount> = getUpdatesFlowOfType()