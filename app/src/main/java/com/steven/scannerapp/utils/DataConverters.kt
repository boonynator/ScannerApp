package com.steven.scannerapp.utils

import androidx.room.TypeConverter
import java.net.URL
import java.util.*

class DataConverters {

    @TypeConverter
    fun dateFromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun urlFromString(string: String?): URL? {
        return string?.let { URL(it) }
    }

    @TypeConverter
    fun urlToString(url: URL?): String? {
        return url?.toString()
    }

}