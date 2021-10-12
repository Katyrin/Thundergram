package com.katyrin.libtd_ktx.extensions

import com.katyrin.libtd_ktx.core.TelegramFlow
import com.katyrin.libtd_ktx.coroutines.*
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.*

/**
 * Interface for access [TdApi.Chat] extension functions. Can be used alongside with other extension
 * interfaces of the package. Must contain [TelegramFlow] instance field to access its functionality
 */
interface ChatKtx : BaseKtx {
    /**
     * Instance of the [TelegramFlow] connecting extensions to the Telegram Client
     */
    override val api: TelegramFlow

    /**
     * Suspend function, which adds a new member to a chat. Members can't be added to private or
     * secret chats. Members will not be added until the chat state has been synchronized with the
     * server.
     *
     * @param userId Identifier of the user.
     * @param forwardLimit The number of earlier messages from the chat to be forwarded to the new
     * member; up to 100. Ignored for supergroups and channels.
     */
    suspend fun Chat.addMember(userId: Int, forwardLimit: Int): Unit =
        api.addChatMember(id, userId, forwardLimit)

    /**
     * Suspend function, which adds multiple new members to a chat. Currently this option is only
     * available for supergroups and channels. This option can't be used to join a chat. Members can't be
     * added to a channel if it has more than 200 members. Members will not be added until the chat state
     * has been synchronized with the server.
     *
     * @param userIds Identifiers of the users to be added to the chat.
     */
    suspend fun Chat.addMembers(userIds: IntArray?): Unit = api.addChatMembers(id, userIds)

    /**
     * Suspend function, which adds a local message to a chat. The message is persistent across
     * application restarts only if the message database is used. Returns the added message.
     *
     * @param senderUserId Identifier of the user who will be shown as the sender of the message; may
     * be 0 for channel posts.
     * @param replyToMessageId Identifier of the message to reply to or 0.
     * @param disableNotification Pass true to disable notification for the message.
     * @param inputMessageContent The content of the message to be added.
     *
     * @return [TdApi.Message] Describes a message.
     */
    suspend fun Chat.addLocalMessage(
        senderUserId: Int,
        replyToMessageId: Long,
        disableNotification: Boolean,
        inputMessageContent: InputMessageContent?
    ): Message = api.addLocalMessage(
        id,
        senderUserId,
        replyToMessageId,
        disableNotification,
        inputMessageContent
    )

    /**
     * Suspend function, which adds a chat to the list of recently found chats. The chat is added to
     * the beginning of the list. If the chat is already in the list, it will be removed from the list
     * first.
     */
    suspend fun Chat.addRecentlyFound(): Unit = api.addRecentlyFoundChat(id)

    /**
     * Suspend function, which checks whether a username can be set for a chat.
     *
     * @param username Username to be checked.
     *
     * @return [TdApi.CheckChatUsernameResult] This class is an abstract base class.
     */
    suspend fun Chat.checkUsername(username: String?): CheckChatUsernameResult =
        api.checkChatUsername(id, username)

    /**
     * Suspend function, which informs TDLib that the chat is closed by the user. Many useful
     * activities depend on the chat being opened or closed.
     */
    suspend fun Chat.close(): Unit = api.closeChat(id)

    /**
     * Suspend function, which deletes all messages in the chat. Use Chat.canBeDeletedOnlyForSelf and
     * Chat.canBeDeletedForAllUsers fields to find whether and how the method can be applied to the chat.
     *
     * @param removeFromChatList Pass true if the chat should be removed from the chat list.
     * @param revoke Pass true to try to delete chat history for all users.
     */
    suspend fun Chat.deleteHistory(removeFromChatList: Boolean, revoke: Boolean): Unit =
        api.deleteChatHistory(id, removeFromChatList, revoke)

    /**
     * Suspend function, which deletes all messages sent by the specified user to a chat. Supported
     * only for supergroups; requires canDeleteMessages administrator privileges.
     *
     * @param userId User identifier.
     */
    suspend fun Chat.deleteMessagesFromUser(userId: Int): Unit =
        api.deleteChatMessagesFromUser(id, userId)

    /**
     * Suspend function, which deletes the default reply markup from a chat. Must be called after a
     * one-time keyboard or a ForceReply reply markup has been used. UpdateChatReplyMarkup will be sent
     * if the reply markup will be changed.
     *
     * @param messageId The message identifier of the used keyboard.
     */
    suspend fun Chat.deleteReplyMarkup(messageId: Long): Unit =
        api.deleteChatReplyMarkup(id, messageId)

    /**
     * Suspend function, which deletes messages.
     *
     * @param messageIds Identifiers of the messages to be deleted.
     * @param revoke Pass true to try to delete messages for all chat members. Always true for
     * supergroups, channels and secret chats.
     */
    suspend fun Chat.deleteMessages(messageIds: LongArray?, revoke: Boolean): Unit =
        api.deleteMessages(id, messageIds, revoke)

    /**
     * Suspend function, which edits the message content caption. Returns the edited message after the
     * edit is completed on the server side.
     *
     * @param messageId Identifier of the message.
     * @param replyMarkup The new message reply markup; for bots only.
     * @param caption New message content caption; 0-GetOption(&quot;message_caption_length_max&quot;)
     * characters.
     *
     * @return [TdApi.Message] Describes a message.
     */
    suspend fun Chat.editMessageCaption(
        messageId: Long,
        replyMarkup: ReplyMarkup?,
        caption: FormattedText?
    ): Message = api.editMessageCaption(id, messageId, replyMarkup, caption)

