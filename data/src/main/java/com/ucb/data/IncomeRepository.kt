package com.ucb.data

import com.ucb.data.income.IIncomeDataSource
import com.ucb.domain.Income

class IncomeRepository(private val dataSource: IIncomeDataSource) {

    suspend fun saveIncome(income: Income): Long {
        return dataSource.saveIncome(income)
    }

    suspend fun getAllIncomes(): List<Income> {
        return dataSource.getAllIncomes()
    }

    suspend fun getIncomeById(id: Long): Income? {
        return dataSource.getIncomeById(id)
    }

    suspend fun deleteIncome(id: Long): Boolean {
        return dataSource.deleteIncome(id)
    }
}