package com.ucb.usecases

import com.ucb.data.IncomeRepository

class DeleteIncome(private val incomeRepository: IncomeRepository) {
    suspend fun invoke(id: Long): Boolean {
        return incomeRepository.deleteIncome(id)
    }
}