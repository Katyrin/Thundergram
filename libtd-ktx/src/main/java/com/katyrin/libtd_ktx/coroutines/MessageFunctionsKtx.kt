package com.katyrin.libtd_ktx.coroutines

import com.katyrin.libtd_ktx.core.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi.*

/**
 * Suspend function, which adds a local message to a chat. The message is persistent across
 * application restarts only if the message database is used. Returns the added message.
 *
 * @param chatId Target chat.
 * @param senderUserId Identifier of the user who will be shown as the sender of the message; may be
 * 0 for channel posts.
 * @param replyToMessageId Identifier of the message to reply to or 0.
 * @param disableNotification Pass true to disable notification for the message.
 * @param inputMessageContent The content of the message to be added.
 *
 * @return [Message] Describes a message.
 */
suspend fun TelegramFlow.addLocalMessage(
    chatId: Long,
    senderUserId: Int,
    replyToMessageId: Long,
    disableNotification: Boolean,
    inputMessageContent: InputMessageContent?
): Message = sendFunctionAsync(
    AddLocalMessage(
        chatId,
        senderUserId,
        replyToMessageId,
        disableNotification,
        inputMessageContent
    )
)

/**
 * Suspend function, which clears draft messages in all chats.
 *
 * @param excludeSecretChats If true, local draft messages in secret chats will not be cleared.
 */
suspend fun TelegramFlow.clearAllDraftMessages(excludeSecretChats: Boolean): Unit =
    sendFunctionLaunch(ClearAllDraftMessages(excludeSecretChats))

/**
 * Suspend function, which deletes messages.
 *
 * @param chatId Chat identifier.
 * @param messageIds Identifiers of the messages to be deleted.
 * @param revoke Pass true to try to delete messages for all chat members. Always true for
 * supergroups, channels and secret chats.
 */
suspend fun TelegramFlow.deleteMessages(
    chatId: Long,
    messageIds: LongArray?,
    revoke: Boolean
): Unit = sendFunctionLaunch(DeleteMessages(chatId, messageIds, revoke))

/**
 * Suspend function, which edits the caption of an inline message sent via a bot; for bots only.
 *
 * @param inlineMessageId Inline message identifier.
 * @param replyMarkup The new message reply markup.
 * @param caption New message content caption; 0-GetOption(&quot;message_caption_length_max&quot;)
 * characters.
 */
suspend fun TelegramFlow.editInlineMessageCaption(
    inlineMessageId: String?,
    replyMarkup: ReplyMarkup?,
    caption: FormattedText?
): Unit = sendFunctionLaunch(EditInlineMessageCaption(inlineMessageId, replyMarkup, caption))

/**
 * Suspend function, which edits the content of a live location in an inline message sent via a bot;
 * for bots only.
 *
 * @param inlineMessageId Inline message identifier.
 * @param replyMarkup The new message reply markup.
 * @param location New location content of the message; may be null. Pass null to stop sharing the
 * live location.
 */
suspend fun TelegramFlow.editInlineMessageLiveLocation(
    inlineMessageId: String?,
    replyMarkup: ReplyMarkup?,
    location: Location? = null
): Unit = sendFunctionLaunch(EditInlineMessageLiveLocation(inlineMessageId, replyMarkup, location))

/**
 * Suspend function, which edits the content of a message with an animation, an audio, a document, a
 * photo or a video in an inline message sent via a bot; for bots only.
 *
 * @param inlineMessageId Inline message identifier.
 * @param replyMarkup The new message reply markup; for bots only.
 * @param inputMessageContent New content of the message. Must be one of the following types:
 * InputMessageAnimation, InputMessageAudio, InputMessageDocument, InputMessagePhoto or
 * InputMessageVideo.
 */
suspend fun TelegramFlow.editInlineMessageMedia(
    inlineMessageId: String?,
    replyMarkup: ReplyMarkup?,
    inputMessageContent: InputMessageContent?
): Unit = sendFunctionLaunch(
    EditInlineMessageMedia(inlineMessageId, replyMarkup, inputMessageContent)
)

/**
 * Suspend function, which edits the reply markup of an inline message sent via a bot; for bots
 * only.
 *
 * @param inlineMessageId Inline message identifier.
 * @param replyMarkup The new message reply markup.
 */
