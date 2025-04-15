package com.ucb.framework.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_books")
data class FavoriteBookEntity(
    @PrimaryKey
    val key: String,
    val title: String,
    val authorNames: String,
    val firstPublishYear: Int?,
    val coverId: Int?,
    val coverEditionKey: String?,
    val savedDate: String
)