    /**
     * Suspend function, which edits the message content of a live location. Messages can be edited
     * for a limited period of time specified in the live location. Returns the edited message after the
     * edit is completed on the server side.
     *
     * @param messageId Identifier of the message.
     * @param replyMarkup The new message reply markup; for bots only.
     * @param location New location content of the message; may be null. Pass null to stop sharing the
     * live location.
     *
     * @return [TdApi.Message] Describes a message.
     */
    suspend fun Chat.editMessageLiveLocation(
        messageId: Long,
        replyMarkup: ReplyMarkup?,
        location: Location? = null
    ): Message = api.editMessageLiveLocation(id, messageId, replyMarkup, location)

    /**
     * Suspend function, which edits the content of a message with an animation, an audio, a document,
     * a photo or a video. The media in the message can't be replaced if the message was set to
     * self-destruct. Media can't be replaced by self-destructing media. Media in an album can be edited
     * only to contain a photo or a video. Returns the edited message after the edit is completed on the
     * server side.
     *
     * @param messageId Identifier of the message.
     * @param replyMarkup The new message reply markup; for bots only.
     * @param inputMessageContent New content of the message. Must be one of the following types:
     * InputMessageAnimation, InputMessageAudio, InputMessageDocument, InputMessagePhoto or
     * InputMessageVideo.
     *
     * @return [TdApi.Message] Describes a message.
     */
    suspend fun Chat.editMessageMedia(
        messageId: Long,
        replyMarkup: ReplyMarkup?,
        inputMessageContent: InputMessageContent?
    ): Message = api.editMessageMedia(id, messageId, replyMarkup, inputMessageContent)

    /**
     * Suspend function, which edits the message reply markup; for bots only. Returns the edited
     * message after the edit is completed on the server side.
     *
     * @param messageId Identifier of the message.
     * @param replyMarkup The new message reply markup.
     *
     * @return [TdApi.Message] Describes a message.
     */
    suspend fun Chat.editMessageReplyMarkup(messageId: Long, replyMarkup: ReplyMarkup?): Message =
        api.editMessageReplyMarkup(id, messageId, replyMarkup)

    /**
     * Suspend function, which edits the time when a scheduled message will be sent. Scheduling state
     * of all messages in the same album or forwarded together with the message will be also changed.
     *
     * @param messageId Identifier of the message.
     * @param schedulingState The new message scheduling state. Pass null to send the message
     * immediately.
     */
    suspend fun Chat.editMessageSchedulingState(
        messageId: Long,
        schedulingState: MessageSchedulingState?
    ): Unit = api.editMessageSchedulingState(id, messageId, schedulingState)

    /**
     * Suspend function, which edits the text of a message (or a text of a game message). Returns the
     * edited message after the edit is completed on the server side.
     *
     * @param messageId Identifier of the message.
     * @param replyMarkup The new message reply markup; for bots only.
     * @param inputMessageContent New text content of the message. Should be of type InputMessageText.
     *
     * @return [TdApi.Message] Describes a message.
     */
    suspend fun Chat.editMessageText(
        messageId: Long,
        replyMarkup: ReplyMarkup?,
        inputMessageContent: InputMessageContent?
    ): Message = api.editMessageText(id, messageId, replyMarkup, inputMessageContent)

    /**
     * Suspend function, which forwards previously sent messages. Returns the forwarded messages in
     * the same order as the message identifiers passed in messageIds. If a message can't be forwarded,
     * null will be returned instead of the message.
     *
     * @param fromChatId Identifier of the chat from which to forward messages.
     * @param messageIds Identifiers of the messages to forward.
     * @param options Options to be used to send the messages.
     * @param asAlbum True, if the messages should be grouped into an album after forwarding. For this
     * to work, no more than 10 messages may be forwarded, and all of them must be photo or video
     * messages.
     * @param sendCopy True, if content of the messages needs to be copied without links to the
     * original messages. Always true if the messages are forwarded to a secret chat.
     * @param removeCaption True, if media captions of message copies needs to be removed. Ignored if
     * sendCopy is false.
     *
     * @return [TdApi.Messages] Contains a list of messages.
     */
    suspend fun Chat.forwardMessages(
        fromChatId: Long,
        messageIds: LongArray?,
        options: SendMessageOptions?,
        asAlbum: Boolean,
        sendCopy: Boolean,
        removeCaption: Boolean
    ): Messages =
        api.forwardMessages(id, fromChatId, messageIds, options, asAlbum, sendCopy, removeCaption)

    /**
     * Suspend function, which generates a new invite link for a chat; the previously generated link
     * is revoked. Available for basic groups, supergroups, and channels. Requires administrator
     * privileges and canInviteUsers right.
     *
     *
     * @return [TdApi.ChatInviteLink] Contains a chat invite link.
     */
    suspend fun Chat.generateInviteLink(): ChatInviteLink = api.generateChatInviteLink(id)

    /**
     * Suspend function, which sends a callback query to a bot and returns an answer. Returns an error
     * with code 502 if the bot fails to answer the query before the query timeout expires.
     *
     * @param messageId Identifier of the message from which the query originated.
     * @param payload Query payload.
     *
     * @return [TdApi.CallbackQueryAnswer] Contains a bot's answer to a callback query.
     */
    suspend fun Chat.getCallbackQueryAnswer(
        messageId: Long,
        payload: CallbackQueryPayload?
    ): CallbackQueryAnswer = api.getCallbackQueryAnswer(id, messageId, payload)

    /**
     * Suspend function, which returns information about a chat by its identifier, this is an offline
     * request if the current user is not a bot.
     *
     *
     * @return [TdApi.Chat] A chat. (Can be a private chat, basic group, supergroup, or secret chat.)
     */
    suspend fun Chat.get(): Chat = api.getChat(id)