suspend fun TelegramFlow.editInlineMessageReplyMarkup(
    inlineMessageId: String?,
    replyMarkup: ReplyMarkup?
): Unit = sendFunctionLaunch(EditInlineMessageReplyMarkup(inlineMessageId, replyMarkup))

/**
 * Suspend function, which edits the text of an inline text or game message sent via a bot; for bots
 * only.
 *
 * @param inlineMessageId Inline message identifier.
 * @param replyMarkup The new message reply markup.
 * @param inputMessageContent New text content of the message. Should be of type InputMessageText.
 */
suspend fun TelegramFlow.editInlineMessageText(
    inlineMessageId: String?,
    replyMarkup: ReplyMarkup?,
    inputMessageContent: InputMessageContent?
): Unit = sendFunctionLaunch(
    EditInlineMessageText(inlineMessageId, replyMarkup, inputMessageContent)
)

/**
 * Suspend function, which edits the message content caption. Returns the edited message after the
 * edit is completed on the server side.
 *
 * @param chatId The chat the message belongs to.
 * @param messageId Identifier of the message.
 * @param replyMarkup The new message reply markup; for bots only.
 * @param caption New message content caption; 0-GetOption(&quot;message_caption_length_max&quot;)
 * characters.
 *
 * @return [Message] Describes a message.
 */
suspend fun TelegramFlow.editMessageCaption(
    chatId: Long,
    messageId: Long,
    replyMarkup: ReplyMarkup?,
    caption: FormattedText?
): Message = sendFunctionAsync(EditMessageCaption(chatId, messageId, replyMarkup, caption))

/**
 * Suspend function, which edits the message content of a live location. Messages can be edited for
 * a limited period of time specified in the live location. Returns the edited message after the edit
 * is completed on the server side.
 *
 * @param chatId The chat the message belongs to.
 * @param messageId Identifier of the message.
 * @param replyMarkup The new message reply markup; for bots only.
 * @param location New location content of the message; may be null. Pass null to stop sharing the
 * live location.
 *
 * @return [Message] Describes a message.
 */
suspend fun TelegramFlow.editMessageLiveLocation(
    chatId: Long,
    messageId: Long,
    replyMarkup: ReplyMarkup?,
    location: Location? = null
): Message = sendFunctionAsync(EditMessageLiveLocation(chatId, messageId, replyMarkup, location))

/**
 * Suspend function, which edits the content of a message with an animation, an audio, a document, a
 * photo or a video. The media in the message can't be replaced if the message was set to
 * self-destruct. Media can't be replaced by self-destructing media. Media in an album can be edited
 * only to contain a photo or a video. Returns the edited message after the edit is completed on the
 * server side.
 *
 * @param chatId The chat the message belongs to.
 * @param messageId Identifier of the message.
 * @param replyMarkup The new message reply markup; for bots only.
 * @param inputMessageContent New content of the message. Must be one of the following types:
 * InputMessageAnimation, InputMessageAudio, InputMessageDocument, InputMessagePhoto or
 * InputMessageVideo.
 *
 * @return [Message] Describes a message.
 */
suspend fun TelegramFlow.editMessageMedia(
    chatId: Long,
    messageId: Long,
    replyMarkup: ReplyMarkup?,
    inputMessageContent: InputMessageContent?
): Message =
    sendFunctionAsync(EditMessageMedia(chatId, messageId, replyMarkup, inputMessageContent))

/**
 * Suspend function, which edits the message reply markup; for bots only. Returns the edited message
 * after the edit is completed on the server side.
 *
 * @param chatId The chat the message belongs to.
 * @param messageId Identifier of the message.
 * @param replyMarkup The new message reply markup.
 *
 * @return [Message] Describes a message.
 */
suspend fun TelegramFlow.editMessageReplyMarkup(
    chatId: Long,
    messageId: Long,
    replyMarkup: ReplyMarkup?
): Message = sendFunctionAsync(EditMessageReplyMarkup(chatId, messageId, replyMarkup))

/**
 * Suspend function, which edits the time when a scheduled message will be sent. Scheduling state of
 * all messages in the same album or forwarded together with the message will be also changed.
 *
 * @param chatId The chat the message belongs to.
 * @param messageId Identifier of the message.
 * @param schedulingState The new message scheduling state. Pass null to send the message
 * immediately.
 */
