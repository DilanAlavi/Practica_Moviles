package com.ucb.data.expense

import com.ucb.domain.Expense

interface IExpenseDataSource {
    suspend fun saveExpense(expense: Expense): Long
    suspend fun getAllExpenses(): List<Expense>
    suspend fun getExpenseById(id: Long): Expense?
    suspend fun deleteExpense(id: Long): Boolean
}