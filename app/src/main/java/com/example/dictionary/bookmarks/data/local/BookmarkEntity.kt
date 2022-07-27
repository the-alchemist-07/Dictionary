package com.example.dictionary.bookmarks.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dictionary.search.domain.model.SearchResponse

@Entity(tableName = "bookmark_table")
data class BookmarkEntity(
    @PrimaryKey
    val word: String,
    val meanings: List<SearchResponse.MeaningModel>,
    val phonetics: List<SearchResponse.PhoneticModel>,
    val sourceUrls: List<String>
)