    /**
     * Suspend function, which returns a list of administrators of the chat with their custom titles.
     *
     *
     * @return [TdApi.ChatAdministrators] Represents a list of chat administrators.
     */
    suspend fun Chat.getAdministrators(): ChatAdministrators = api.getChatAdministrators(id)

    /**
     * Suspend function, which returns a list of service actions taken by chat members and
     * administrators in the last 48 hours. Available only for supergroups and channels. Requires
     * administrator rights. Returns results in reverse chronological order (i. e., in order of
     * decreasing eventId).
     *
     * @param query Search query by which to filter events.
     * @param fromEventId Identifier of an event from which to return results. Use 0 to get results
     * from the latest events.
     * @param limit The maximum number of events to return; up to 100.
     * @param filters The types of events to return. By default, all types will be returned.
     * @param userIds User identifiers by which to filter events. By default, events relating to all
     * users will be returned.
     *
     * @return [TdApi.ChatEvents] Contains a list of chat events.
     */
    suspend fun Chat.getEventLog(
        query: String?,
        fromEventId: Long,
        limit: Int,
        filters: ChatEventLogFilters?,
        userIds: IntArray?
    ): ChatEvents = api.getChatEventLog(id, query, fromEventId, limit, filters, userIds)

    /**
     * Suspend function, which returns messages in a chat. The messages are returned in a reverse
     * chronological order (i.e., in order of decreasing messageId). For optimal performance the number
     * of returned messages is chosen by the library. This is an offline request if onlyLocal is true.
     *
     * @param fromMessageId Identifier of the message starting from which history must be fetched; use
     * 0 to get results from the last message.
     * @param offset Specify 0 to get results from exactly the fromMessageId or a negative offset up
     * to 99 to get additionally some newer messages.
     * @param limit The maximum number of messages to be returned; must be positive and can't be
     * greater than 100. If the offset is negative, the limit must be greater or equal to -offset. Fewer
     * messages may be returned than specified by the limit, even if the end of the message history has
     * not been reached.
     * @param onlyLocal If true, returns only messages that are available locally without sending
     * network requests.
     *
     * @return [TdApi.Messages] Contains a list of messages.
     */
    suspend fun Chat.getHistory(
        fromMessageId: Long,
        offset: Int,
        limit: Int,
        onlyLocal: Boolean
    ): Messages = api.getChatHistory(id, fromMessageId, offset, limit, onlyLocal)

    /**
     * Suspend function, which returns information about a single member of a chat.
     *
     * @param userId User identifier.
     *
     * @return [TdApi.ChatMember] A user with information about joining/leaving a chat.
     */
    suspend fun Chat.getMember(userId: Int): ChatMember = api.getChatMember(id, userId)

    /**
     * Suspend function, which returns the last message sent in a chat no later than the specified
     * date.
     *
     * @param date Point in time (Unix timestamp) relative to which to search for messages.
     *
     * @return [TdApi.Message] Describes a message.
     */
    suspend fun Chat.getMessageByDate(date: Int): Message = api.getChatMessageByDate(id, date)

    /**
     * Suspend function, which returns approximate number of messages of the specified type in the
     * chat.
     *
     * @param filter Filter for message content; searchMessagesFilterEmpty is unsupported in this
     * function.
     * @param returnLocal If true, returns count that is available locally without sending network
     * requests, returning -1 if the number of messages is unknown.
     *
     * @return [TdApi.Count] Contains a counter.
     */
    suspend fun Chat.getMessageCount(filter: SearchMessagesFilter?, returnLocal: Boolean): Count =
        api.getChatMessageCount(id, filter, returnLocal)

    /**
     * Suspend function, which returns information about a pinned chat message.
     *
     *
     * @return [TdApi.Message] Describes a message.
     */
    suspend fun Chat.getPinnedMessage(): Message = api.getChatPinnedMessage(id)

    /**
     * Suspend function, which returns all scheduled messages in a chat. The messages are returned in
     * a reverse chronological order (i.e., in order of decreasing messageId).
     *
     *
     * @return [TdApi.Messages] Contains a list of messages.
     */
    suspend fun Chat.getScheduledMessages(): Messages = api.getChatScheduledMessages(id)

    /**
     * Suspend function, which returns an HTTP URL with the chat statistics. Currently this method can
     * be used only for channels. Can be used only if SupergroupFullInfo.canViewStatistics == true.
     *
     * @param parameters Parameters from &quot;tg://statsrefresh?params=******&quot; link.
     * @param isDark Pass true if a URL with the dark theme must be returned.
     *
     * @return [TdApi.HttpUrl] Contains an HTTP URL.
     */
    suspend fun Chat.getStatisticsUrl(parameters: String?, isDark: Boolean): HttpUrl =
        api.getChatStatisticsUrl(id, parameters, isDark)

    /**
     * Suspend function, which returns the high scores for a game and some part of the high score
     * table in the range of the specified user; for bots only.
     *
     * @param messageId Identifier of the message.
     * @param userId User identifier.
     *
     * @return [TdApi.GameHighScores] Contains a list of game high scores.
     */
    suspend fun Chat.getGameHighScores(messageId: Long, userId: Int): GameHighScores =
        api.getGameHighScores(id, messageId, userId)

