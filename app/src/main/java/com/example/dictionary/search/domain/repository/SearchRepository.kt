package com.example.dictionary.search.domain.repository

import com.example.dictionary.common.Resource
import com.example.dictionary.search.domain.model.SearchResponse
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun searchKeyword(keyword: String): Flow<Resource<SearchResponse>>

}