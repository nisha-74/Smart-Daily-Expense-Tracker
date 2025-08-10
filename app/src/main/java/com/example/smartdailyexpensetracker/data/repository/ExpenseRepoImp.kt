package com.example.smartdailyexpensetracker.data.repository

import com.example.smartdailyexpensetracker.data.local.ExpenseEntity
import com.example.smartdailyexpensetracker.data.local.ExpenseDao
import com.example.smartdailyexpensetracker.data.local.toDomain
import com.example.smartdailyexpensetracker.data.local.toEntity
import com.example.smartdailyexpensetracker.domen.model.Expense
import com.example.smartdailyexpensetracker.domen.repository.ExpanseRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExpenseRepoImp @Inject constructor(private  val expenseDao: ExpenseDao) :ExpanseRepo {
    override suspend fun addExpense(expense: Expense) {
        expenseDao.adExpense(expense.toEntity())

    }

    override fun getExpensesByDate(start: Long, end: Long) =
        expenseDao.getAllExpense(start, end).map { list ->
            list.map {
                it.toDomain()
            }
        }


    override fun getTotalSpentByDate(start: Long, end: Long): Flow<Double> =
        expenseDao.getTotalSpentByDate(start, end).map {
            it ?: 0.0
        }
}
