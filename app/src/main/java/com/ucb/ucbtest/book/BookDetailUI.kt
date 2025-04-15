package com.ucb.ucbtest.book

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ucb.domain.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailUI(book: Book, onBackPressed: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles del Libro") },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Cover image
            if (book.cover_i != null) {
                AsyncImage(
                    model = "https://covers.openlibrary.org/b/id/${book.cover_i}-L.jpg",
                    contentDescription = "Book cover",
                    modifier = Modifier
                        .height(300.dp)
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // Title
            Text(
                text = book.title,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Authors
            if (book.author_name.isNotEmpty()) {
                Text(
                    text = "Autor(es): ${book.author_name.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Publication year
            book.first_publish_year?.let {
                Text(
                    text = "Año de publicación: $it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Open Library Link
            val bookUrl = "https://openlibrary.org${book.key}"
            Button(
                onClick = { /* Aquí podrías abrir un navegador con el URL */ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Ver en Open Library")
            }
        }
    }
}