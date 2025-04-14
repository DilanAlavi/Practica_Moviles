package com.ucb.usecases

import com.ucb.data.IncomeRepository
import com.ucb.domain.Income

class SaveIncome(private val incomeRepository: IncomeRepository) {
    suspend fun invoke(income: Income): Long {
        return incomeRepository.saveIncome(income)
    }
}