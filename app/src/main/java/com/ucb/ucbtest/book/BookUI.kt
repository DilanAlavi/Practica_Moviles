package com.ucb.ucbtest.book

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ucb.domain.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookUI(
    viewModel: BookViewModel = hiltViewModel(),
    onFavoritesClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    val bookState by viewModel.state.collectAsState()
    val resultsLoaded by viewModel.resultsLoaded.collectAsState()
    val focusManager = LocalFocusManager.current
    var showFavoritesOnly by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Búsqueda de Libros") },
                actions = {
                    // Botón para alternar entre búsqueda y favoritos locales
                    IconButton(onClick = {
                        if (!showFavoritesOnly) {
                            viewModel.loadFavorites()
                            showFavoritesOnly = true
                        } else {
                            if (searchQuery.isNotBlank()) {
                                viewModel.searchBooks(searchQuery)
                            } else {
                                viewModel.resetToInitialState()
                            }
                            showFavoritesOnly = false
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = if (showFavoritesOnly) "Mostrar búsqueda" else "Mostrar favoritos locales"
                        )
                    }

                    // Botón para ir a la pantalla completa de favoritos
                    IconButton(onClick = onFavoritesClick) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Ver todos los favoritos"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Campo de búsqueda
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar libros") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = {
                        if (searchQuery.isNotBlank()) {
                            focusManager.clearFocus()
                            showFavoritesOnly = false
                            viewModel.searchBooks(searchQuery)
                        }
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    if (searchQuery.isNotBlank()) {
                        focusManager.clearFocus()
                        showFavoritesOnly = false
                        viewModel.searchBooks(searchQuery)
                    }
                }),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Estado de la búsqueda
            when (val state = bookState) {
                is BookViewModel.BookState.Initial -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Ingrese un término para buscar libros o vea sus favoritos")
                    }
                }
                is BookViewModel.BookState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is BookViewModel.BookState.Success -> {
                    // Mostrar el título apropiado
                    Text(
                        text = if (showFavoritesOnly) "Mis Libros Favoritos" else "Resultados de la búsqueda",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.books) { book ->
                            // Mostrar el botón de favorito solo cuando los resultados están completamente cargados
                            BookItem(
                                book = book,
                                isFavorite = state.favoriteKeys.contains(book.key),
                                onFavoriteClick = { viewModel.toggleFavorite(book) },
                                showFavoriteButton = resultsLoaded // Solo mostrar el botón cuando está cargado
                            )
                        }
                    }
                }
                is BookViewModel.BookState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(state.message)
                    }
                }
            }
        }
    }
}

@Composable
fun BookItem(
    book: Book,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    showFavoriteButton: Boolean = true
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Cover image
            if (book.cover_i != null) {
                AsyncImage(
                    model = "https://covers.openlibrary.org/b/id/${book.cover_i}-M.jpg",
                    contentDescription = "Book cover",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 16.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Image")
                }
            }

            // Book details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (book.author_name.isNotEmpty()) {
                    Text(
                        text = book.author_name.joinToString(", "),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                book.first_publish_year?.let {
                    Text(
                        text = "Año: $it",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Botón de favorito - solo se muestra cuando showFavoriteButton es true
            if (showFavoriteButton) {
                IconButton(
                    onClick = onFavoriteClick
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) "Eliminar de favoritos" else "Añadir a favoritos",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }
            } else {
                // Espacio placeholder cuando no se muestra el botón
                Spacer(modifier = Modifier.width(48.dp))
            }
        }
    }
}