package com.ucb.data

import com.ucb.data.book.IFavoriteBookDataSource
import com.ucb.domain.Book

class FavoriteBookRepository(private val dataSource: IFavoriteBookDataSource) {

    suspend fun saveFavoriteBook(book: Book): Long {
        return dataSource.saveFavoriteBook(book)
    }

    suspend fun getAllFavoriteBooks(): List<Book> {
        return dataSource.getAllFavoriteBooks()
    }

    suspend fun isBookFavorite(key: String): Boolean {
        return dataSource.isBookFavorite(key)
    }

    suspend fun removeFavoriteBook(key: String): Boolean {
        return dataSource.removeFavoriteBook(key)
    }

    suspend fun toggleFavoriteBook(book: Book): Boolean {
        return if (dataSource.isBookFavorite(book.key)) {
            dataSource.removeFavoriteBook(book.key)
        } else {
            dataSource.saveFavoriteBook(book) > 0
        }
    }
}