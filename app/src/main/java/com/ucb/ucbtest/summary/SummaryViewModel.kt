package com.ucb.ucbtest.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.usecases.FinancialRecord
import com.ucb.usecases.GetAllFinancialRecords
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val getAllFinancialRecords: GetAllFinancialRecords
) : ViewModel() {

    sealed class SummaryState {
        object Loading : SummaryState()
        data class Success(
            val records: List<FinancialRecord>,
            val totalIncome: Double,
            val totalExpense: Double
        ) : SummaryState()
        data class Error(val message: String) : SummaryState()
    }

    private val _state = MutableStateFlow<SummaryState>(SummaryState.Loading)
    val state: StateFlow<SummaryState> = _state

    init {
        loadRecords()
    }

    fun loadRecords() {
        viewModelScope.launch {
            _state.value = SummaryState.Loading
            try {
                val records = getAllFinancialRecords.invoke()

                // Calcular los totales
                val totalIncome = records.filter { !it.isExpense }.sumOf { it.amount }
                val totalExpense = records.filter { it.isExpense }.sumOf { it.amount }

                _state.value = SummaryState.Success(
                    records = records,
                    totalIncome = totalIncome,
                    totalExpense = totalExpense
                )
            } catch (e: Exception) {
                _state.value = SummaryState.Error("Error al cargar los registros: ${e.message}")
            }
        }
    }

    // Método para refrescar los datos desde otras pantallas
    fun refreshData() {
        loadRecords()
    }
}