package com.ucb.framework.expense

import android.content.Context
import com.ucb.data.expense.IExpenseDataSource
import com.ucb.domain.Expense
import com.ucb.framework.mappers.toDomain
import com.ucb.framework.mappers.toEntity
import com.ucb.framework.persistence.AppRoomDatabase
import com.ucb.framework.persistence.IExpenseDAO

class ExpenseLocalDataSource(context: Context) : IExpenseDataSource {
    private val expenseDao: IExpenseDAO = AppRoomDatabase.getDatabase(context).expenseDao()

    override suspend fun saveExpense(expense: Expense): Long {
        return expenseDao.insertExpense(expense.toEntity())
    }

    override suspend fun getAllExpenses(): List<Expense> {
        return expenseDao.getAllExpenses().map { it.toDomain() }
    }

    override suspend fun getExpenseById(id: Long): Expense? {
        return expenseDao.getExpenseById(id)?.toDomain()
    }

    override suspend fun deleteExpense(id: Long): Boolean {
        return expenseDao.deleteExpense(id) > 0
    }
}