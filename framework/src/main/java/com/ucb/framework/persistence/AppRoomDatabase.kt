package com.ucb.framework.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GitAccount::class, ExpenseEntity::class, IncomeEntity::class], version = 3, exportSchema = false)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun githubDao(): IGitAccountDAO
    abstract fun expenseDao(): IExpenseDAO
    abstract fun incomeDao(): IIncomeDAO

    companion object {
        @Volatile
        var Instance: AppRoomDatabase? = null

        fun getDatabase(context: Context): AppRoomDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, AppRoomDatabase::class.java, "app_database")
                    .fallbackToDestructiveMigration()  // Para manejar la migración de versión 2 a 3
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

