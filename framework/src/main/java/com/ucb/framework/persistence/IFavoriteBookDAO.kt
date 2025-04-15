package com.ucb.framework.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IFavoriteBookDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteBook(book: FavoriteBookEntity): Long

    @Query("SELECT * FROM favorite_books ORDER BY savedDate DESC")
    suspend fun getAllFavoriteBooks(): List<FavoriteBookEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_books WHERE `key` = :key LIMIT 1)")
    suspend fun isBookFavorite(key: String): Boolean

    @Query("DELETE FROM favorite_books WHERE `key` = :key")
    suspend fun deleteFavoriteBook(key: String): Int
}