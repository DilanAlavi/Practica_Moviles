package com.ucb.ucbtest.expense

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.domain.Expense
import com.ucb.usecases.DeleteExpense
import com.ucb.usecases.GetAllExpenses
import com.ucb.usecases.GetAllIncomes
import com.ucb.usecases.SaveExpense
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val getAllExpenses: GetAllExpenses,
    private val getAllIncomes: GetAllIncomes,
    private val deleteExpense: DeleteExpense,
    @ApplicationContext private val context: Context
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

    private val _deleteState = MutableStateFlow<Boolean?>(null)
    val deleteState: StateFlow<Boolean?> = _deleteState

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
                // Verificar si hay fondos suficientes
                val totalIncome = getAllIncomes.invoke().sumOf { it.amount }
                val totalExpense = getAllExpenses.invoke().sumOf { it.amount }
                val balance = totalIncome - totalExpense

                // Si no hay fondos suficientes, mostrar notificación
                if (balance < amountValue) {
                    Toast.makeText(
                        context,
                        "¡Alerta! Has registrado un gasto de Bs. ${String.format("%.2f", amountValue)} sin tener ingresos suficientes.",
                        Toast.LENGTH_LONG
                    ).show()
                }

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

    fun deleteExpense(id: Long) {
        viewModelScope.launch {
            try {
                val success = deleteExpense.invoke(id)
                _deleteState.value = success
                if (success) {
                    loadExpenses() // Recargar la lista después de eliminar
                }
            } catch (e: Exception) {
                _deleteState.value = false
            }
        }
    }

    fun resetSaveState() {
        _saveState.value = null
    }

    fun resetDeleteState() {
        _deleteState.value = null
    }
}