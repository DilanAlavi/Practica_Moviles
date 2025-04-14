package com.ucb.data

import com.ucb.data.expense.IExpenseDataSource
import com.ucb.domain.Expense

class ExpenseRepository(private val dataSource: IExpenseDataSource) {

    suspend fun saveExpense(expense: Expense): Long {
        return dataSource.saveExpense(expense)
    }

    suspend fun getAllExpenses(): List<Expense> {
        return dataSource.getAllExpenses()
    }

    suspend fun getExpenseById(id: Long): Expense? {
        return dataSource.getExpenseById(id)
    }

    suspend fun deleteExpense(id: Long): Boolean {
        return dataSource.deleteExpense(id)
    }
}