package com.steven.scannerapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.steven.scannerapp.data.dao.ItemDao
import com.steven.scannerapp.data.model.Item
import com.steven.scannerapp.utils.DataConverters

@Database(entities = [Item::class], version = 1, exportSchema = false)
@TypeConverters(DataConverters::class)
abstract class ScannerAppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}