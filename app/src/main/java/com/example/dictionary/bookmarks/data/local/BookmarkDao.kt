package com.example.dictionary.bookmarks.data.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("SELECT * FROM bookmark_table")
    fun getBookmarks(): Flow<List<BookmarkEntity>>

}