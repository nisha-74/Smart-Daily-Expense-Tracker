package com.example.smartdailyexpensetracker.domen.usecases

import com.example.smartdailyexpensetracker.domen.model.Expense
import com.example.smartdailyexpensetracker.domen.repository.ExpanseRepo
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(private  val repo:ExpanseRepo) {
    suspend operator fun  invoke(expense:Expense)= repo.addExpense(expense)

}