suspend fun TelegramFlow.editMessageSchedulingState(
    chatId: Long,
    messageId: Long,
    schedulingState: MessageSchedulingState?
): Unit = sendFunctionLaunch(EditMessageSchedulingState(chatId, messageId, schedulingState))

/**
 * Suspend function, which edits the text of a message (or a text of a game message). Returns the
 * edited message after the edit is completed on the server side.
 *
 * @param chatId The chat the message belongs to.
 * @param messageId Identifier of the message.
 * @param replyMarkup The new message reply markup; for bots only.
 * @param inputMessageContent New text content of the message. Should be of type InputMessageText.
 *
 * @return [Message] Describes a message.
 */
suspend fun TelegramFlow.editMessageText(
    chatId: Long,
    messageId: Long,
    replyMarkup: ReplyMarkup?,
    inputMessageContent: InputMessageContent?
): Message = sendFunctionAsync(EditMessageText(chatId, messageId, replyMarkup, inputMessageContent))

/**
 * Suspend function, which forwards previously sent messages. Returns the forwarded messages in the
 * same order as the message identifiers passed in messageIds. If a message can't be forwarded, null
 * will be returned instead of the message.
 *
 * @param chatId Identifier of the chat to which to forward messages.
 * @param fromChatId Identifier of the chat from which to forward messages.
 * @param messageIds Identifiers of the messages to forward.
 * @param options Options to be used to send the messages.
 * @param asAlbum True, if the messages should be grouped into an album after forwarding. For this
 * to work, no more than 10 messages may be forwarded, and all of them must be photo or video messages.
 *
 * @param sendCopy True, if content of the messages needs to be copied without links to the original
 * messages. Always true if the messages are forwarded to a secret chat.
 * @param removeCaption True, if media captions of message copies needs to be removed. Ignored if
 * sendCopy is false.
 *
 * @return [Messages] Contains a list of messages.
 */
suspend fun TelegramFlow.forwardMessages(
    chatId: Long,
    fromChatId: Long,
    messageIds: LongArray?,
    options: SendMessageOptions?,
    asAlbum: Boolean,
    sendCopy: Boolean,
    removeCaption: Boolean
): Messages = sendFunctionAsync(
    ForwardMessages(chatId, fromChatId, messageIds, options, asAlbum, sendCopy, removeCaption)
)

/**
 * Suspend function, which returns all active live locations that should be updated by the client.
 * The list is persistent across application restarts only if the message database is used.
 *
 * @return [Messages] Contains a list of messages.
 */
suspend fun TelegramFlow.getActiveLiveLocationMessages(): Messages =
    sendFunctionAsync(GetActiveLiveLocationMessages())

/**
 * Suspend function, which returns information about a message.
 *
 * @param chatId Identifier of the chat the message belongs to.
 * @param messageId Identifier of the message to get.
 *
 * @return [Message] Describes a message.
 */
suspend fun TelegramFlow.getMessage(chatId: Long, messageId: Long): Message =
    sendFunctionAsync(GetMessage(chatId, messageId))

/**
 * Suspend function, which returns a private HTTPS link to a message in a chat. Available only for
 * already sent messages in supergroups and channels. The link will work only for members of the chat.
 *
 * @param chatId Identifier of the chat to which the message belongs.
 * @param messageId Identifier of the message.
 *
 * @return [HttpUrl] Contains an HTTP URL.
 */
suspend fun TelegramFlow.getMessageLink(chatId: Long, messageId: Long): HttpUrl =
    sendFunctionAsync(GetMessageLink(chatId, messageId))

/**
 * Suspend function, which returns information about a public or private message link.
 *
 * @param url The message link in the format &quot;https://t.me/c/...&quot;, or
 * &quot;tg://privatepost?...&quot;, or &quot;https://t.me/username/...&quot;, or
 * &quot;tg://resolve?...&quot;.
 *
 * @return [MessageLinkInfo] Contains information about a link to a message in a chat.
 */
suspend fun TelegramFlow.getMessageLinkInfo(url: String?): MessageLinkInfo =
    sendFunctionAsync(GetMessageLinkInfo(url))

/**
 * Suspend function, which returns information about a message, if it is available locally without
 * sending network request. This is an offline request.
 *
 * @param chatId Identifier of the chat the message belongs to.
 * @param messageId Identifier of the message to get.
 *
 * @return [Message] Describes a message.
 */
