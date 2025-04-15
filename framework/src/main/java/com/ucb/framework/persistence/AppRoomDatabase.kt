package com.ucb.framework.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        GitAccount::class,
        ExpenseEntity::class,
        IncomeEntity::class,
        FavoriteBookEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun githubDao(): IGitAccountDAO
    abstract fun expenseDao(): IExpenseDAO
    abstract fun incomeDao(): IIncomeDAO
    abstract fun favoriteBookDao(): IFavoriteBookDAO

    companion object {
        @Volatile
        var Instance: AppRoomDatabase? = null

        fun getDatabase(context: Context): AppRoomDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, AppRoomDatabase::class.java, "app_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}