package com.ucb.usecases

import com.ucb.data.ExpenseRepository
import com.ucb.domain.Expense

class SaveExpense(private val expenseRepository: ExpenseRepository) {
    suspend fun invoke(expense: Expense): Long {
        return expenseRepository.saveExpense(expense)
    }
}