    /**
     * Suspend function, which sends an inline query to a bot and returns its results. Returns an
     * error with code 502 if the bot fails to answer the query before the query timeout expires.
     *
     * @param botUserId The identifier of the target bot.
     * @param userLocation Location of the user, only if needed.
     * @param query Text of the query.
     * @param offset Offset of the first entry to return.
     *
     * @return [TdApi.InlineQueryResults] Represents the results of the inline query. Use
     * sendInlineQueryResultMessage to send the result of the query.
     */
    suspend fun Chat.getInlineQueryResults(
        botUserId: Int,
        userLocation: Location?,
        query: String?,
        offset: String?
    ): InlineQueryResults = api.getInlineQueryResults(botUserId, id, userLocation, query, offset)

    /**
     * Suspend function, which returns an HTTP URL which can be used to automatically authorize the
     * user on a website after clicking an inline button of type inlineKeyboardButtonTypeLoginUrl. Use
     * the method getLoginUrlInfo to find whether a prior user confirmation is needed. If an error is
     * returned, then the button must be handled as an ordinary URL button.
     *
     * @param messageId Message identifier of the message with the button.
     * @param buttonId Button identifier.
     * @param allowWriteAccess True, if the user allowed the bot to send them messages.
     *
     * @return [TdApi.HttpUrl] Contains an HTTP URL.
     */
    suspend fun Chat.getLoginUrl(
        messageId: Long,
        buttonId: Int,
        allowWriteAccess: Boolean
    ): HttpUrl = api.getLoginUrl(id, messageId, buttonId, allowWriteAccess)

    /**
     * Suspend function, which returns information about a button of type
     * inlineKeyboardButtonTypeLoginUrl. The method needs to be called when the user presses the button.
     *
     * @param messageId Message identifier of the message with the button.
     * @param buttonId Button identifier.
     *
     * @return [TdApi.LoginUrlInfo] This class is an abstract base class.
     */
    suspend fun Chat.getLoginUrlInfo(messageId: Long, buttonId: Int): LoginUrlInfo =
        api.getLoginUrlInfo(id, messageId, buttonId)

    /**
     * Suspend function, which returns information about a file with a map thumbnail in PNG format.
     * Only map thumbnail files with size less than 1MB can be downloaded.
     *
     * @param location Location of the map center.
     * @param zoom Map zoom level; 13-20.
     * @param width Map width in pixels before applying scale; 16-1024.
     * @param height Map height in pixels before applying scale; 16-1024.
     * @param scale Map scale; 1-3.
     *
     * @return [TdApi.File] Represents a file.
     */
    suspend fun Chat.getMapThumbnailFile(
        location: Location?,
        zoom: Int,
        width: Int,
        height: Int,
        scale: Int
    ): File = api.getMapThumbnailFile(location, zoom, width, height, scale, id)

    /**
     * Suspend function, which returns information about a message.
     *
     * @param messageId Identifier of the message to get.
     *
     * @return [TdApi.Message] Describes a message.
     */
    suspend fun Chat.getMessage(messageId: Long): Message = api.getMessage(id, messageId)

    /**
     * Suspend function, which returns a private HTTPS link to a message in a chat. Available only for
     * already sent messages in supergroups and channels. The link will work only for members of the
     * chat.
     *
     * @param messageId Identifier of the message.
     *
     * @return [TdApi.HttpUrl] Contains an HTTP URL.
     */
    suspend fun Chat.getMessageLink(messageId: Long): HttpUrl = api.getMessageLink(id, messageId)

    /**
     * Suspend function, which returns information about a message, if it is available locally without
     * sending network request. This is an offline request.
     *
     * @param messageId Identifier of the message to get.
     *
     * @return [TdApi.Message] Describes a message.
     */
    suspend fun Chat.getMessageLocally(messageId: Long): Message =
        api.getMessageLocally(id, messageId)

    /**
     * Suspend function, which returns information about messages. If a message is not found, returns
     * null on the corresponding position of the result.
     *
     * @param messageIds Identifiers of the messages to get.
     *
     * @return [TdApi.Messages] Contains a list of messages.
     */
    suspend fun Chat.getMessages(messageIds: LongArray?): Messages = api.getMessages(id, messageIds)

    /**
     * Suspend function, which returns an invoice payment form. This method should be called when the
     * user presses inlineKeyboardButtonBuy.
     *
     * @param messageId Message identifier.
     *
     * @return [TdApi.PaymentForm] Contains information about an invoice payment form.
     */
    suspend fun Chat.getPaymentForm(messageId: Long): PaymentForm =
        api.getPaymentForm(id, messageId)

    /**
     * Suspend function, which returns information about a successful payment.
     *
     * @param messageId Message identifier.
     *
     * @return [TdApi.PaymentReceipt] Contains information about a successful payment.
     */
    suspend fun Chat.getPaymentReceipt(messageId: Long): PaymentReceipt =
        api.getPaymentReceipt(id, messageId)

    /**
     * Suspend function, which returns users voted for the specified option in a non-anonymous polls.
     * For the optimal performance the number of returned users is chosen by the library.
     *
     * @param messageId Identifier of the message containing the poll.
     * @param optionId 0-based identifier of the answer option.
     * @param offset Number of users to skip in the result; must be non-negative.
     * @param limit The maximum number of users to be returned; must be positive and can't be greater
     * than 50. Fewer users may be returned than specified by the limit, even if the end of the voter
     * list has not been reached.
     *
     * @return [TdApi.Users] Represents a list of users.
     */
    suspend fun Chat.getPollVoters(
        messageId: Long,
        optionId: Int,
        offset: Int,
        limit: Int
    ): Users = api.getPollVoters(id, messageId, optionId, offset, limit)

