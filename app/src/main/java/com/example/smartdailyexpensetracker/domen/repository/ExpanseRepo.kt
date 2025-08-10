package com.example.smartdailyexpensetracker.domen.repository

import com.example.smartdailyexpensetracker.data.local.ExpenseEntity
import com.example.smartdailyexpensetracker.domen.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpanseRepo {

    suspend fun  addExpense(expense: Expense)
    fun getExpensesByDate(start: Long, end: Long): Flow<List<Expense>>
    fun getTotalSpentByDate(start: Long, end: Long): Flow<Double>
}
