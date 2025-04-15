package com.ucb.data.book

import com.ucb.domain.Book

interface IFavoriteBookDataSource {
    suspend fun saveFavoriteBook(book: Book): Long
    suspend fun getAllFavoriteBooks(): List<Book>
    suspend fun isBookFavorite(key: String): Boolean
    suspend fun removeFavoriteBook(key: String): Boolean
}