    /**
     * Suspend function, which returns a public HTTPS link to a message. Available only for messages
     * in supergroups and channels with a username.
     *
     * @param messageId Identifier of the message.
     * @param forAlbum Pass true if a link for a whole media album should be returned.
     *
     * @return [TdApi.PublicMessageLink] Contains a public HTTPS link to a message in a supergroup or
     * channel with a username.
     */
    suspend fun Chat.getPublicMessageLink(messageId: Long, forAlbum: Boolean): PublicMessageLink =
        api.getPublicMessageLink(id, messageId, forAlbum)

    /**
     * Suspend function, which returns information about a message that is replied by given message.
     *
     * @param messageId Identifier of the message reply to which get.
     *
     * @return [TdApi.Message] Describes a message.
     */
    suspend fun Chat.getRepliedMessage(messageId: Long): Message =
        api.getRepliedMessage(id, messageId)

    /**
     * Suspend function, which adds current user as a new member to a chat. Private and secret chats
     * can't be joined using this method.
     */
    suspend fun Chat.join(): Unit = api.joinChat(id)

    /**
     * Suspend function, which removes current user from chat members. Private and secret chats can't
     * be left using this method.
     */
    suspend fun Chat.leave(): Unit = api.leaveChat(id)

    /**
     * Suspend function, which informs TDLib that the chat is opened by the user. Many useful
     * activities depend on the chat being opened or closed (e.g., in supergroups and channels all
     * updates are received only for opened chats).
     */
    suspend fun Chat.open(): Unit = api.openChat(id)

    /**
     * Suspend function, which informs TDLib that the message content has been opened (e.g., the user
     * has opened a photo, video, document, location or venue, or has listened to an audio file or voice
     * note message). An updateMessageContentOpened update will be generated if something has changed.
     *
     * @param messageId Identifier of the message with the opened content.
     */
    suspend fun Chat.openMessageContent(messageId: Long): Unit =
        api.openMessageContent(id, messageId)

    /**
     * Suspend function, which pins a message in a chat; requires canPinMessages rights.
     *
     * @param messageId Identifier of the new pinned message.
     * @param disableNotification True, if there should be no notification about the pinned message.
     */
    suspend fun Chat.pinMessage(messageId: Long, disableNotification: Boolean): Unit =
        api.pinChatMessage(id, messageId, disableNotification)

    /**
     * Suspend function, which marks all mentions in a chat as read.
     */
    suspend fun Chat.readAllMentions(): Unit = api.readAllChatMentions(id)

    /**
     * Suspend function, which removes a chat action bar without any other action.
     */
    suspend fun Chat.removeActionBar(): Unit = api.removeChatActionBar(id)

    /**
     * Suspend function, which removes a chat from the list of recently found chats.
     */
    suspend fun Chat.removeRecentlyFound(): Unit = api.removeRecentlyFoundChat(id)

    /**
     * Suspend function, which removes a chat from the list of frequently used chats. Supported only
     * if the chat info database is enabled.
     *
     * @param category Category of frequently used chats.
     */
    suspend fun Chat.removeTop(category: TopChatCategory?): Unit = api.removeTopChat(category, id)

    /**
     * Suspend function, which reports a chat to the Telegram moderators. Supported only for
     * supergroups, channels, or private chats with bots, since other chats can't be checked by
     * moderators, or when the report is done from the chat action bar.
     *
     * @param reason The reason for reporting the chat.
     * @param messageIds Identifiers of reported messages, if any.
     */
    suspend fun Chat.report(reason: ChatReportReason?, messageIds: LongArray?): Unit =
        api.reportChat(id, reason, messageIds)

    /**
     * Suspend function, which resends messages which failed to send. Can be called only for messages
     * for which messageSendingStateFailed.canRetry is true and after specified in
     * messageSendingStateFailed.retryAfter time passed. If a message is re-sent, the corresponding
     * failed to send message is deleted. Returns the sent messages in the same order as the message
     * identifiers passed in messageIds. If a message can't be re-sent, null will be returned instead of
     * the message.
     *
     * @param messageIds Identifiers of the messages to resend. Message identifiers must be in a
     * strictly increasing order.
     *
     * @return [TdApi.Messages] Contains a list of messages.
     */
    suspend fun Chat.resendMessages(messageIds: LongArray?): Messages =
        api.resendMessages(id, messageIds)

    /**
     * Suspend function, which saves application log event on the server. Can be called before
     * authorization.
     *
     * @param type Event type.
     * @param data The log event data.
     */
    suspend fun Chat.saveApplicationLogEvent(type: String?, data: JsonValue?): Unit =
        api.saveApplicationLogEvent(type, id, data)

    /**
     * Suspend function, which searches for a specified query in the first name, last name and
     * username of the members of a specified chat. Requires administrator rights in channels.
     *
     * @param query Query to search for.
     * @param limit The maximum number of users to be returned.
     * @param filter The type of users to return. By default, chatMembersFilterMembers.
     *
     * @return [TdApi.ChatMembers] Contains a list of chat members.
     */
    suspend fun Chat.searchMembers(
        query: String?,
        limit: Int,
        filter: ChatMembersFilter?
    ): ChatMembers = api.searchChatMembers(id, query, limit, filter)

