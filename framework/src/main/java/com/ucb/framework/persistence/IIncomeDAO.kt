package com.ucb.framework.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IIncomeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncome(income: IncomeEntity): Long

    @Query("SELECT * FROM incomes ORDER BY date DESC")
    suspend fun getAllIncomes(): List<IncomeEntity>

    @Query("SELECT * FROM incomes WHERE id = :incomeId")
    suspend fun getIncomeById(incomeId: Long): IncomeEntity?

    @Query("DELETE FROM incomes WHERE id = :incomeId")
    suspend fun deleteIncome(incomeId: Long): Int
}