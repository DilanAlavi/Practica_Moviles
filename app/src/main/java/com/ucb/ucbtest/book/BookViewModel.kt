package com.ucb.ucbtest.book
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.data.NetworkResult
import com.ucb.domain.Book
import com.ucb.usecases.SearchBooks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val searchBooks: SearchBooks
) : ViewModel() {

    sealed class BookState {
        object Initial : BookState()
        object Loading : BookState()
        data class Success(val books: List<Book>) : BookState()
        data class Error(val message: String) : BookState()
    }

    private val _state = MutableStateFlow<BookState>(BookState.Initial)
    val state: StateFlow<BookState> = _state

    fun searchBooks(query: String) {
        if (query.isBlank()) {
            _state.value = BookState.Error("Por favor ingrese un término de búsqueda")
            return
        }

        _state.value = BookState.Loading
        viewModelScope.launch {
            when (val result = searchBooks.invoke(query)) {
                is NetworkResult.Success -> {
                    if (result.data.isEmpty()) {
                        _state.value = BookState.Error("No se encontraron libros para: $query")
                    } else {
                        _state.value = BookState.Success(result.data)
                    }
                }
                is NetworkResult.Error -> {
                    _state.value = BookState.Error(result.error)
                }
            }
        }
    }
}