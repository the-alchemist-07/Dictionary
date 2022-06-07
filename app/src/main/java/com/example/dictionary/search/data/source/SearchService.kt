package com.example.dictionary.search.data.source

//import com.example.dictionary.search.data.dto.SearchResponseDto
import com.example.dictionary.search.data.dto.SearchResponseDtoItem
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface SearchService {

    @GET("api/v2/entries/en/{keyword}")
    suspend fun searchKeyword(@Path("keyword") keyword: String): ApiResponse<List<SearchResponseDtoItem>>
//    suspend fun searchKeyword(@Path("keyword") keyword: String): ApiResponse<SearchResponseDto>

}
