package com.steven.scannerapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URL
import java.util.*
import javax.inject.Inject

@Entity
data class Item @Inject constructor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val code: String,
    val date: Date,
    val url: URL
)