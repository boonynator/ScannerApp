package com.steven.scannerapp.di.module

import android.content.Context
import androidx.room.Room
import com.steven.scannerapp.data.ScannerAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideItemDao(appDatabase: ScannerAppDatabase) = appDatabase.itemDao()

    @Singleton
    @Provides
    fun provideScannerDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context.applicationContext,
        ScannerAppDatabase::class.java,
        "item_database"
    ).build()

}