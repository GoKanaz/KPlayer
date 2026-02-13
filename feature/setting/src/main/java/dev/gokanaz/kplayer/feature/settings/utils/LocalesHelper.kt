package dev.gokanaz.kplayer.feature.settings.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import java.util.Locale
import kotlin.collections.set

object LocalesHelper {
    
    private var cachedLocales: List<LocaleInfo>? = null
    private val localeMap = mutableMapOf<String, Locale>()
    
    data class LocaleInfo(
        val code: String,
        val displayName: String,
        val nativeName: String,
        val isRTL: Boolean,
        val flagEmoji: String
    )
    
    fun getSupportedLocales(context: Context): List<LocaleInfo> {
        if (cachedLocales != null) {
            return cachedLocales!!
        }
        
        val locales = mutableListOf<LocaleInfo>()
        val resources = context.resources
        val config = resources.configuration
        
        val availableLocales = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localesArray = Resources.getSystem().assets.locales
            localesArray?.map { Locale.forLanguageTag(it.replace("_", "-")) } ?: emptyList()
        } else {
            listOf(
                Locale.ENGLISH,
                Locale.US,
                Locale.UK,
                Locale("in", "ID"),
                Locale.JAPANESE,
                Locale.KOREAN,
                Locale.CHINESE,
                Locale.FRENCH,
                Locale.GERMAN,
                Locale.ITALIAN,
                Locale("es", "ES"),
                Locale("pt", "BR"),
                Locale("ru", "RU"),
                Locale("ar", "SA"),
                Locale("hi", "IN")
            )
        }
        
        val addedCodes = mutableSetOf<String>()
        
        availableLocales.distinctBy { it.language }.forEach { locale ->
            try {
                val code = locale.toLanguageTag()
                if (code.isNotEmpty() && !addedCodes.contains(code)) {
                    val displayName = getLocaleDisplayName(locale, inNativeLanguage = false)
                    val nativeName = getLocaleDisplayName(locale, inNativeLanguage = true)
                    val isRTL = isRTL(locale)
                    val flagEmoji = getFlagEmoji(locale)
                    
                    locales.add(
                        LocaleInfo(
                            code = code,
                            displayName = displayName,
                            nativeName = nativeName,
                            isRTL = isRTL,
                            flagEmoji = flagEmoji
                        )
                    )
                    addedCodes.add(code)
                    localeMap[code] = locale
                }
            } catch (e: Exception) {
            }
        }
        
        val sortedLocales = locales.sortedBy { it.displayName }
        cachedLocales = sortedLocales
        return sortedLocales
    }
    
    fun getLocaleDisplayName(locale: Locale, inNativeLanguage: Boolean): String {
        return if (inNativeLanguage) {
            locale.getDisplayName(locale)
        } else {
            locale.getDisplayName(Locale.ENGLISH)
        }.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ENGLISH) else it.toString() }
    }
    
    fun getCurrentLocale(context: Context): Locale {
        return ConfigurationCompat.getLocales(context.resources.configuration)[0] ?: Locale.getDefault()
    }
    
    fun setLocale(context: Context, localeCode: String): Context {
        val locale = localeMap[localeCode] ?: Locale.forLanguageTag(localeCode)
        return updateConfiguration(context, locale)
    }
    
    fun isRTL(locale: Locale): Boolean {
        return when (locale.language) {
            "ar", "fa", "he", "iw", "ur", "yi" -> true
            else -> false
        }
    }
    
    fun getFlagEmoji(locale: Locale): String {
        val countryCode = locale.country.uppercase(Locale.ENGLISH)
        if (countryCode.length != 2) return ""
        
        val firstChar = countryCode[0]
        val secondChar = countryCode[1]
        
        if (firstChar !in 'A'..'Z' || secondChar !in 'A'..'Z') return ""
        
        val flag = StringBuilder()
        flag.appendCodePoint(0x1F1E6 + (firstChar.code - 'A'.code))
        flag.appendCodePoint(0x1F1E6 + (secondChar.code - 'A'.code))
        return flag.toString()
    }
    
    fun getStringForLocale(context: Context, resId: Int, locale: Locale): String {
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        val localizedContext = context.createConfigurationContext(config)
        return localizedContext.getString(resId)
    }
    
    fun getQuantityStringForLocale(
        context: Context,
        resId: Int,
        quantity: Int,
        locale: Locale
    ): String {
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        val localizedContext = context.createConfigurationContext(config)
        return localizedContext.resources.getQuantityString(resId, quantity, quantity)
    }
    
    fun formatNumberForLocale(number: Number, locale: Locale): String {
        return String.format(locale, "%,d", number)
    }
    
    fun getLanguagePreferenceOptions(context: Context): List<Pair<String, String>> {
        val options = mutableListOf<Pair<String, String>>()
        options.add("system" to "Follow System")
        
        getSupportedLocales(context).forEach { localeInfo ->
            options.add(localeInfo.code to "${localeInfo.flagEmoji} ${localeInfo.displayName} (${localeInfo.nativeName})")
        }
        
        return options
    }
    
    fun getCurrentLanguagePreference(context: Context): Pair<String, String> {
        val currentLocale = getCurrentLocale(context)
        val code = currentLocale.toLanguageTag()
        val displayName = getLocaleDisplayName(currentLocale, inNativeLanguage = false)
        val flagEmoji = getFlagEmoji(currentLocale)
        
        return code to "$flagEmoji $displayName"
    }
    
    fun applyLanguagePreference(context: Context, languageCode: String): Context {
        return if (languageCode == "system") {
            context
        } else {
            setLocale(context, languageCode)
        }
    }
    
    fun updateConfiguration(context: Context, locale: Locale): Context {
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.createConfigurationContext(config)
        } else {
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            return context
        }
    }
    
    fun persistLocale(context: Context, localeCode: String) {
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
            .edit()
            .putString("selected_language", localeCode)
            .apply()
    }
    
    fun loadPersistedLocale(context: Context): String {
        return context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
            .getString("selected_language", "system") ?: "system"
    }
    
    fun clearCache() {
        cachedLocales = null
        localeMap.clear()
    }
}
