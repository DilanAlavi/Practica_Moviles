package com.ucb.framework.book

import com.ucb.data.NetworkResult
import com.ucb.data.book.IBookRemoteDataSource
import com.ucb.domain.Book
import com.ucb.framework.mappers.toModel
import com.ucb.framework.service.RetrofitBuilder

class BookRemoteDataSource(
    private val retrofitBuilder: RetrofitBuilder
) : IBookRemoteDataSource {

    override suspend fun searchBooks(query: String): NetworkResult<List<Book>> {
        val response = retrofitBuilder.bookApiService.searchBooks(query)
        return if (response.isSuccessful) {
            val books = response.body()?.docs?.map { it.toModel() } ?: emptyList()
            NetworkResult.Success(books)
        } else {
            NetworkResult.Error(response.message())
        }
    }
}