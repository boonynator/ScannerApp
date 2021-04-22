package com.steven.scannerapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.steven.scannerapp.data.model.Item
import dagger.Component
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM item")
    fun getAllItems(): Flow<List<Item>>

    @Insert
    fun insertItem(item: Item)

    @Delete
    fun deleteItem(item: Item)

    @Query("DELETE FROM item")
    fun deleteAll()

}