    /**
     * Suspend function, which searches for messages with given words in the chat. Returns the results
     * in reverse chronological order, i.e. in order of decreasing messageId. Cannot be used in secret
     * chats with a non-empty query (searchSecretMessages should be used instead), or without an enabled
     * message database. For optimal performance the number of returned messages is chosen by the
     * library.
     *
     * @param query Query to search for.
     * @param senderUserId If not 0, only messages sent by the specified user will be returned. Not
     * supported in secret chats.
     * @param fromMessageId Identifier of the message starting from which history must be fetched; use
     * 0 to get results from the last message.
     * @param offset Specify 0 to get results from exactly the fromMessageId or a negative offset to
     * get the specified message and some newer messages.
     * @param limit The maximum number of messages to be returned; must be positive and can't be
     * greater than 100. If the offset is negative, the limit must be greater than -offset. Fewer
     * messages may be returned than specified by the limit, even if the end of the message history has
     * not been reached.
     * @param filter Filter for message content in the search results.
     *
     * @return [TdApi.Messages] Contains a list of messages.
     */
    suspend fun Chat.searchMessages(
        query: String?,
        senderUserId: Int,
        fromMessageId: Long,
        offset: Int,
        limit: Int,
        filter: SearchMessagesFilter?
    ): Messages =
        api.searchChatMessages(id, query, senderUserId, fromMessageId, offset, limit, filter)

    /**
     * Suspend function, which returns information about the recent locations of chat members that
     * were sent to the chat. Returns up to 1 location message per user.
     *
     * @param limit The maximum number of messages to be returned.
     *
     * @return [TdApi.Messages] Contains a list of messages.
     */
    suspend fun Chat.searchRecentLocationMessages(limit: Int): Messages =
        api.searchChatRecentLocationMessages(id, limit)

    /**
     * Suspend function, which searches for messages in secret chats. Returns the results in reverse
     * chronological order. For optimal performance the number of returned messages is chosen by the
     * library.
     *
     * @param query Query to search for. If empty, searchChatMessages should be used instead.
     * @param fromSearchId The identifier from the result of a previous request, use 0 to get results
     * from the last message.
     * @param limit The maximum number of messages to be returned; up to 100. Fewer messages may be
     * returned than specified by the limit, even if the end of the message history has not been reached.
     *
     * @param filter A filter for the content of messages in the search results.
     *
     * @return [TdApi.FoundMessages] Contains a list of messages found by a search.
     */
    suspend fun Chat.searchSecretMessages(
        query: String?,
        fromSearchId: Long,
        limit: Int,
        filter: SearchMessagesFilter?
    ): FoundMessages = api.searchSecretMessages(id, query, fromSearchId, limit, filter)

    /**
     * Suspend function, which invites a bot to a chat (if it is not yet a member) and sends it the
     * /start command. Bots can't be invited to a private chat other than the chat with the bot. Bots
     * can't be invited to channels (although they can be added as admins) and secret chats. Returns the
     * sent message.
     *
     * @param botUserId Identifier of the bot.
     * @param parameter A hidden parameter sent to the bot for deep linking purposes
     * (https://core.telegram.org/bots#deep-linking).
     *
     * @return [TdApi.Message] Describes a message.
     */
    suspend fun Chat.sendBotStartMessage(botUserId: Int, parameter: String?): Message =
        api.sendBotStartMessage(botUserId, id, parameter)

    /**
     * Suspend function, which sends a notification about user activity in a chat.
     *
     * @param action The action description.
     */
    suspend fun Chat.sendAction(action: ChatAction?): Unit = api.sendChatAction(id, action)

    /**
     * Suspend function, which sends a notification about a screenshot taken in a chat. Supported only
     * in private and secret chats.
     */
    suspend fun Chat.sendScreenshotTakenNotification(): Unit =
        api.sendChatScreenshotTakenNotification(id)

    /**
     * Suspend function, which changes the current TTL setting (sets a new self-destruct timer) in a
     * secret chat and sends the corresponding message.
     *
     * @param ttl New TTL value, in seconds.
     *
     * @return [TdApi.Message] Describes a message.
     */
    suspend fun Chat.sendSetTtlMessage(ttl: Int): Message = api.sendChatSetTtlMessage(id, ttl)

    /**
     * Suspend function, which sends the result of an inline query as a message. Returns the sent
     * message. Always clears a chat draft message.
     *
     * @param replyToMessageId Identifier of a message to reply to or 0.
     * @param options Options to be used to send the message.
     * @param queryId Identifier of the inline query.
     * @param resultId Identifier of the inline result.
     * @param hideViaBot If true, there will be no mention of a bot, via which the message is sent.
     * Can be used only for bots GetOption(&quot;animation_search_bot_username&quot;),
     * GetOption(&quot;photo_search_bot_username&quot;) and
     * GetOption(&quot;venue_search_bot_username&quot;).
     *
     * @return [TdApi.Message] Describes a message.
     */
    suspend fun Chat.sendInlineQueryResultMessage(
        replyToMessageId: Long,
        options: SendMessageOptions?,
        queryId: Long,
        resultId: String?,
        hideViaBot: Boolean
    ): Message = api.sendInlineQueryResultMessage(
        id,
        replyToMessageId,
        options,
        queryId,
        resultId,
        hideViaBot
    )

    /**
     * Suspend function, which sends a message. Returns the sent message.
     *
     * @param replyToMessageId Identifier of the message to reply to or 0.
     * @param options Options to be used to send the message.
     * @param replyMarkup Markup for replying to the message; for bots only.
     * @param inputMessageContent The content of the message to be sent.
     *
     * @return [TdApi.Message] Describes a message.
     */
    suspend fun Chat.sendMessage(
        replyToMessageId: Long,
        options: SendMessageOptions?,
        replyMarkup: ReplyMarkup?,
        inputMessageContent: InputMessageContent?
    ): Message = api.sendMessage(id, replyToMessageId, options, replyMarkup, inputMessageContent)

