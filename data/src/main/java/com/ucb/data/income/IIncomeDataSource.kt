package com.ucb.data.income

import com.ucb.domain.Income

interface IIncomeDataSource {
    suspend fun saveIncome(income: Income): Long
    suspend fun getAllIncomes(): List<Income>
    suspend fun getIncomeById(id: Long): Income?
    suspend fun deleteIncome(id: Long): Boolean
}