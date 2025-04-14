package com.ucb.usecases

import com.ucb.data.ExpenseRepository
import com.ucb.data.IncomeRepository
import com.ucb.domain.Expense
import com.ucb.domain.Income
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

// Creamos un modelo combinado para facilitar el manejo
data class FinancialRecord(
    val id: Long,
    val name: String,
    val amount: Double,
    val description: String,
    val date: String,
    val isExpense: Boolean // true para gastos, false para ingresos
)

class GetAllFinancialRecords @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val incomeRepository: IncomeRepository
) {
    suspend fun invoke(): List<FinancialRecord> {
        val expenses = expenseRepository.getAllExpenses().map { it.toFinancialRecord(true) }
        val incomes = incomeRepository.getAllIncomes().map { it.toFinancialRecord(false) }

        // Combinamos ambas listas y ordenamos por fecha (más reciente primero)
        return (expenses + incomes).sortedByDescending { it.date }
    }

    private fun Expense.toFinancialRecord(isExpense: Boolean): FinancialRecord {
        return FinancialRecord(
            id = id,
            name = name,
            amount = amount,
            description = description,
            date = date,
            isExpense = isExpense
        )
    }

    private fun Income.toFinancialRecord(isExpense: Boolean): FinancialRecord {
        return FinancialRecord(
            id = id,
            name = name,
            amount = amount,
            description = description,
            date = date,
            isExpense = isExpense
        )
    }
}