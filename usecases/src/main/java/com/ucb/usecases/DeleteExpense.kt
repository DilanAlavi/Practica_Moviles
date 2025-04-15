package com.ucb.usecases

import com.ucb.data.ExpenseRepository

class DeleteExpense(private val expenseRepository: ExpenseRepository) {
    suspend fun invoke(id: Long): Boolean {
        return expenseRepository.deleteExpense(id)
    }
}