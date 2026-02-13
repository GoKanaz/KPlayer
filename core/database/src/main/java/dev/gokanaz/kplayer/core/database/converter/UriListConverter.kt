package dev.gokanaz.kplayer.core.database.converter

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UriListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromUriList(uris: List<Uri>?): String? {
        return uris?.let { gson.toJson(it.map { uri -> uri.toString() }) }
    }

    @TypeConverter
    fun toUriList(data: String?): List<Uri>? {
        return data?.let {
            val type = object : TypeToken<List<String>>() {}.type
            val strings: List<String> = gson.fromJson(it, type)
            strings.map { str -> Uri.parse(str) }
        }
    }

    @TypeConverter
    fun fromStringList(strings: List<String>?): String? {
        return strings?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toStringList(data: String?): List<String>? {
        return data?.let {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(it, type)
        }
    }

    @TypeConverter
    fun fromStringMap(map: Map<String, String>?): String? {
        return map?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toStringMap(data: String?): Map<String, String>? {
        return data?.let {
            val type = object : TypeToken<Map<String, String>>() {}.type
            gson.fromJson(it, type)
        }
    }
}
