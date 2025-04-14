package com.ucb.usecases

import com.ucb.data.ExpenseRepository
import com.ucb.domain.Expense

class GetAllExpenses(private val expenseRepository: ExpenseRepository) {
    suspend fun invoke(): List<Expense> {
        return expenseRepository.getAllExpenses()
    }
}