suspend fun TelegramFlow.getMessageLocally(chatId: Long, messageId: Long): Message =
    sendFunctionAsync(GetMessageLocally(chatId, messageId))

/**
 * Suspend function, which returns information about messages. If a message is not found, returns
 * null on the corresponding position of the result.
 *
 * @param chatId Identifier of the chat the messages belong to.
 * @param messageIds Identifiers of the messages to get.
 *
 * @return [Messages] Contains a list of messages.
 */
suspend fun TelegramFlow.getMessages(chatId: Long, messageIds: LongArray? = null): Messages =
    sendFunctionAsync(GetMessages(chatId, messageIds))

/**
 * Suspend function, which returns a public HTTPS link to a message. Available only for messages in
 * supergroups and channels with a username.
 *
 * @param chatId Identifier of the chat to which the message belongs.
 * @param messageId Identifier of the message.
 * @param forAlbum Pass true if a link for a whole media album should be returned.
 *
 * @return [PublicMessageLink] Contains a public HTTPS link to a message in a supergroup or channel
 * with a username.
 */
suspend fun TelegramFlow.getPublicMessageLink(
    chatId: Long,
    messageId: Long,
    forAlbum: Boolean
): PublicMessageLink = sendFunctionAsync(GetPublicMessageLink(chatId, messageId, forAlbum))

/**
 * Suspend function, which returns information about a message that is replied by given message.
 *
 * @param chatId Identifier of the chat the message belongs to.
 * @param messageId Identifier of the message reply to which get.
 *
 * @return [Message] Describes a message.
 */
suspend fun TelegramFlow.getRepliedMessage(chatId: Long, messageId: Long): Message =
    sendFunctionAsync(GetRepliedMessage(chatId, messageId))

/**
 * Suspend function, which informs TDLib that the message content has been opened (e.g., the user
 * has opened a photo, video, document, location or venue, or has listened to an audio file or voice
 * note message). An updateMessageContentOpened update will be generated if something has changed.
 *
 * @param chatId Chat identifier of the message.
 * @param messageId Identifier of the message with the opened content.
 */
suspend fun TelegramFlow.openMessageContent(chatId: Long, messageId: Long): Unit =
    sendFunctionLaunch(OpenMessageContent(chatId, messageId))

/**
 * Suspend function, which resends messages which failed to send. Can be called only for messages
 * for which messageSendingStateFailed.canRetry is true and after specified in
 * messageSendingStateFailed.retryAfter time passed. If a message is re-sent, the corresponding failed
 * to send message is deleted. Returns the sent messages in the same order as the message identifiers
 * passed in messageIds. If a message can't be re-sent, null will be returned instead of the message.
 *
 * @param chatId Identifier of the chat to send messages.
 * @param messageIds Identifiers of the messages to resend. Message identifiers must be in a
 * strictly increasing order.
 *
 * @return [Messages] Contains a list of messages.
 */
suspend fun TelegramFlow.resendMessages(chatId: Long, messageIds: LongArray?): Messages =
    sendFunctionAsync(ResendMessages(chatId, messageIds))

/**
 * Suspend function, which searches for messages in all chats except secret chats. Returns the
 * results in reverse chronological order (i.e., in order of decreasing (date, chatId, messageId)). For
 * optimal performance the number of returned messages is chosen by the library.
 *
 * @param chatList Chat list in which to search messages; pass null to search in all chats
 * regardless of their chat list.
 * @param query Query to search for.
 * @param offsetDate The date of the message starting from which the results should be fetched. Use
 * 0 or any date in the future to get results from the last message.
 * @param offsetChatId The chat identifier of the last found message, or 0 for the first request.
 * @param offsetMessageId The message identifier of the last found message, or 0 for the first
 * request.
 * @param limit The maximum number of messages to be returned, up to 100. Fewer messages may be
 * returned than specified by the limit, even if the end of the message history has not been reached.
 *
 * @return [Messages] Contains a list of messages.
 */
suspend fun TelegramFlow.searchMessages(
    chatList: ChatList?,
    query: String?,
    offsetDate: Int,
    offsetChatId: Long,
    offsetMessageId: Long,
    limit: Int
): Messages = sendFunctionAsync(
    SearchMessages(chatList, query, offsetDate, offsetChatId, offsetMessageId, limit)
)