    /**
     * Suspend function, which sends messages grouped together into an album. Currently only photo and
     * video messages can be grouped into an album. Returns sent messages.
     *
     * @param replyToMessageId Identifier of a message to reply to or 0.
     * @param options Options to be used to send the messages.
     * @param inputMessageContents Contents of messages to be sent.
     *
     * @return [TdApi.Messages] Contains a list of messages.
     */
    suspend fun Chat.sendMessageAlbum(
        replyToMessageId: Long,
        options: SendMessageOptions?,
        inputMessageContents: Array<InputMessageContent>?
    ): Messages = api.sendMessageAlbum(id, replyToMessageId, options, inputMessageContents)

    /**
     * Suspend function, which sends a filled-out payment form to the bot for final verification.
     *
     * @param messageId Message identifier.
     * @param orderInfoId Identifier returned by ValidateOrderInfo, or an empty string.
     * @param shippingOptionId Identifier of a chosen shipping option, if applicable.
     * @param credentials The credentials chosen by user for payment.
     *
     * @return [TdApi.PaymentResult] Contains the result of a payment request.
     */
    suspend fun Chat.sendPaymentForm(
        messageId: Long,
        orderInfoId: String?,
        shippingOptionId: String?,
        credentials: InputCredentials?
    ): PaymentResult =
        api.sendPaymentForm(id, messageId, orderInfoId, shippingOptionId, credentials)

    /**
     * Suspend function, which moves a chat to a different chat list. Current chat list of the chat
     * must ne non-null.
     *
     * @param chatList New chat list of the chat.
     */
    suspend fun Chat.setList(chatList: ChatList?): Unit = api.setChatChatList(id, chatList)

    /**
     * Suspend function, which changes client data associated with a chat.
     *
     * @param clientData New value of clientData.
     */
    suspend fun Chat.setClientData(clientData: String?): Unit =
        api.setChatClientData(id, clientData)

    /**
     * Suspend function, which changes information about a chat. Available for basic groups,
     * supergroups, and channels. Requires canChangeInfo rights.
     *
     * @param description New chat description; 0-255 characters.
     */
    suspend fun Chat.setDescription(description: String?): Unit =
        api.setChatDescription(id, description)

    /**
     * Suspend function, which changes the discussion group of a channel chat; requires canChangeInfo
     * rights in the channel if it is specified.
     *
     * @param discussionChatId Identifier of a new channel's discussion group. Use 0 to remove the
     * discussion group. Use the method getSuitableDiscussionChats to find all suitable groups. Basic
     * group chats needs to be first upgraded to supergroup chats. If new chat members don't have access
     * to old messages in the supergroup, then toggleSupergroupIsAllHistoryAvailable needs to be used
     * first to change that.
     */
    suspend fun Chat.setDiscussionGroup(discussionChatId: Long): Unit =
        api.setChatDiscussionGroup(id, discussionChatId)

    /**
     * Suspend function, which changes the draft message in a chat.
     *
     * @param draftMessage New draft message; may be null.
     */
    suspend fun Chat.setDraftMessage(draftMessage: DraftMessage? = null): Unit =
        api.setChatDraftMessage(id, draftMessage)

    /**
     * Suspend function, which changes the location of a chat. Available only for some location-based
     * supergroups, use supergroupFullInfo.canSetLocation to check whether the method is allowed to use.
     *
     * @param location New location for the chat; must be valid and not null.
     */
    suspend fun Chat.setLocation(location: ChatLocation?): Unit = api.setChatLocation(id, location)

    /**
     * Suspend function, which changes the status of a chat member, needs appropriate privileges. This
     * function is currently not suitable for adding new members to the chat and transferring chat
     * ownership; instead, use addChatMember or transferChatOwnership. The chat member status will not be
     * changed until it has been synchronized with the server.
     *
     * @param userId User identifier.
     * @param status The new status of the member in the chat.
     */
    suspend fun Chat.setMemberStatus(userId: Int, status: ChatMemberStatus?): Unit =
        api.setChatMemberStatus(id, userId, status)

    /**
     * Suspend function, which changes the notification settings of a chat. Notification settings of a
     * chat with the current user (Saved Messages) can't be changed.
     *
     * @param notificationSettings New notification settings for the chat. If the chat is muted for
     * more than 1 week, it is considered to be muted forever.
     */
    suspend fun Chat.setNotificationSettings(notificationSettings: ChatNotificationSettings?): Unit =
        api.setChatNotificationSettings(id, notificationSettings)

    /**
     * Suspend function, which changes the chat members permissions. Supported only for basic groups
     * and supergroups. Requires canRestrictMembers administrator right.
     *
     * @param permissions New non-administrator members permissions in the chat.
     */
    suspend fun Chat.setPermissions(permissions: ChatPermissions?): Unit =
        api.setChatPermissions(id, permissions)

    /**
     * Suspend function, which changes the photo of a chat. Supported only for basic groups,
     * supergroups and channels. Requires canChangeInfo rights. The photo will not be changed before
     * request to the server has been completed.
     *
     * @param photo New chat photo. You can use a zero InputFileId to delete the chat photo. Files
     * that are accessible only by HTTP URL are not acceptable.
     */
    suspend fun Chat.setPhoto(photo: InputFile?): Unit = api.setChatPhoto(id, photo)

    /**
     * Suspend function, which changes the slow mode delay of a chat. Available only for supergroups;
     * requires canRestrictMembers rights.
     *
     * @param slowModeDelay New slow mode delay for the chat; must be one of 0, 10, 30, 60, 300, 900,
     * 3600.
     */
    suspend fun Chat.setSlowModeDelay(slowModeDelay: Int): Unit =
        api.setChatSlowModeDelay(id, slowModeDelay)

