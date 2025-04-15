package com.ucb.ucbtest.income

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.domain.Income
import com.ucb.usecases.DeleteIncome
import com.ucb.usecases.GetAllIncomes
import com.ucb.usecases.SaveIncome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class IncomeViewModel @Inject constructor(
    private val saveIncome: SaveIncome,
    private val getAllIncomes: GetAllIncomes,
    private val deleteIncome: DeleteIncome
) : ViewModel() {

    sealed class IncomeState {
        object Loading : IncomeState()
        data class Success(val incomes: List<Income>) : IncomeState()
        data class Error(val message: String) : IncomeState()
    }

    private val _state = MutableStateFlow<IncomeState>(IncomeState.Loading)
    val state: StateFlow<IncomeState> = _state

    private val _saveState = MutableStateFlow<Boolean?>(null)
    val saveState: StateFlow<Boolean?> = _saveState

    private val _deleteState = MutableStateFlow<Boolean?>(null)
    val deleteState: StateFlow<Boolean?> = _deleteState

    init {
        loadIncomes()
    }

    fun loadIncomes() {
        viewModelScope.launch {
            _state.value = IncomeState.Loading
            try {
                val incomes = getAllIncomes.invoke()
                _state.value = IncomeState.Success(incomes)
            } catch (e: Exception) {
                _state.value = IncomeState.Error("Error al cargar ingresos: ${e.message}")
            }
        }
    }

    fun saveIncome(name: String, amount: String, description: String) {
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

                val income = Income(
                    name = name,
                    amount = amountValue,
                    description = description,
                    date = currentDate
                )

                saveIncome.invoke(income)
                _saveState.value = true
                loadIncomes() // Recargar la lista después de guardar
            } catch (e: Exception) {
                _saveState.value = false
            }
        }
    }

    fun deleteIncome(id: Long) {
        viewModelScope.launch {
            try {
                val success = deleteIncome.invoke(id)
                _deleteState.value = success
                if (success) {
                    loadIncomes() // Recargar la lista después de eliminar
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