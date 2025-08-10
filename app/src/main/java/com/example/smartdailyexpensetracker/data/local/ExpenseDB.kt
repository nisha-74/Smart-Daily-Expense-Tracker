package com.example.smartdailyexpensetracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ExpenseEntity::class], version = 2)
abstract  class ExpenseDB:RoomDatabase() {
    abstract  fun  expenseDao():ExpenseDao
}