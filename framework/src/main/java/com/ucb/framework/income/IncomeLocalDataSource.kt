package com.ucb.framework.income

import android.content.Context
import com.ucb.data.income.IIncomeDataSource
import com.ucb.domain.Income
import com.ucb.framework.mappers.toDomain
import com.ucb.framework.mappers.toEntity
import com.ucb.framework.persistence.AppRoomDatabase
import com.ucb.framework.persistence.IIncomeDAO

class IncomeLocalDataSource(context: Context) : IIncomeDataSource {
    private val incomeDao: IIncomeDAO = AppRoomDatabase.getDatabase(context).incomeDao()

    override suspend fun saveIncome(income: Income): Long {
        return incomeDao.insertIncome(income.toEntity())
    }

    override suspend fun getAllIncomes(): List<Income> {
        return incomeDao.getAllIncomes().map { it.toDomain() }
    }

    override suspend fun getIncomeById(id: Long): Income? {
        return incomeDao.getIncomeById(id)?.toDomain()
    }

    override suspend fun deleteIncome(id: Long): Boolean {
        return incomeDao.deleteIncome(id) > 0
    }
}