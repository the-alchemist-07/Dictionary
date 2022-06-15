package com.example.dictionary.search.presentation

import com.example.dictionary.search.domain.model.SearchResponse

sealed class SearchState {
    data class SearchSuccess(val searchResponse: SearchResponse): SearchState()
    data class Error(val message: String): SearchState()
    object Loading: SearchState()
    object Idle: SearchState()
}
