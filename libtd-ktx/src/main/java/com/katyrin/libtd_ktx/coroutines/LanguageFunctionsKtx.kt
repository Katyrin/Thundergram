package com.katyrin.libtd_ktx.coroutines

import com.katyrin.libtd_ktx.core.TelegramFlow
import org.drinkless.td.libcore.telegram.TdApi.*

/**
 * Suspend function, which adds a custom server language pack to the list of installed language
 * packs in current localization target. Can be called before authorization.
 *
 * @param languagePackId Identifier of a language pack to be added; may be different from a name
 * that is used in an &quot;https://t.me/setlanguage/&quot; link.
 */
suspend fun TelegramFlow.addCustomServerLanguagePack(languagePackId: String?): Unit =
    sendFunctionLaunch(AddCustomServerLanguagePack(languagePackId))

/**
 * Suspend function, which deletes all information about a language pack in the current localization
 * target. The language pack which is currently in use (including base language pack) or is being
 * synchronized can't be deleted. Can be called before authorization.
 *
 * @param languagePackId Identifier of the language pack to delete.
 */
suspend fun TelegramFlow.deleteLanguagePack(languagePackId: String?): Unit =
    sendFunctionLaunch(DeleteLanguagePack(languagePackId))

/**
 * Suspend function, which edits information about a custom local language pack in the current
 * localization target. Can be called before authorization.
 *
 * @param info New information about the custom local language pack.
 */
suspend fun TelegramFlow.editCustomLanguagePackInfo(info: LanguagePackInfo?): Unit =
    sendFunctionLaunch(EditCustomLanguagePackInfo(info))

/**
 * Suspend function, which returns information about a language pack. Returned language pack
 * identifier may be different from a provided one. Can be called before authorization.
 *
 * @param languagePackId Language pack identifier.
 *
 * @return [LanguagePackInfo] Contains information about a language pack.
 */
suspend fun TelegramFlow.getLanguagePackInfo(languagePackId: String?): LanguagePackInfo =
    sendFunctionAsync(GetLanguagePackInfo(languagePackId))

/**
 * Suspend function, which returns a string stored in the local database from the specified
 * localization target and language pack by its key. Returns a 404 error if the string is not found.
 * This is an offline method. Can be called before authorization. Can be called synchronously.
 *
 * @param languagePackDatabasePath Path to the language pack database in which strings are stored.
 * @param localizationTarget Localization target to which the language pack belongs.
 * @param languagePackId Language pack identifier.
 * @param key Language pack key of the string to be returned.
 *
 * @return [LanguagePackStringValue] This class is an abstract base class.
 */
suspend fun TelegramFlow.getLanguagePackString(
    languagePackDatabasePath: String?,
    localizationTarget: String?,
    languagePackId: String?,
    key: String?
): LanguagePackStringValue = sendFunctionAsync(
    GetLanguagePackString(languagePackDatabasePath, localizationTarget, languagePackId, key)
)

/**
 * Suspend function, which returns strings from a language pack in the current localization target
 * by their keys. Can be called before authorization.
 *
 * @param languagePackId Language pack identifier of the strings to be returned.
 * @param keys Language pack keys of the strings to be returned; leave empty to request all
 * available strings.
 *
 * @return [LanguagePackStrings] Contains a list of language pack strings.
 */
suspend fun TelegramFlow.getLanguagePackStrings(
    languagePackId: String?,
    keys: Array<String>?
): LanguagePackStrings = sendFunctionAsync(GetLanguagePackStrings(languagePackId, keys))

/**
 * Suspend function, which returns an IETF language tag of the language preferred in the country,
 * which should be used to fill native fields in Telegram Passport personal details. Returns a 404
 * error if unknown.
 *
 * @param countryCode A two-letter ISO 3166-1 alpha-2 country code.
 *
 * @return [Text] Contains some text.
 */
suspend fun TelegramFlow.getPreferredCountryLanguage(countryCode: String?): Text =
    sendFunctionAsync(GetPreferredCountryLanguage(countryCode))

/**
 * Suspend function, which adds or changes a custom local language pack to the current localization
 * target.
 *
 * @param info Information about the language pack. Language pack ID must start with 'X', consist
 * only of English letters, digits and hyphens, and must not exceed 64 characters. Can be called before
 * authorization.
 * @param strings Strings of the new language pack.
 */
suspend fun TelegramFlow.setCustomLanguagePack(
    info: LanguagePackInfo?,
    strings: Array<LanguagePackString>?
): Unit = sendFunctionLaunch(SetCustomLanguagePack(info, strings))

/**
 * Suspend function, which adds, edits or deletes a string in a custom local language pack. Can be
 * called before authorization.
 *
 * @param languagePackId Identifier of a previously added custom local language pack in the current
 * localization target.
 * @param newString New language pack string.
 */
suspend fun TelegramFlow.setCustomLanguagePackString(
    languagePackId: String?,
    newString: LanguagePackString?
): Unit = sendFunctionLaunch(SetCustomLanguagePackString(languagePackId, newString))

/**
 * Suspend function, which fetches the latest versions of all strings from a language pack in the
 * current localization target from the server. This method doesn't need to be called explicitly for
 * the current used/base language packs. Can be called before authorization.
 *
 * @param languagePackId Language pack identifier.
 */
suspend fun TelegramFlow.synchronizeLanguagePack(languagePackId: String?): Unit =
    sendFunctionLaunch(SynchronizeLanguagePack(languagePackId))
