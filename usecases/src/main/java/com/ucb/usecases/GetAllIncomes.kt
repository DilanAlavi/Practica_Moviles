package com.ucb.usecases

import com.ucb.data.IncomeRepository
import com.ucb.domain.Income

class GetAllIncomes(private val incomeRepository: IncomeRepository) {
    suspend fun invoke(): List<Income> {
        return incomeRepository.getAllIncomes()
    }
}