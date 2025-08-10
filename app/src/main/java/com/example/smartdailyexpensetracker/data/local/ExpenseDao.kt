package com.example.smartdailyexpensetracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smartdailyexpensetracker.domen.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  adExpense(expense: ExpenseEntity)
    @Query("SELECT * FROM expense WHERE date BETWEEN :start AND :end ORDER BY date DESC")
    fun  getAllExpense(start: Long, end: Long):Flow<List<ExpenseEntity>>
    @Query("SELECT SUM(amount) FROM expense WHERE date BETWEEN :start AND :end")
    fun getTotalSpentByDate(start: Long, end: Long): Flow<Double?>
}