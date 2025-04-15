package com.ucb.usecases

import com.ucb.data.ExpenseRepository
import com.ucb.data.IncomeRepository
import javax.inject.Inject

class CheckBalance @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val incomeRepository: IncomeRepository
) {
    suspend fun invoke(): Double {
        val totalIncome = incomeRepository.getAllIncomes().sumOf { it.amount }
        val totalExpense = expenseRepository.getAllExpenses().sumOf { it.amount }

        return totalIncome - totalExpense
    }

    suspend fun hasSufficientFunds(amount: Double): Boolean {
        val balance = invoke()
        return balance >= amount
    }
}