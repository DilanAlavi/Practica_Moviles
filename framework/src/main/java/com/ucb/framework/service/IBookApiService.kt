package com.ucb.framework.service

import com.ucb.framework.dto.BookResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IBookApiService {
    @GET("/search.json")
    suspend fun searchBooks(@Query("q") query: String): Response<BookResponseDto>
}