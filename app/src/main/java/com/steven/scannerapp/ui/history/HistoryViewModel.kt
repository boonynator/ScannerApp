package com.steven.scannerapp.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.steven.scannerapp.data.model.Item
import com.steven.scannerapp.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val itemRepository: ItemRepository) :
    ViewModel() {

    val allHistoryItems: LiveData<List<Item>> = itemRepository.allHistoryItems.asLiveData()

    fun insert(item: Item) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            itemRepository.insert(item)
        }
    }

}