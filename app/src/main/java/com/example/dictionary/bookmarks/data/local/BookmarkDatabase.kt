package com.example.dictionary.bookmarks.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BookmarkEntity::class], version = 1, exportSchema = false)
abstract class BookmarkDatabase: RoomDatabase() {
    abstract val bookmarkDao: BookmarkDao
}