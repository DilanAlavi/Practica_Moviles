package com.ucb.framework.book

import android.content.Context
import com.ucb.data.book.IFavoriteBookDataSource
import com.ucb.domain.Book
import com.ucb.framework.mappers.toDomain
import com.ucb.framework.mappers.toFavoriteEntity
import com.ucb.framework.persistence.AppRoomDatabase
import com.ucb.framework.persistence.IFavoriteBookDAO

class FavoriteBookLocalDataSource(context: Context) : IFavoriteBookDataSource {
    private val favoriteBookDao: IFavoriteBookDAO = AppRoomDatabase.getDatabase(context).favoriteBookDao()

    override suspend fun saveFavoriteBook(book: Book): Long {
        return favoriteBookDao.insertFavoriteBook(book.toFavoriteEntity())
    }

    override suspend fun getAllFavoriteBooks(): List<Book> {
        return favoriteBookDao.getAllFavoriteBooks().map { it.toDomain() }
    }

    override suspend fun isBookFavorite(key: String): Boolean {
        return favoriteBookDao.isBookFavorite(key)
    }

    override suspend fun removeFavoriteBook(key: String): Boolean {
        return favoriteBookDao.deleteFavoriteBook(key) > 0
    }
}