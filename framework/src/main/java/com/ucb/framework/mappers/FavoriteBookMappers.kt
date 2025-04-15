package com.ucb.framework.mappers

import com.ucb.domain.Book
import com.ucb.framework.persistence.FavoriteBookEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Book.toFavoriteEntity(): FavoriteBookEntity {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val currentDate = dateFormat.format(Date())

    return FavoriteBookEntity(
        key = key,
        title = title,
        authorNames = author_name.joinToString(","),
        firstPublishYear = first_publish_year,
        coverId = cover_i,
        coverEditionKey = cover_edition_key,
        savedDate = currentDate
    )
}

fun FavoriteBookEntity.toDomain(): Book {
    return Book(
        key = key,
        title = title,
        author_name = authorNames.split(",").filter { it.isNotEmpty() },
        first_publish_year = firstPublishYear,
        cover_i = coverId,
        cover_edition_key = coverEditionKey
    )
}