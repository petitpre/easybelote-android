package com.petitpre.easybelote.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.petitpre.easybelote.model.Declaration
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    val gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(data: String?): MutableSet<Declaration> {
        if (data == null) {
            return mutableSetOf()
        }
        return gson.fromJson(data, genericType<MutableSet<Declaration>>())
    }

    @TypeConverter
    fun someObjectListToString(someObjects: Set<Declaration>): String {
        return gson.toJson(someObjects)
    }
}

inline fun <reified T> genericType() = object : TypeToken<T>() {}.type