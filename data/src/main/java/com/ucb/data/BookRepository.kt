package com.ucb.data


import com.ucb.data.book.IBookRemoteDataSource
import com.ucb.domain.Book

class BookRepository(
    private val remoteDataSource: IBookRemoteDataSource
) {
    suspend fun searchBooks(query: String): NetworkResult<List<Book>> {
        return remoteDataSource.searchBooks(query)
    }
}