    /**
     * Suspend function, which changes the chat title. Supported only for basic groups, supergroups
     * and channels. Requires canChangeInfo rights. The title will not be changed until the request to
     * the server has been completed.
     *
     * @param title New title of the chat; 1-128 characters.
     */
    suspend fun Chat.setTitle(title: String?): Unit = api.setChatTitle(id, title)

    /**
     * Suspend function, which updates the game score of the specified user in the game; for bots
     * only.
     *
     * @param messageId Identifier of the message.
     * @param editMessage True, if the message should be edited.
     * @param userId User identifier.
     * @param score The new score.
     * @param force Pass true to update the score even if it decreases. If the score is 0, the user
     * will be deleted from the high score table.
     *
     * @return [TdApi.Message] Describes a message.
     */
    suspend fun Chat.setGameScore(
        messageId: Long,
        editMessage: Boolean,
        userId: Int,
        score: Int,
        force: Boolean
    ): Message = api.setGameScore(id, messageId, editMessage, userId, score, force)

    /**
     * Suspend function, which changes the user answer to a poll. A poll in quiz mode can be answered
     * only once.
     *
     * @param messageId Identifier of the message containing the poll.
     * @param optionIds 0-based identifiers of answer options, chosen by the user. User can choose
     * more than 1 answer option only is the poll allows multiple answers.
     */
    suspend fun Chat.setPollAnswer(messageId: Long, optionIds: IntArray?): Unit =
        api.setPollAnswer(id, messageId, optionIds)

    /**
     * Suspend function, which stops a poll. A poll in a message can be stopped when the message has
     * canBeEdited flag set.
     *
     * @param messageId Identifier of the message containing the poll.
     * @param replyMarkup The new message reply markup; for bots only.
     */
    suspend fun Chat.stopPoll(messageId: Long, replyMarkup: ReplyMarkup?): Unit =
        api.stopPoll(id, messageId, replyMarkup)

    /**
     * Suspend function, which changes the value of the default disableNotification parameter, used
     * when a message is sent to a chat.
     *
     * @param defaultDisableNotification New value of defaultDisableNotification.
     */
    suspend fun Chat.toggleDefaultDisableNotification(defaultDisableNotification: Boolean): Unit =
        api.toggleChatDefaultDisableNotification(id, defaultDisableNotification)

    /**
     * Suspend function, which changes the marked as unread state of a chat.
     *
     * @param isMarkedAsUnread New value of isMarkedAsUnread.
     */
    suspend fun Chat.toggleIsMarkedAsUnread(isMarkedAsUnread: Boolean): Unit =
        api.toggleChatIsMarkedAsUnread(id, isMarkedAsUnread)

    /**
     * Suspend function, which changes the pinned state of a chat. You can pin up to
     * GetOption(&quot;pinned_chat_count_max&quot;)/GetOption(&quot;pinned_archived_chat_count_max&quot;)
     * non-secret chats and the same number of secret chats in the main/archive chat list.
     *
     * @param isPinned New value of isPinned.
     */
    suspend fun Chat.toggleIsPinned(isPinned: Boolean): Unit = api.toggleChatIsPinned(id, isPinned)

    /**
     * Suspend function, which changes the owner of a chat. The current user must be a current owner
     * of the chat. Use the method canTransferOwnership to check whether the ownership can be transferred
     * from the current session. Available only for supergroups and channel chats.
     *
     * @param userId Identifier of the user to which transfer the ownership. The ownership can't be
     * transferred to a bot or to a deleted user.
     * @param password The password of the current user.
     */
    suspend fun Chat.transferOwnership(userId: Int, password: String?): Unit =
        api.transferChatOwnership(id, userId, password)

    /**
     * Suspend function, which removes the pinned message from a chat; requires canPinMessages rights
     * in the group or channel.
     */
    suspend fun Chat.unpinMessage(): Unit = api.unpinChatMessage(id)

    /**
     * Suspend function, which creates a new supergroup from an existing basic group and sends a
     * corresponding messageChatUpgradeTo and messageChatUpgradeFrom; requires creator privileges.
     * Deactivates the original basic group.
     *
     *
     * @return [TdApi.Chat] A chat. (Can be a private chat, basic group, supergroup, or secret chat.)
     */
    suspend fun Chat.upgradeBasicGroupToSupergroup(): Chat =
        api.upgradeBasicGroupChatToSupergroupChat(id)

    /**
     * Suspend function, which validates the order information provided by a user and returns the
     * available shipping options for a flexible invoice.
     *
     * @param messageId Message identifier.
     * @param orderInfo The order information, provided by the user.
     * @param allowSave True, if the order information can be saved.
     *
     * @return [TdApi.ValidatedOrderInfo] Contains a temporary identifier of validated order
     * information, which is stored for one hour. Also contains the available shipping options.
     */
    suspend fun Chat.validateOrderInfo(
        messageId: Long,
        orderInfo: OrderInfo?,
        allowSave: Boolean
    ): ValidatedOrderInfo = api.validateOrderInfo(id, messageId, orderInfo, allowSave)

    /**
     * Suspend function, which informs TDLib that messages are being viewed by the user. Many useful
     * activities depend on whether the messages are currently being viewed or not (e.g., marking
     * messages as read, incrementing a view counter, updating a view counter, removing deleted messages
     * in supergroups and channels).
     *
     * @param messageIds The identifiers of the messages being viewed.
     * @param forceRead True, if messages in closed chats should be marked as read.
     */
    suspend fun Chat.viewMessages(messageIds: LongArray?, forceRead: Boolean): Unit =
        api.viewMessages(id, messageIds, forceRead)
}