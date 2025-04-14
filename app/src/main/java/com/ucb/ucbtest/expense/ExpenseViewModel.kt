package com.ucb.ucbtest.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.domain.Expense
import com.ucb.usecases.GetAllExpenses
import com.ucb.usecases.SaveExpense
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val saveExpense: SaveExpense,
    private val getAllExpenses: GetAllExpenses
) : ViewModel() {

    sealed class ExpenseState {
        object Loading : ExpenseState()
        data class Success(val expenses: List<Expense>) : ExpenseState()
        data class Error(val message: String) : ExpenseState()
    }

    private val _state = MutableStateFlow<ExpenseState>(ExpenseState.Loading)
    val state: StateFlow<ExpenseState> = _state

    private val _saveState = MutableStateFlow<Boolean?>(null)
    val saveState: StateFlow<Boolean?> = _saveState

    init {
        loadExpenses()
    }

    fun loadExpenses() {
        viewModelScope.launch {
            _state.value = ExpenseState.Loading
            try {
                val expenses = getAllExpenses.invoke()
                _state.value = ExpenseState.Success(expenses)
            } catch (e: Exception) {
                _state.value = ExpenseState.Error("Error al cargar gastos: ${e.message}")
            }
        }
    }

    fun saveExpense(name: String, amount: String, description: String) {
        if (name.isBlank() || amount.isBlank()) {
            _saveState.value = false
            return
        }

        val amountValue = amount.toDoubleOrNull()
        if (amountValue == null || amountValue <= 0) {
            _saveState.value = false
            return
        }

        viewModelScope.launch {
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val currentDate = dateFormat.format(Date())

                val expense = Expense(
                    name = name,
                    amount = amountValue,
                    description = description,
                    date = currentDate
                )

                saveExpense.invoke(expense)
                _saveState.value = true
                loadExpenses() // Recargar la lista después de guardar
            } catch (e: Exception) {
                _saveState.value = false
            }
        }
    }

    fun resetSaveState() {
        _saveState.value = null
    }
}