/**
 * Suspend function, which searches for messages in secret chats. Returns the results in reverse
 * chronological order. For optimal performance the number of returned messages is chosen by the
 * library.
 *
 * @param chatId Identifier of the chat in which to search. Specify 0 to search in all secret chats.
 *
 * @param query Query to search for. If empty, searchChatMessages should be used instead.
 * @param fromSearchId The identifier from the result of a previous request, use 0 to get results
 * from the last message.
 * @param limit The maximum number of messages to be returned; up to 100. Fewer messages may be
 * returned than specified by the limit, even if the end of the message history has not been reached.
 * @param filter A filter for the content of messages in the search results.
 *
 * @return [FoundMessages] Contains a list of messages found by a search.
 */
suspend fun TelegramFlow.searchSecretMessages(
    chatId: Long,
    query: String?,
    fromSearchId: Long,
    limit: Int,
    filter: SearchMessagesFilter?
): FoundMessages = sendFunctionAsync(
    SearchSecretMessages(chatId, query, fromSearchId, limit, filter)
)

/**
 * Suspend function, which invites a bot to a chat (if it is not yet a member) and sends it the
 * /start command. Bots can't be invited to a private chat other than the chat with the bot. Bots can't
 * be invited to channels (although they can be added as admins) and secret chats. Returns the sent
 * message.
 *
 * @param botUserId Identifier of the bot.
 * @param chatId Identifier of the target chat.
 * @param parameter A hidden parameter sent to the bot for deep linking purposes
 * (https://core.telegram.org/bots#deep-linking).
 *
 * @return [Message] Describes a message.
 */
suspend fun TelegramFlow.sendBotStartMessage(
    botUserId: Int,
    chatId: Long,
    parameter: String?
): Message = sendFunctionAsync(SendBotStartMessage(botUserId, chatId, parameter))

/**
 * Suspend function, which sends a message. Returns the sent message.
 *
 * @param chatId Target chat.
 * @param replyToMessageId Identifier of the message to reply to or 0.
 * @param options Options to be used to send the message.
 * @param replyMarkup Markup for replying to the message; for bots only.
 * @param inputMessageContent The content of the message to be sent.
 *
 * @return [Message] Describes a message.
 */
suspend fun TelegramFlow.sendMessage(
    chatId: Long,
    replyToMessageId: Long = 0,
    options: SendMessageOptions? = null,
    replyMarkup: ReplyMarkup? = null,
    inputMessageContent: InputMessageContent?
): Message = sendFunctionAsync(
    SendMessage(chatId, replyToMessageId, options, replyMarkup, inputMessageContent)
)

/**
 * Suspend function, which sends messages grouped together into an album. Currently only photo and
 * video messages can be grouped into an album. Returns sent messages.
 *
 * @param chatId Target chat.
 * @param replyToMessageId Identifier of a message to reply to or 0.
 * @param options Options to be used to send the messages.
 * @param inputMessageContents Contents of messages to be sent.
 *
 * @return [Messages] Contains a list of messages.
 */
suspend fun TelegramFlow.sendMessageAlbum(
    chatId: Long,
    replyToMessageId: Long,
    options: SendMessageOptions?,
    inputMessageContents: Array<InputMessageContent>?
): Messages = sendFunctionAsync(
    SendMessageAlbum(chatId, replyToMessageId, options, inputMessageContents)
)

/**
 * Suspend function, which toggles sender signatures messages sent in a channel; requires
 * canChangeInfo rights.
 *
 * @param supergroupId Identifier of the channel.
 * @param signMessages New value of signMessages.
 */
suspend fun TelegramFlow.toggleSupergroupSignMessages(
    supergroupId: Int,
    signMessages: Boolean
): Unit = sendFunctionLaunch(ToggleSupergroupSignMessages(supergroupId, signMessages))

/**
 * Suspend function, which informs TDLib that messages are being viewed by the user. Many useful
 * activities depend on whether the messages are currently being viewed or not (e.g., marking messages
 * as read, incrementing a view counter, updating a view counter, removing deleted messages in
 * supergroups and channels).
 *
 * @param chatId Chat identifier.
 * @param messageIds The identifiers of the messages being viewed.
 * @param forceRead True, if messages in closed chats should be marked as read.
 */
suspend fun TelegramFlow.viewMessages(
    chatId: Long,
    messageIds: LongArray?,
    forceRead: Boolean
): Unit = sendFunctionLaunch(ViewMessages(chatId, messageIds, forceRead))