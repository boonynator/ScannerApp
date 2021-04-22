package com.steven.scannerapp.data.repository

import androidx.annotation.WorkerThread
import com.steven.scannerapp.data.dao.ItemDao
import com.steven.scannerapp.data.model.Item
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ItemRepository @Inject constructor(private val itemDao: ItemDao) {

    val allHistoryItems: Flow<List<Item>> = itemDao.getAllItems()

    @WorkerThread
    suspend fun insert(item: Item) {
        itemDao.insertItem(item)
    }
}