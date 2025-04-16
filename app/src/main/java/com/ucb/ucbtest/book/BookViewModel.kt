package com.ucb.ucbtest.book
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.data.NetworkResult
import com.ucb.domain.Book
import com.ucb.usecases.GetAllFavoriteBooks
import com.ucb.usecases.IsBookFavorite
import com.ucb.usecases.SearchBooks
import com.ucb.usecases.ToggleFavoriteBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val searchBooks: SearchBooks,
    private val toggleFavoriteBook: ToggleFavoriteBook,
    private val isBookFavorite: IsBookFavorite,
    private val getAllFavoriteBooks: GetAllFavoriteBooks
) : ViewModel() {

    sealed class BookState {
        object Initial : BookState()
        object Loading : BookState()
        data class Success(val books: List<Book>, val favoriteKeys: Set<String> = emptySet()) : BookState()
        data class Error(val message: String) : BookState()
    }

    private val _state = MutableStateFlow<BookState>(BookState.Initial)
    val state: StateFlow<BookState> = _state

    // Estado para controlar cuándo se ha completado la carga principal de resultados
    private val _resultsLoaded = MutableStateFlow(false)
    val resultsLoaded: StateFlow<Boolean> = _resultsLoaded

    // Método para búsqueda de libros
    fun searchBooks(query: String) {
        if (query.isBlank()) {
            _state.value = BookState.Error("Por favor ingrese un término de búsqueda")
            return
        }

        // Indicamos que los resultados están cargando y restablecer el estado de resultados cargados
        _state.value = BookState.Loading
        _resultsLoaded.value = false

        viewModelScope.launch {
            try {
                // Realizar la búsqueda
                val result = searchBooks.invoke(query)

                when (result) {
                    is NetworkResult.Success -> {
                        if (result.data.isEmpty()) {
                            _state.value = BookState.Error("No se encontraron libros para: $query")
                        } else {
                            // Primero mostramos los resultados sin verificar favoritos
                            _state.value = BookState.Success(result.data)

                            // Luego verificamos los favoritos en segundo plano
                            val favoriteKeys = mutableSetOf<String>()
                            result.data.forEach { book ->
                                if (isBookFavorite.invoke(book.key)) {
                                    favoriteKeys.add(book.key)
                                }
                            }

                            // Actualizar con la información de favoritos
                            _state.value = BookState.Success(result.data, favoriteKeys)

                            // Marcar que los resultados están completamente cargados
                            _resultsLoaded.value = true
                        }
                    }
                    is NetworkResult.Error -> {
                        _state.value = BookState.Error(result.error)
                    }
                }
            } catch (e: Exception) {
                _state.value = BookState.Error("Error al buscar libros: ${e.message}")
            }
        }
    }

    // Método para alternar favorito
    fun toggleFavorite(book: Book) {
        viewModelScope.launch {
            val wasToggled = toggleFavoriteBook.invoke(book)

            // Actualizar el estado de favoritos
            val currentState = _state.value
            if (currentState is BookState.Success) {
                val updatedFavorites = currentState.favoriteKeys.toMutableSet()

                if (wasToggled) {
                    if (isBookFavorite.invoke(book.key)) {
                        updatedFavorites.add(book.key)
                    } else {
                        updatedFavorites.remove(book.key)
                    }
                }

                _state.value = BookState.Success(currentState.books, updatedFavorites)
            }
        }
    }

    // Método para cargar sólo los favoritos
    fun loadFavorites() {
        _state.value = BookState.Loading
        _resultsLoaded.value = false

        viewModelScope.launch {
            val favorites = getAllFavoriteBooks.invoke()
            if (favorites.isEmpty()) {
                _state.value = BookState.Error("No tienes libros favoritos guardados")
            } else {
                val favoriteKeys = favorites.map { it.key }.toSet()
                _state.value = BookState.Success(favorites, favoriteKeys)
                _resultsLoaded.value = true
            }
        }
    }

    // Método para reiniciar al estado inicial
    fun resetToInitialState() {
        _state.value = BookState.Initial
        _resultsLoaded.value = false
    }
}