package com.steven.scannerapp.ui.scanner

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steven.scannerapp.data.api.ImageAnalyzer
import com.steven.scannerapp.data.model.Item
import com.steven.scannerapp.data.repository.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ScannerViewModel @Inject constructor(private val itemRepository: ItemRepository) :
    ViewModel() {

    val takenImage = null

    @ExperimentalGetImage
    fun processImage(takenImage: ImageProxy) {
        val analyzer = ImageAnalyzer()
        analyzer.analyze(takenImage)
    }

    fun insert(item: Item) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            itemRepository.insert(item)
        }
    }

}