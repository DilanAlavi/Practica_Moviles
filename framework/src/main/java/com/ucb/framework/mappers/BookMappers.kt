package com.ucb.framework.mappers

import com.ucb.domain.Book
import com.ucb.framework.dto.BookDto

fun BookDto.toModel(): Book {
    return Book(
        key = key,
        title = title,
        author_name = authorName ?: emptyList(),
        first_publish_year = firstPublishYear,
        cover_i = coverId,
        cover_edition_key = coverEditionKey
    )
}