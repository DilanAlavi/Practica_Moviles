package com.ucb.usecases

import com.ucb.data.ExpenseRepository
import com.ucb.data.IncomeRepository
import javax.inject.Inject

class CheckSufficientBalance @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val incomeRepository: IncomeRepository
) {
    suspend fun invoke(newExpenseAmount: Double): Boolean {
        // Obtener todos los ingresos y gastos
        val expenses = expenseRepository.getAllExpenses()
        val incomes = incomeRepository.getAllIncomes()

        // Calcular el saldo actual
        val totalIncomes = incomes.sumOf { it.amount }
        val totalExpenses = expenses.sumOf { it.amount }

        // Verificar si el saldo actual + el nuevo gasto excede los ingresos
        val currentBalance = totalIncomes - totalExpenses

        // Retornar true si hay saldo suficiente, false si no
        return currentBalance >= newExpenseAmount
    }
}