package com.ucb.usecases

import com.ucb.data.FavoriteBookRepository
import com.ucb.domain.Book
import javax.inject.Inject

class ToggleFavoriteBook @Inject constructor(
    private val favoriteBookRepository: FavoriteBookRepository
) {
    suspend fun invoke(book: Book): Boolean {
        return favoriteBookRepository.toggleFavoriteBook(book)
    }
}

class IsBookFavorite @Inject constructor(
    private val favoriteBookRepository: FavoriteBookRepository
) {
    suspend fun invoke(key: String): Boolean {
        return favoriteBookRepository.isBookFavorite(key)
    }
}

class GetAllFavoriteBooks @Inject constructor(
    private val favoriteBookRepository: FavoriteBookRepository
) {
    suspend fun invoke(): List<Book> {
        return favoriteBookRepository.getAllFavoriteBooks()
    }
}